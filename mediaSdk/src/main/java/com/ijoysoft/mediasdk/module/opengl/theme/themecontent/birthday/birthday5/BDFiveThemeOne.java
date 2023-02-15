package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5;

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
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.startcluster.BDFiveStarSystemOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.startcluster.StarClusterDrawer;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class BDFiveThemeOne extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private float stayValue = 1;
    private int intensityLocation = 0;
    private StarClusterDrawer starClusterDrawer;

    public BDFiveThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0, -2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;

        //widget
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 2, 0, 0).setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetOne.setZView(0);
//        //因为纹理坐标和屏幕坐标是相反的，所以这里需要y值也取反
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 2, 0, 0).setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).build());
        widgetTwo.setZView(0);

        starClusterDrawer = new StarClusterDrawer(new BDFiveStarSystemOne());

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        starClusterDrawer.onDrawFrame();
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
        //one
        float scaleX = width < height ? 2 : (width == height ? 3 : 2);
        float centerX = width < height ? 0.6f : (width == height ? 0.7f : 0.7f);
        float centerY = width < height ? 0.72f : (width == height ? 0.7f : 0.6f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 3 : (width == height ? 4 : 3);
        centerX = width < height ? -0.6f : (width == height ? -0.7f : -0.7f);
        centerY = width < height ? 0.82f : (width == height ? 0.75f : 0.7f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        intensityLocation = GLES20.glGetUniformLocation(mProgram, "intensity");
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
//        mixGrayFilter.onSizeChanged(width, height);
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
