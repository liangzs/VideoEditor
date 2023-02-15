package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.flowercluster.FlowClusterDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.flowercluster.WidgetOneFlowerSystem;

import java.util.List;

/**
 * 控件分两种
 * 1、有伸缩功能，不断变大
 * 2、有自旋转功能，原地旋转
 */
public class GreetFiveThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetBorder;

    private FlowClusterDrawer flowClusterDrawer;
    private int width, height;
    private float[] posOneOffset = new float[8];//计算控件出现动画
    private float[] posOneVertex = new float[8];//计算控件出现动画
    private float[] posTwoOffset = new float[8];//计算控件出现动画
    private float[] posTwoVertex = new float[8];//计算控件出现动画
    private float widgetEneterCount;
    private float widgetEneterIndex;

    public GreetFiveThemeOne(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setIsNoZaxis(true).build();
//        enterAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, isNoZaxis);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setIsNoZaxis(true).build());


        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0)
                .setCoordinate(0, 1, 0, 0).build());
//        //因为纹理坐标和屏幕坐标是相反的，所以这里需要y值也取反
//        bgImageFilter = new ImageOriginFilter();
        widgetEneterIndex = 0;
        flowClusterDrawer = new FlowClusterDrawer(new WidgetOneFlowerSystem());
    }


    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetThree.drawFrame();
        if (getStatus() != ActionStatus.ENTER) {
            if (getStatus() == ActionStatus.STAY && widgetEneterIndex < widgetEneterCount) {
                widgetEneterIndex++;
                arrayAdd();
                widgetOne.setVertex(posOneVertex);
                widgetTwo.setVertex(posTwoVertex);
            }
            widgetOne.drawFrame();
            widgetTwo.drawFrame();
            if (widgetEneterIndex >= widgetEneterCount) {
                flowClusterDrawer.onDrawFrame();
            }
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        this.width = width;
        this.height = height;
        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);
        //one
        widgetOne.init(mimaps.get(1), width, height);
        //two
        widgetTwo.init(mimaps.get(2), width, height);
        widgetThree.init(mimaps.get(3), width, height);
        float scale = width < height ? 2 : (width == height ? 2 : 3);
        float centerX = width < height ? 0.3f : (width == height ? 0.3f : 0.1f);
        float centerY = width < height ? 0.85f : (width == height ? 0.85f : 0.8f);
        widgetThree.adjustScaling(width, height, mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scale, scale);
        //顶点坐标
        //顶点变化动画
        calculateWidgetOnePos();
        widgetOne.setVertex(new float[8]);
        widgetTwo.setVertex(new float[8]);
        flowClusterDrawer.init(mimaps.subList(4, mimaps.size()), R.raw.theme_congra_flower_cluster_vertex, R.raw.theme_congra_flower_cluster_fragment);
    }

    private void calculateWidgetOnePos() {
        widgetEneterCount = (600 * ConstantMediaSize.FPS / 1000);
        //one
        float end[] = width <= height ? new float[]{
                -1.0f, 1f,
                -1.0f, 0.6f,
                -0.3f, 1f,
                -0.3f, 0.6f,
        } : new float[]{
                -1.0f, 1f,
                -1.0f, 0.4f,
                -0.3f, 1f,
                -0.3f, 0.4f,
        };
        posOneVertex = new float[]{
                -0.3f, 1f,
                -0.3f, 1f,
                -0.3f, 1f,
                -0.3f, 1f,
        };
        for (int i = 0; i < end.length; i++) {
            posOneOffset[i] = (end[i] - posOneVertex[i]) / widgetEneterCount;
        }
        //two
        end = width < -height ? new float[]{
                0.3f, -0.7f,
                0.3f, -1f,
                1f, -0.7f,
                1f, -1f,
        } : new float[]{
                0.3f, -0.6f,
                0.3f, -1f,
                1f, -0.6f,
                1f, -1f,
        };
        posTwoVertex = new float[]{
                1f, -0.7f,
                1f, -0.7f,
                1f, -0.7f,
                1f, -0.7f,
        };
        for (int i = 0; i < end.length; i++) {
            posTwoOffset[i] = (end[i] - posTwoVertex[i]) / widgetEneterCount;
        }
    }

    private void arrayAdd() {
        for (int i = 0; i < posOneVertex.length; i++) {
            posOneVertex[i] = posOneVertex[i] + posOneOffset[i];
        }
        for (int i = 0; i < posTwoVertex.length; i++) {
            posTwoVertex[i] = posTwoVertex[i] + posTwoOffset[i];
        }
    }


    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
//        bgImageFilter.onSizeChanged(width, height);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
//        bgImageFilter.create();
        flowClusterDrawer.onSurfaceCreated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (flowClusterDrawer != null) {
            flowClusterDrawer.onDestroy();
        }
        widgetOne.onDestroy();
        widgetThree.onDestroy();
        widgetTwo.onDestroy();
        widgetBorder.onDestroy();
    }

}
