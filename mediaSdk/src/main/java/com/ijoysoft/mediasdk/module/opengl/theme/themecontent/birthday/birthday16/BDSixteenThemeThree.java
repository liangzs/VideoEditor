package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarSystem;

import java.util.List;

public class BDSixteenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;
    private BDSixteenStarDrawer bdSixteenDrawer;

    public BDSixteenThemeThree(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        stayAction.setZView(0.0f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setIsNoZaxis(true).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).build();
        bdSixteenDrawer = new BDSixteenStarDrawer(new BDSixteenStarSystem(BDSixteenStarSystem.StartType.LINE));
    }


    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
        widgetFour.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        if (getStatus() != ActionStatus.ENTER) {
            bdSixteenDrawer.onDrawFrame();
        }
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setZView(-0.0f);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setZView(-0.0f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-0.0f).setIsNoZaxis(true).
                setCoordinate(0, 1f, 0f, 0f).build());

        widgetThree = new BaseThemeExample(totalTime, 2200, 2000, DEFAULT_OUT_TIME, true);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1000).setCoordinate(0, -1.0f, 0f, 0f).setZView(-0.0f)));
        widgetThree.setZView(-0.0f);
        widgetFour = new BaseThemeExample(totalTime, 2200, 2000, DEFAULT_OUT_TIME, true);
        widgetFour.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1500).setCoordinate(0, -1.0f, 0f, 0f).setZView(-0.0f)));
        widgetFour.setZView(-0.0f);
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        pos = new float[]{
                -1, -0.7f,
                -1, -1,
                1, -0.7f,
                1, -1
        };
        widgetOne.setVertex(pos);

        float scaleX = width < height ? 1.5f : (width == height ? 1.5f : 1.8f);
        float centerX = 0;
        float centerY = width < height ? 0.9f : (width == height ? 0.85f : 0.78f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 5 : (width == height ? 4 : 4);
        centerX = width < height ? 0.5f : (width == height ? 0.6f : 0.75f);
        centerY = width < height ? -0.88f : (width == height ? -0.75f : -0.65f);
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 5 : (width == height ? 4 : 4);
        centerX = width < height ? 0.75f : (width == height ? 0.85f : 0.85f);
        centerY = width < height ? -0.7f : (width == height ? -0.7f : -0.4f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);

        bdSixteenDrawer.init(mimaps.get(4));

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
        bdSixteenDrawer.onDestroy();
    }


}
