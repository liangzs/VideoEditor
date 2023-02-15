package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine10.particle.DBTenDrawerTwo;

import java.util.List;

/**
 * stay 放大-缩小
 */
public class VTTenThemeFive extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private DBTenDrawerTwo particleDrawer;

    public VTTenThemeFive(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.1f, 1f);
        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));

        outAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0f, 0f, 2, 0f).
                setOrientation(AnimateInfo.ORIENTATION.LEFT));

        this.isNoZaxis = isNoZaxis;
        particleDrawer = new DBTenDrawerTwo();
    }

    @Override
    public void initWidget() {
        //widget
        widgetOne = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_OUT_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(-2f, 0.0f, 0f, 0f).setZView(0).setIsNoZaxis(true).build());
        widgetOne.setZView(0);


        //two
        widgetTwo = new BaseThemeExample(totalTime - DEFAULT_OUT_TIME, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).
                setCoordinate(2f, 0f, 0f, 0f).build());
        widgetTwo.setZView(-0.0f);


        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).
                setIsNoZaxis(true).setFade(1, 0).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).
                setIsNoZaxis(true).setIsNoZaxis(true).setFade(1, 0).build());
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float[] pos1 = new float[]{
                -0.9f, 1f,
                -0.9f, 0.7f,
                -0.0f, 1f,
                -0.0f, 0.7f
        };
        float[] pos2 = new float[]{
                0.0f, 1f,
                0.0f, 0.7f,
                0.9f, 1f,
                0.9f, 0.7f,
        };
        if (ConstantMediaSize.ratioType == RatioType._16_9) {
            pos1 = new float[]{
                    -0.4f, 1f,
                    -0.4f, 0.7f,
                    -0.0f, 1f,
                    -0.0f, 0.7f
            };
            pos2 = new float[]{
                    0.0f, 1f,
                    0.0f, 0.7f,
                    0.4f, 1f,
                    0.4f, 0.7f,
            };
        }
        widgetOne.setVertex(pos1);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.setVertex(pos2);
        particleDrawer.init(mimaps.get(2));
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        if (status != ActionStatus.ENTER) {
            particleDrawer.onDrawFrame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        particleDrawer.onDestroy();
    }

}
