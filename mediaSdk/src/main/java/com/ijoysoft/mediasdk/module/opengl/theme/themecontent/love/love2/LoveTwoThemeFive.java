package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/21
 * @ description
 */
public class LoveTwoThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public LoveTwoThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -2f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 180, 0).
                setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0, 2.5f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 0).build();
        setZView(-2.5f);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.5f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(-1, 0f, 0f, 0f)
                .build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f)
                .setZView(-2.5f).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(2, 0f, 0f, 0f)
                .build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1f)
                .setZView(-2.5f).build());
    }

    @Override
    public void drawWiget() {
        widgetTwo.drawFrame();
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width == height ? 4f : (width < height ? 3f : 4f);
        float scaleY = width == height ? 4f : (width < height ? 3f : 4f);
        float centerX = width == height ? 0.7f : (width < height ? 0f : 0.8f);
        float centerY = width == height ? 0.7f : (width < height ? 0.8f : 0.7f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleY);

        scaleX = width == height ? 4f : (width < height ? 3f : 4f);
        scaleY = width == height ? 4f : (width < height ? 3f : 4f);
        centerX = width == height ? -0.7f : (width < height ? 0f : 0f);
        centerY = width == height ? 0.7f : (width < height ? 0.8f : 0.7f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleY);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
