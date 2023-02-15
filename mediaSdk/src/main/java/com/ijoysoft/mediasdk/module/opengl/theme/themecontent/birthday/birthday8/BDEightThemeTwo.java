package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday8;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 * 天使画星星诅咒你
 */
public class BDEightThemeTwo extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BDEightStarClusterDrawer starClusterDrawer;

    public BDEightThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -2f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.0f, 0, 0).
                setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2.5f, 0).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 180).build();
        setZView(-2.5f);
        starClusterDrawer = new BDEightStarClusterDrawer(new BDEightStarSystemOne());
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.5f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(0, 1f, 0f, 0f)
                .build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 1)
                .setZView(-2.5f).build());


    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        if (getStatus() == ActionStatus.STAY) {
            starClusterDrawer.onDrawFrame();
        }
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 2 : (width == height ? 1.5f : 1.2f);
        widgetOne.adjustScaling((int) (width / 2f), (int) (height / 4.5), mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scale);
        widgetOne.setStayAction(new StayShakeAnimation(3000, width, height, 0f, 1f, 5, -2.5f));
        starClusterDrawer.init(mimaps.get(1));
    }

    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
//        Matrix.translateM(mViewMatrix, 0, 0f, -0.4f, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        starClusterDrawer.onDestroy();
    }
}
