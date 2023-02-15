package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 右上角x轴翻滚进，x反相下方出
 */
public class CDFiveThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public CDFiveThemeThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, -2f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).build();
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_X);
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 180).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 1700, 0, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1.0f, 0f, 0f).setEnterDelay(500).setZView(-2.4f)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());
        widgetOne.setZView(-2.4f);
        //two
        widgetTwo = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1000).setCoordinate(0, -1.0f, 0f, -0).setZView(-2.4f)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());
        widgetTwo.setZView(-2.4f);
        ;

        widgetThree = new BaseThemeExample(totalTime, 2700, 0, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1500).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        widgetThree.setZView(-2.4f);
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());

        //three
        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetFour.setZView(0);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 1f, 0f, 0f)
                .build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1, 0)
                .setIsNoZaxis(true).setZView(0).build());


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
        widgetFour.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 3f : (width == height ? 2.5f : 1.5f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height / 2, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.LEFT_TOP, scaleX);
        //two
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling(width, height / 2, mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), AnimateInfo.ORIENTATION.RIGHT_TOP, scaleX);
        scaleX = width < height ? 4f : (width == height ? 3.5f : 2.5f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height / 2, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.TOP, scaleX);
        //three
        scaleX = width < height ? 2 : (width == height ? 3 : 2);
        widgetFour.init(mimaps.get(3), width, height);

        widgetFour.adjustScaling(width / 2, height, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scaleX);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
        widgetFour.onDestroy();
    }
}
