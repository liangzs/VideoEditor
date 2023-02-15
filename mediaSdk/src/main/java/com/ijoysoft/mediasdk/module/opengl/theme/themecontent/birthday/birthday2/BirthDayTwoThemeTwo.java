package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class BirthDayTwoThemeTwo extends BaseThemeExample {
    private int intensityLocation = 0;
    private float stayValue = 1;
    private BaseThemeExample widgetOne;

    public BirthDayTwoThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;


        widgetOne = new BaseThemeExample(totalTime, 2500, 0, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(2500).setCoordinate(0, 1f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM).setZView(0).setIsNoZaxis(true)));
        widgetOne.setZView(0);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).setFade(1, 0).build());
    }


    @Override
    public void init(Bitmap bitmap, Bitmap bitmap1, List<Bitmap> list, int width, int height) {
        super.init(bitmap, bitmap1, list, width, height);
        intensityLocation = GLES20.glGetUniformLocation(mProgram, "intensity");
        widgetOne.init(list.get(0), width, height);
        float scale = width < height ? 1.5f : 2f;
        float centerX = width < height ? -0.3f : (width == height ? -0.4f : -0.4f);
        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.7f);
        widgetOne.adjustScaling(width, height, list.get(0).getWidth(), list.get(0).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(intensityLocation, stayValue);
    }

    @Override
    public void drawWiget() {
        super.drawWiget();
        widgetOne.drawFrame();
    }

    /**
     * 复写这里的方法，传入滤镜
     */
    @Override
    public void drawFrame() {
        if (status == ActionStatus.STAY) {
            stayValue = (stayAction.getFrameValue() - 1f) * 10;
            stayValue = stayValue < 0 ? 0 : stayValue;
            stayValue = 1 - stayValue;
        }
        super.drawFrame();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.readShaderFromRawResource(R.raw.theme_two_mixcold));
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
    }
}
