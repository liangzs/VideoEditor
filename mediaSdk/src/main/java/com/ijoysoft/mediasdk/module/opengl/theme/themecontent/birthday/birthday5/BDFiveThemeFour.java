package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.startcluster.BDFiveStarSystemTwo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.startcluster.StarClusterDrawer;

import java.util.List;

/**
 * 缩小-放大-缩小
 */
public class BDFiveThemeFour extends BaseThemeExample {
    //    private BaseThemeExample widgetOne;
    private float stayValue = 1;
    private int intensityLocation = 0;

    //挤压变形
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private float[] origin = new float[8];

    private float widgetEneterCount;
    private float widgetEneterIndex;
    private float[] posOneOffset = new float[8];//计算控件出现动画
    private StarClusterDrawer starClusterDrawer;

    public BDFiveThemeFour(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 3, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, -2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));
        this.isNoZaxis = isNoZaxis;
        starClusterDrawer = new StarClusterDrawer(new BDFiveStarSystemTwo());
    }


    /**
     * 复写这里的方法，传入滤镜
     */
    @Override
    public void drawFrame() {
        if (status == ActionStatus.STAY) {
            stayValue = (stayAction.getFrameCount() - stayAction.getFrameIndex()) * 1f / stayAction.getFrameCount() * 1f;
        }
        super.drawFrame();
    }

    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(intensityLocation, stayValue);
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setStayAction(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setZView(0);

        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, -1, 0, 0).setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetTwo.setZView(0);
    }

    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.STAY) {
            if (widgetEneterIndex < widgetEneterCount) {
                arrayAdd();
            } else if (widgetEneterIndex > widgetEneterCount && widgetEneterIndex < widgetEneterCount * 2) {
                arraySub();
            }
            widgetEneterIndex++;
            widgetOne.setVertex(origin);
        }
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        starClusterDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        intensityLocation = GLES20.glGetUniformLocation(mProgram, "intensity");
        //one
        float scaleX = width < height ? 2 : (width == height ? 3 : 3);
        float centerX = width < height ? 0.5f : (width == height ? 0.65f : 0.65f);
        float centerY = width < height ? 0.83f : (width == height ? 0.8f : 0.7f);
        widgetOne.init(mimaps.get(0), width, height);
        origin = widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 1.5f : (width == height ? 2 : 3);
        centerX = 0;
        centerY = width < height ? -0.82f : (width == height ? -0.75f : -0.75f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        evaluateShade();
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private void evaluateShade() {
        float[] end = new float[8];
        end[0] = origin[0] + 0.1f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] + 0.1f;
        end[3] = origin[3] - 0.1f;
        end[4] = origin[4] - 0.1f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] - 0.1f;
        end[7] = origin[7] - 0.1f;
        widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
        for (int i = 0; i < end.length; i++) {
            posOneOffset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }
    }

    private void arrayAdd() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] + posOneOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] - posOneOffset[i];
        }
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.readShaderFromRawResource(R.raw.theme_two_mixgray));
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        starClusterDrawer.onDestroy();
    }
}
