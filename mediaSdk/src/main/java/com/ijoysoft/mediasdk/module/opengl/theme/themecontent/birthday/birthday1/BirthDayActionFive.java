package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneThemeManager.BD_ONE_ZVIEW;

/**
 * 左下角z轴翻滚进，右y轴翻转出
 */
public class BirthDayActionFive extends BaseThemeExample {

    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public BirthDayActionFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height, BD_ONE_ZVIEW);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).setZView(BD_ONE_ZVIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 90).setZView(BD_ONE_ZVIEW).build();
        setZView(BD_ONE_ZVIEW);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0f, -1f, 0, 0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0f, 0f, 0, -1).build());
        widgetOne.setZView(0);
        //two
        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0f, 1f, 0, 0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0f, 0f, 0, 1).build());
        widgetTwo.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width <= height ? 1.5f : 2;
        float centerX = 0;
        float centerY = width <= height ? -0.9f : -0.85f;
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
        widgetTwo.init(mimaps.get(1), width, height);
        scale = width <= height ? 1.5f : 2;
        centerY = width <= height ? 0.8f : 0.65f;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
