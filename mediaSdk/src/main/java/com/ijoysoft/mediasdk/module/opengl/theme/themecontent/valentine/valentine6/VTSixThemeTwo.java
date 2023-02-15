package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class VTSixThemeTwo extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;

    public VTSixThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 3, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.TOP));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2f, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
        this.isNoZaxis = isNoZaxis;

    }


    @Override
    public void drawLast() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2).setCoordinate(-1, 0f, 0f, 0f)
                .build());

        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setCoordinate(1, 0f, 0f, 0f)
                .build());

        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setFade(1, 0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setFade(1, 0).build());

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标|Z    +-362*/1E`  
        float centerX = width < height ? -0.5f : -0.55f;
        float centerY = 0.55f;
        float scale = width < height ? 4 : (width == height ? 4 : 3);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        widgetOne.setStayAction(new StayShakeAnimation(2500, width, height, -0.65f, 0.8f, 6, -2f));

        widgetTwo.init(mimaps.get(1), width, height);
        //顶点坐标
        centerX = width < height ? 0.5f : 0.55f;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        widgetTwo.setStayAction(new StayShakeAnimation(2500, width, height, 0.65f, 0.8f, 6, -2f));
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
    }

}
