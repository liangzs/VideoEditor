package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;

import java.util.List;

/**
 * <p>
 * //TODO 换成控件方案
 */
public class VTThreeThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public VTThreeThemeThree(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
//        stayAction = new AnimationBuilder(3000).
//                setCoordinate(0f, -0.1f, 0f, 0).setZView(-2.5f).build();
//        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
//                setCoordinate(2f, 0f, 0, -0.1f).setZView(-2.5f));
        stayAction = new StayScaleAnimation(3000, true, 1, 0.1f, 1.0f);
        stayAction.setZView(-3f);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(2.5f, 0f, 0, -0.0f));
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, 0f, 2.5f).build();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 2 : (width == height ? 2 : 1.5f);
        widgetOne.adjustScaling(width, height / 2, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scale);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1.5f, 0f, 0f).setZView(-2.5f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(0, 0f, 0f, 1.5f).build());
        widgetOne.setZView(-2.5f);
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
