package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday7;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ThemeWedStayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday7.particle.DBSevenDrawerTwo;

import java.util.List;

/**
 * 上进，上下悬停，左划出
 */
public class BDSevenThemeTwo extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private DBSevenDrawerTwo starClusterDrawer;

    public BDSevenThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new ThemeWedStayAnimation(3000, -2f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.0f, 0, 0).
                setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2.5f, 0).setZView(-2.5f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 180).build();
        setZView(-2.5f);
        starClusterDrawer = new DBSevenDrawerTwo();
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setZView(-2.5f);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setCoordinate(2, 0f, 0f, 0f)
                .build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 2f, 0)
                .setZView(-2.5f).build());

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2f, 0f, 0f).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.5f).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, -2.5f, 0f).setZView(-2).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setZView(-2.5f).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
        widgetTwo.drawFrame();
        if (getStatus() == ActionStatus.STAY) {
            starClusterDrawer.onDrawFrame();
        }
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        float scaleX = width < height ? 3 : (width == height ? 3 : 3);
        float centerX = width < height ? 0.8f : (width == height ? 0.8f : 0.8f);
        float centerY = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        scaleX = width < height ? 3f : (width == height ? 4 : 3);
        centerX = 0;
        centerY = width < height ? 0.82f : (width == height ? 0.8f : 0.79f);
        widgetTwo.init(mimaps.get(1), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);

        starClusterDrawer.init(mimaps.get(2));
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
        widgetTwo.onDestroy();
        starClusterDrawer.onDestroy();
    }
}
