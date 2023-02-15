package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

public class BDSixThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public BDSixThemeOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setZView(-2.5f).build();
    }

    @Override
    public void drawFrame() {
        super.drawFrame();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 2700, 0, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1.0f, 0f, 0f).setEnterDelay(1500).setZView(-2.4f)));

        //two
        widgetTwo = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(1000).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));
        //three
        widgetThree = new BaseThemeExample(totalTime, 1700, 0, DEFAULT_OUT_TIME);
        widgetThree.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setEnterDelay(500).setCoordinate(0, -1.0f, 0f, 0f).setZView(-2.4f)));

        widgetOne.setZView(-2.4f);
        widgetTwo.setZView(-2.4f);
        widgetThree.setZView(-2.4f);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
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
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
