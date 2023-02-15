package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

public class BDTenThemeFive extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetBorder;

    public BDTenThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setZView(-2.5f).build();

        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setZView(-2.5f).build();

        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setScale(1.04f, 1.04f).setZView(-2.5f).build());
        widgetBorder.setStayAction(new AnimationBuilder(3000).setIsNoZaxis(true).setZView(0).build());
        widgetBorder.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).build());
    }


    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.ENTER) {
            widgetBorder.setNoZaxis(false);
        } else {
            widgetBorder.setNoZaxis(true);
        }
        widgetBorder.drawFrame();
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2).setCoordinate(-1, 0f, 0f, 0f)
                .build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -1f, 0f)
                .setZView(-2f).build());


        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setCoordinate(1, 0f, 0f, 0f)
                .build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 1f, 0f)
                .setZView(-2f).build());
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetBorder.init(mimaps.get(2), width, height);
        widgetBorder.setVertex(pos);
        float scaleX = width < height ? 6 : (width == height ? 5 : 4);
        float centerX = width < height ? -0.6f : (width == height ? -0.6f : -0.7f);
        float centerY = width < height ? 0.6f : (width == height ? 0.6f : 0.6f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
        widgetOne.setStayAction(new StayShakeAnimation(3000, width, height, -0.65f, 0.7f, 10, -2f));

        scaleX = width < height ? 6 : (width == height ? 5 : 4);
        centerX = width < height ? 0.6f : (width == height ? 0.6f : 0.7f);
        centerY = width < height ? 0.6f : (width == height ? 0.6f : 0.6f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);
        widgetTwo.setStayAction(new StayShakeAnimation(3000, width, height, 0.65f, 0.7f, 10, -2f));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetBorder.onDestroy();
    }

}
