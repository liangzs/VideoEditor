package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love16;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneThemeManager.BD_ONE_ZVIEW;

/**
 * 左y轴滚进，右下z翻转出
 */
public class LoveSixteenThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;

    public LoveSixteenThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height, BD_ONE_ZVIEW);

        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 360).setZView(BD_ONE_ZVIEW).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 2f).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 270).setZView(BD_ONE_ZVIEW).setWidthHeight(width, height).build();
        setZView(BD_ONE_ZVIEW);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(-1, 0, 0, 0).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setCoordinate(0, 0, -1, 0).
                setConors(AnimateInfo.ROTATION.NONE, 0, 0).setIsWidget(true).build());
        widgetOne.setZView(-2);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float centerX = -0.5f;
        float centerY = 0.6f;
        float scale = width < height ? 6 : (width == height ? 8 : 6);
        widgetOne.adjustScaling(width, height, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scale, scale);
        //two
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
