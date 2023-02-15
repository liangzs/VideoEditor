package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左上角z轴翻滚进，右下角z轴滚出
 */
public class BDFourThemeFour extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private BaseThemeExample widgetFour;

    public BDFourThemeFour(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, -2f, 0, 0).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 2f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 180).setWidthHeight(width, height).build();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0, 0, -2f).build());

        //two
        widgetTwo = new BaseThemeExample(totalTime, 1700, 0, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(500).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0f, 0, -2f).build());
        //three
        widgetThree = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1000).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.4f).setCoordinate(0, 0f, 0, -2f).build());

        widgetOne.setZView(-2.4f);
        widgetTwo.setZView(-2.4f);
        widgetThree.setZView(-2.4f);

        widgetFour = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetFour.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2).build());
        widgetFour.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2.5f, 0f).setZView(-2).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).build());
        widgetFour.setZView(-2.4f);

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
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling((int) (width / 2f), (int) (height / 4f), mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.LEFT_TOP, 2);
        //two
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling((int) (width / 2f), (int) (height / 4f), mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.TOP, 2);
        //three
        widgetThree.init(mimaps.get(2), width, height);
        widgetThree.adjustScaling((int) (width / 2f), (int) (height / 4f), mimaps.get(2).getWidth(), mimaps.get(2).getHeight(), AnimateInfo.ORIENTATION.RIGHT_TOP, 2);

        float scaleX = width < height ? 1.5f : (width == height ? 1.8f : 3);
        float centerX = 0;
        float centerY = width < height ? 0.73f : (width == height ? 0.72f : 0.8f);
        widgetFour.init(mimaps.get(3), width, height);
        widgetFour.adjustScaling(width, height,
                mimaps.get(3).getWidth(), mimaps.get(3).getHeight(), centerX, centerY, scaleX, scaleX);
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
