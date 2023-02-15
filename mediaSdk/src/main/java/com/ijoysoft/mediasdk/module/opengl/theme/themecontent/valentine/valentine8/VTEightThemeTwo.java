package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine8;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarSystem;

import java.util.List;

/**
 * stay 放大-缩小-放大
 */
public class VTEightThemeTwo extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private BDSixteenStarDrawer bdSixteenDrawer;

    public VTEightThemeTwo(int totalTime, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, true, 2, 0.2f, 1f);
//        enterAnimation = new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0, 0).
//                setOrientation(AnimateInfo.ORIENTATION.TOP));
//        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.1f, 1.1f).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, 3000, 0, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(3000).setOrientation(AnimateInfo.ORIENTATION.BOTTOM).
                setCoordinate(0, 1.0f, 0f, 0f).setZView(0)));
        widgetOne.setZView(0);

        bdSixteenDrawer = new BDSixteenStarDrawer(new BDSixteenStarSystem(BDSixteenStarSystem.StartType.V_SHADE));
    }


    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        if (getStatus() != ActionStatus.ENTER) {
            bdSixteenDrawer.onDrawFrame();
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 1.6f : (width == height ? 1.6f : 1.0f);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, 0, scale);
        bdSixteenDrawer.init(mimaps.get(1));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        bdSixteenDrawer.onDestroy();
    }
}
