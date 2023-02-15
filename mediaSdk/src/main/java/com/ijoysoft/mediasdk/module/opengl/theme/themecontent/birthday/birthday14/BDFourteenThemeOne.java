package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14;

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
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenDrawerOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle.BDFourteenSystemOne;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class BDFourteenThemeOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private float stayValue = 1;
    private int intensityLocation = 0;

    private BDFourteenDrawerOne bdFourteenDrawer;


    public BDFourteenThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2.5f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //widget
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setFade(0, 1.0f).setIsWidget(true).setIsNoZaxis(true).build());
//        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);

        //widget
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 1500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, -1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).setEnterDelay(1000).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(1).build());

        widgetTwo.setZView(0);


        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME * 2, 3000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setIsWidget(true).setIsNoZaxis(true).setEnterDelay(1000)));
        widgetThree.setZView(0);
        bdFourteenDrawer = new BDFourteenDrawerOne(new BDFourteenSystemOne(3500));

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        bdFourteenDrawer.onDrawFrame();
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

    //需要放在这里，因为需要用useprogram 这里注意的是颜色值在[0,1]之间这里注意的是颜色值在[0,1]之间
    //暖色的颜色。是加强R/G来完成。  float[] warmFilterColorData = {0.1f, 0.1f, 0.0f};
    //冷色系的调整。简单的就是增加B的分量float[] coolFilterColorData = {0.0f, 0.0f, 0.1f};
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(intensityLocation, stayValue);
//        GLES20.glUniform3fv(colorLocation, 1, new float[]{0.1f, 0.1f, 0.0f}, 0);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        widgetOne.init(mimaps.get(0), width, height);
        float[] pos = new float[]{
                -1, 1f,
                -1, 0.8f,
                1, 1f,
                1, 0.8f
        };
        widgetOne.setVertex(pos);
        widgetTwo.init(mimaps.get(1), width, height);
        pos = width < height ? new float[]{
                -1, -0.75f,
                -1, -1,
                1, -0.75f,
                1, -1
        } : (width == height ? new float[]{
                -1, -0.7f,
                -1, -1,
                1, -0.7f,
                1, -1
        } : new float[]{
                -1, -0.5f,
                -1, -1,
                1, -0.5f,
                1, -1
        });
        widgetTwo.setVertex(pos);

        widgetThree.init(mimaps.get(2), width, height);
        pos = width < height ? new float[]{
                0.5f, 0.8f,
                0.5f, 0.6f,
                1, 0.8f,
                1, 0.6f
        } : (width == height ? new float[]{
                0.6f, 0.8f,
                0.6f, 0.5f,
                1, 0.8f,
                1, 0.5f
        } : new float[]{
                0.7f, 0.8f,
                0.7f, 0.4f,
                1, 0.8f,
                1, 0.4f
        });
        widgetThree.setVertex(pos);
        intensityLocation = GLES20.glGetUniformLocation(mProgram, "intensity");
        bdFourteenDrawer.init(mimaps.get(3));
    }


    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.readShaderFromRawResource(R.raw.theme_two_mixwarm));
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
        if (bdFourteenDrawer != null) {
            bdFourteenDrawer.onDestroy();
        }
    }


    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

}
