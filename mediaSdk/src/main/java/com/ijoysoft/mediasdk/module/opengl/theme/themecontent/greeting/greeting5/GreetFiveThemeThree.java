package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.flowercluster.FlowClusterDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.flowercluster.WidgetThreeFlowerSystem;

import java.util.List;

/**
 *
 */
public class GreetFiveThemeThree extends BaseBlurThemeExample {

    private BaseThemeExample widgetBorder;
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    private FlowClusterDrawer flowClusterDrawer;

    public GreetFiveThemeThree(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, isNoZaxis);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build());
//        //因为纹理坐标和屏幕坐标是相反的，所以这里需要y值也取反
        flowClusterDrawer = new FlowClusterDrawer(new WidgetThreeFlowerSystem());

        widgetOne = new BaseThemeExample(totalTime, 1700, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setEnterDelay(500).setScale(0.01f, 1f).build());
        float centerX = width < height ? 0.7f : (width == height ? 0.8f : 0.8f);
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, centerX, 1);
        widgetOne.setZView(0);
//        //因为纹理坐标和屏幕坐标是相反的，所以这里需要y值也取反
        widgetTwo = new BaseThemeExample(totalTime, 600, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(600).setFade(0, 1f).setIsWidget(true).setIsNoZaxis(true).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0)
                .setCoordinate(0, -1, 0, 0).build());
    }

    @Override
    public void drawWiget() {
        widgetBorder.drawFrame();
        widgetThree.drawFrame();
        widgetOne.drawFrame();
        if (getStatus() != ActionStatus.ENTER) {
            widgetTwo.drawFrame();
            flowClusterDrawer.onDrawFrame();
        }
    }


    //需要放在这里，因为需要用useprogram 这里注意的是颜色值在[0,1]之间这里注意的是颜色值在[0,1]之间
    //暖色的颜色。是加强R/G来完成。  float[] warmFilterColorData = {0.1f, 0.1f, 0.0f};
    //冷色系的调整。简单的就是增加B的分量float[] coolFilterColorData = {0.0f, 0.0f, 0.1f};
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        widgetBorder.init(mimaps.get(0), width, height);
        widgetBorder.setVertex(pos);
        //one
        widgetOne.init(mimaps.get(1), width, height);
        float scale = width < height ? 2 : (width == height ? 2 : 2);
        float centerX = width < height ? 0.7f : (width == height ? 0.6f : 0.7f);
        widgetOne.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, centerX, scale);

        //two
        widgetTwo.init(mimaps.get(2), width, height);
        float[] temp = width <= height ? new float[]{
                -1.2f, -0.6f,
                -1.2f, -1.2f,
                -0.3f, -0.6f,
                -0.3f, -1.2f
        } : new float[]{
                -1.2f, -0.4f,
                -1.2f, -1.2f,
                -0.3f, -0.4f,
                -0.3f, -1.2f
        };
        widgetTwo.setVertex(temp);

        widgetThree.init(mimaps.get(3), width, height);
        scale = width < height ? 2 : (width == height ? 2 : 3);
        centerX = width < height ? 0.3f : (width == height ? 0.3f : 0.1f);
        float centerY = width < height ? -0.9f : (width == height ? -0.85f : -0.8f);
        widgetThree.adjustScaling(width, height, mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scale, scale);

        flowClusterDrawer.init(mimaps.subList(4, mimaps.size()), R.raw.theme_congra_flower_cluster_vertex, R.raw.theme_congra_flower_cluster_three_fragment);
    }


    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
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
