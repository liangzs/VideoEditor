package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday17;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class BDSeventeenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;


    public BDSeventeenThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
//        starClusterDrawer = new StarClusterDrawer(new BDFiveStarSystemOne());
    }

    @Override
    public void initWidget() {


        widgetOne = new BaseThemeExample(totalTime, 0, 2000, DEFAULT_OUT_TIME, true);
        widgetOne.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetOne.setZView(-0.0f);

        widgetTwo = new BaseThemeExample(totalTime, 400, 2000, DEFAULT_OUT_TIME, true);
        widgetTwo.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetTwo.setZView(-0.0f);

        widgetThree = new BaseThemeExample(totalTime, 1500, 2000, DEFAULT_OUT_TIME, true);
        widgetThree.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetThree.setZView(-0.0f);
    }

    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float scaleX = width < height ? 8 : (width == height ? 6 : 6);
        float centerX = 0.6f;
//        float centerY = width < height ? 0.8f : (width == height ? 0.8f : 0.75f);
        float centerY = -1.4f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 6 : (width == height ? 4 : 5);
        centerX = 0.8f;
        centerY = -1.4f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 6 : (width == height ? 4 : 5);
        centerX = -0.7f;
        centerY = -1.4f;
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }


}
