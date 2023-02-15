package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarSystem;

import java.util.List;

public class BDSixteenThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetFour;
    private BaseThemeExample widgetFive;
    private BaseThemeExample widgetSix;
    private BDSixteenStarDrawer bdSixteenDrawer;

    public BDSixteenThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1);
        stayAction.setZView(0);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 270, 360).setIsNoZaxis(true).build();
        isNoZaxis = true;
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).build();
        bdSixteenDrawer = new BDSixteenStarDrawer(new BDSixteenStarSystem(BDSixteenStarSystem.StartType.HEART));
    }

    @Override
    public void drawWiget() {
        widgetFour.drawFrame();
        widgetFive.drawFrame();
        widgetSix.drawFrame();
        widgetTwo.drawFrame();
        widgetOne.drawFrame();
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


        widgetFour = new BaseThemeExample(totalTime, 500, 2000, DEFAULT_OUT_TIME, true);
        widgetFour.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetFour.setZView(-0.0f);

        widgetFive = new BaseThemeExample(totalTime, 1000, 2000, DEFAULT_OUT_TIME, true);
        widgetFive.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetFive.setZView(-0.0f);

        widgetSix = new BaseThemeExample(totalTime, 2000, 2000, DEFAULT_OUT_TIME, true);
        widgetSix.setStayAction(new AnimationBuilder(totalTime).setZView(0).setIsNoZaxis(true).
                setCoordinate(0, 2.5f, 0f, -1.5f).build());
        widgetSix.setZView(-0.0f);

    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        if (ObjectUtils.isEmpty(bitmap) || ObjectUtils.isEmpty(mimaps)) {
            return;
        }
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        pos = new float[]{
                -1, 1,
                -1, 0.7f,
                1, 1,
                1, 0.7f,
        };
        widgetOne.setVertex(pos);

        float scaleX = width < height ? 4 : (width == height ? 4 : 4);
        float centerX = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        float centerY = width < height ? 0.66f : (width == height ? 0.65f : 0.6f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 7 : (width == height ? 5 : 4);
        centerX = width < height ? 0.7f : (width == height ? 0.8f : 0.8f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetFour.init(mimaps.get(2), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 7 : (width == height ? 5 : 4);
        centerX = width < height ? -0.8f : (width == height ? -0.9f : -0.9f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetFive.init(mimaps.get(3), width, height);
        widgetFive.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 7 : (width == height ? 5 : 4);
        centerX = width < height ? 0.8f : (width == height ? 0.9f : 0.9f);
        centerY = width < height ? -1.4f : (width == height ? -1.4f : -1.4f);
        widgetSix.init(mimaps.get(3), width, height);
        widgetSix.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);

        bdSixteenDrawer.init(mimaps.get(4));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetSix.onDestroy();
        widgetFour.onDestroy();
        widgetFive.onDestroy();
        bdSixteenDrawer.onDestroy();
    }

}
