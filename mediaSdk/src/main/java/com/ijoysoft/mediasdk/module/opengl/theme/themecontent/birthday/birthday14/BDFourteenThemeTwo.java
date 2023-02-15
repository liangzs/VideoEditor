package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenDrawerTwo;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class BDFourteenThemeTwo extends BaseThemeExample {
    private int intensityLocation = 0;
    private float stayValue = 1;
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BDFourteenDrawerTwo fourteenDrawer;

    public BDFourteenThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;

        fourteenDrawer = new BDFourteenDrawerTwo();

        //widget
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 1.0f).setIsWidget(true).setIsNoZaxis(true).build());
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);

        //widget
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1.0f, 0f, 0f).setZView(0).setIsNoZaxis(true).build());
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetTwo.setZView(0);

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1.0f, 0f, 0f).setZView(0).setIsNoZaxis(true).build());
        widgetThree.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        fourteenDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        intensityLocation = GLES20.glGetUniformLocation(mProgram, "intensity");

        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = width < height ? new float[]{
                -1, -0.8f,
                -1, -1f,
                1, -0.8f,
                1, -1f
        } : (width == height ? new float[]{
                -1, -0.8f,
                -1, -1f,
                1, -0.8f,
                1, -1f
        } : new float[]{
                -1, -0.8f,
                -1, -1f,
                1, -0.8f,
                1, -1f
        });

        float scale = width < height ? 4 : (width == height ? 3 : 2);
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height / 2, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.LEFT_BOTTOM, scale);

        widgetThree.init(mimaps.get(1), width, height);
        widgetThree.adjustScaling(width, height / 2, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.RIGHT_BOTTOM, scale);
        widgetTwo.setStayAction(new StayShakeAnimation(3000, width, height, -0.5f, 1f, 5, -0));
        widgetThree.setStayAction(new StayShakeAnimation(3000, width, height, 0.5f, 1f, 5, -0));
        fourteenDrawer.init(mimaps.get(2));
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(intensityLocation, stayValue);
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
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        if (fourteenDrawer != null) {
            fourteenDrawer.onDestroy();
        }
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }
}
