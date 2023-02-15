package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * stay 缩小
 */
public class VTTenThemeThree extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;

    private BaseThemeExample widgetTwo;// 气球
    private BaseThemeExample widgetThree;

    public VTTenThemeThree(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.1f, 1.1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(2f, 0, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.RIGHT).setScale(1.1f, 1.1f));
        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0f, 0f, 0f, 2f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM));
        this.isNoZaxis = isNoZaxis;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).
                setCoordinate(2, 0f, 0f, 0f).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1, 0).build());
        widgetOne.setZView(-0.0f);


        widgetThree = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        widgetThree.setZView(-2f);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2).setCoordinate(0, 1f, 0f, 0f)
                .build());
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setCoordinate(0, 1f, 0f, 0f)
                .build());

        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setFade(1, 0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2f).setFade(1, 0).build());

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
        float[] pos = new float[]{
                -1.0f, -0.8f,
                -1.0f, -1.0f,
                1.0f, -0.8f,
                1.0f, -1.0f
        };
        if (ConstantMediaSize.ratioType == RatioType._16_9) {
            pos = new float[]{
                    -1.0f, -0.7f,
                    -1.0f, -1.0f,
                    1.0f, -0.7f,
                    1.0f, -1.0f
            };
        }
        widgetOne.setVertex(pos);

        widgetThree.init(mimaps.get(1), width, height);
        //顶点坐标|Z    +-362*/1E`
        float centerX = -0.55f;
        float centerY = 0.75f;
        float scale = width < height ? 6 : (width == height ? 5 : 4);
        widgetThree.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        widgetThree.setStayAction(new StayShakeAnimation(2500, width, height, -0.55f, 1f, 6, -2f));
        widgetTwo.init(mimaps.get(1), width, height);
        //顶点坐标
        centerX = 0.55f;
        centerY = 0.75f;
        widgetTwo.adjustScaling(width, height, mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scale, scale);
        widgetTwo.setStayAction(new StayShakeAnimation(2500, width, height, 0.55f, 1f, 6, -2f));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }

}
