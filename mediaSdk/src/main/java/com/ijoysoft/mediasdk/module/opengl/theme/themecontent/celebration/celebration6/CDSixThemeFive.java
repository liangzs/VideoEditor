package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

/**
 * 左下角z轴翻滚进，右y轴翻转出
 */
public class CDSixThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;

    public CDSixThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 90).build();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 1700, 0, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1.0f, 0f, -0.1f).setEnterDelay(500).setZView(-2.4f)));
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, -0.1f, 0f, -0.1f).setFade(1, 0).setZView(-2.4f).build());
        widgetOne.setStayAction(new AnimationBuilder(3000).setCoordinate(0, -0.1f, 0f, -0.1f).setZView(-2.4f).build());
        widgetOne.setZView(-2.4f);
        //two
        widgetTwo = new BaseThemeExample(totalTime, 2200, 0, DEFAULT_OUT_TIME);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(DEFAULT_ENTER_TIME).setOrientation(AnimateInfo.ORIENTATION.TOP).
                setCoordinate(0, -1.0f, 0f, 0f).setEnterDelay(1000).setZView(-2.4f)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setFade(1, 0).setZView(-2.4f).build());
        widgetTwo.setZView(-2.4f);

        //three
        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME, true);
        widgetThree.setZView(0);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).setCoordinate(0, 1f, 0f, 0f)
                .build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1, 0)
                .setIsNoZaxis(true).setZView(0).build());


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
        float scaleX = width < height ? 3f : (width == height ? 2.5f : 1.5f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height / 2, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.LEFT_TOP, scaleX);
        //two
        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height / 2, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.RIGHT_TOP, scaleX);
        //three
        scaleX = 3f;
        widgetThree.init(mimaps.get(1), width, height);
        widgetThree.adjustScaling(width, height / 2, mimaps.get(1).getWidth(),
                mimaps.get(1).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scaleX);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}