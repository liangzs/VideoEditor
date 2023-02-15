package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration7;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左下角z轴翻滚进，右y轴翻转出
 */
public class CDSevenThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;


    public CDSevenThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 90).build();

        float centerX = width < height ? -0.9f : -0.9f;
        float centerY = -0.6f;
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.1f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0, -1, 0).build());
        widgetOne.setZView(0);
        //two
        centerX = width < height ? 0.85f : 0.85f;
        centerY = 0.8f;
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(0.1f, 1f).setIsNoZaxis(true).setZView(0).build());
        widgetTwo.getEnterAnimation().setIsSubAreaWidget(true, centerX, centerY);
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 0, 1, 0).build());
        widgetTwo.setZView(0);
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scale = width < height ? 4 : (width == height ? 4 : 4);
        float centerX = width < height ? -0.9f : -0.9f;
        float centerY = -0.6f;
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        scale = width < height ? 3f : (width == height ? 3 : 3);
        centerX = width < height ? 0.85f : 0.85f;
        centerY = 0.8f;
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }
}
