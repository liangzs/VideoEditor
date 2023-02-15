package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.divali.divali1;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;

import java.util.ArrayList;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Divali1ThemeManager extends BaseTimeThemeManager {

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        switch (index) {
            case 0:
                return new ThemeWidgetInfo(1000, 1000, 1000, 0, 3000);
            case 1:
                return new ThemeWidgetInfo(1000, 1000, 1000, 3000, 6000);
        }
        return new ThemeWidgetInfo(0, 0, 0, 0, 0);
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
        float scale = 2;
        switch (index) {
            case 0:
                widgetTimeExample.setEnterAction(new AnimationBuilder().setFade(0, 1f));
                widgetTimeExample.setOutAction(new AnimationBuilder().setFade(0, 1f));
                widgetTimeExample.adjustScaling(0, 0.8f, scale);
                break;
            case 1:
                widgetTimeExample.setEnterAction(new AnimationBuilder().setFade(0, 1f));
                widgetTimeExample.setOutAction(new AnimationBuilder().setFade(0, 1f));
                widgetTimeExample.adjustScaling(0, 0.8f, scale);
                break;
        }

    }

    @Override
    protected boolean blurBackground() {
        return false;
    }

    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        int period = (int) (duration / 3);
        switch (index % createActions()) {
            case 0:
                list.add(new AnimationBuilder(period, width, height, true).
                        setScale(2f, 1f).setMoveBlurRange(3, 0).setMoveAction(new EaseOutCubic(false, true, false)).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).setScale(1f, 1.5f).build());
                list.add(new AnimationBuilder(period - 100, width, height, true).
                        setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0, 0, 1.5f, 0.5f)
                        .setMoveBlurRange(0, 3).build());
                break;
            case 1:
                list.add(new AnimationBuilder(period, width, height, true).
                        setCoordinate(-0.6f, -0.2f, 0, 0).setScale(1.2f, 1.2f).build());
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.2f, 1.4f).build());
                list.add(new AnimationBuilder(period, width, height, true).setConors(
                        AnimateInfo.ROTATION.Z_ANXIS, 0, 90).
                        setMoveAction(new EaseInCubic(true, false, false))
                        .setScale(1.2f, 1.8f).setMoveBlurRange(0, 10).build());
                break;
            case 2:
                list.add(new AnimationBuilder(period, width, height, true).setScale(1f, 1.5f)
                        .setMoveAction(new EaseInCubic(true, false, false)).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).setConors(
                        AnimateInfo.ROTATION.Z_ANXIS, 0, 5)
                        .setScale(1.5f, 1.4f).build());
                list.add(new AnimationBuilder((int) period - 100, width, height, true)
                        .setRotaCenter(-1, 1).setMoveBlurRange(0, 4).
                                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 30)
                        .setMoveAction(new EaseInCubic(true, false, false)).build());
                break;
            case 3:
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.4f, 1.4f)
                        .setRotaCenter(0, 2f).setMoveBlurRange(10, 0)
                        .setMoveAction(new EaseOutCubic(true, false, false)).
                                setConors(AnimateInfo.ROTATION.Z_ANXIS, -10, 0).build());
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.4f, 1.2f).build());
                list.add(new AnimationBuilder((int) period, width, height, true)
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0, 0, 1.5f, 0).build());
                break;
            case 4:
                list.add(new AnimationBuilder((int) period, width, height, true).setScale(1.2f, 1.2f)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setCoordinate(-1.5f, 0, 0, 0).build());
                list.add(new AnimationBuilder(period - 100, width, height, true).setScale(1.2f, 1.4f).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).
                        setScale(1.0f, 2.8f).setMoveBlurRange(0, 3).build());
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 5;
    }


}
