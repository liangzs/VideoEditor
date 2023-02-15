package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday8;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;

import java.util.List;

public class BDEightThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BDEightStarClusterDrawer starClusterDrawer;

    public BDEightThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -2f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0.0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 180, 0).
                setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0, 2.5f).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 0).build();
        setZView(-2.5f);
        starClusterDrawer = new BDEightStarClusterDrawer(new BDEightStarSystem());
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.5f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(-1f, 0f, 0f, 0f)
                .build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 2f, 0f)
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
        float scaleX = width < height ? 1.5f : (width == height ? 2 : 2);
        float centerX = width < height ? -0.6f : (width == height ? -0.75f : -0.75f);
        float centerY = width < height ? 0.8f : (width == height ? 0.7f : 0.7f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

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
