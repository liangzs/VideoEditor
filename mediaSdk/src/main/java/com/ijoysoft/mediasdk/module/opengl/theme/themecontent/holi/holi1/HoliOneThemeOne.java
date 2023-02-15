package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 缩小
 * 这里在悬停缩放时，添加滤镜
 */
public class HoliOneThemeOne extends BaseBlurThemeExample {
    private BaseThemeExample widgetOne;


    public HoliOneThemeOne(int totalTime, boolean isNoZaxis, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(2500, false, 1, 0.2f, 1.2f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setScale(1.2f, 1.2f).setIsNoZaxis(true).build();
        this.isNoZaxis = isNoZaxis;
        setZView(0);

        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_ENTER_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setZView(0).
                setFade(0, 1).build());
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        widgetOne.setVertex(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }

}
