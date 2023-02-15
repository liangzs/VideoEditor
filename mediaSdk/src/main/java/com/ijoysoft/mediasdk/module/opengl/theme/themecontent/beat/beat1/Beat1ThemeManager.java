package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat1;

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
public class Beat1ThemeManager extends BaseTimeThemeManager {

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        switch (index) {
            case 0:
                return new ThemeWidgetInfo(2000, 2000, 2000, 0, 6000);
            case 1:
                return new ThemeWidgetInfo(1000, 1000, 1000, 3000, 6000);
            case 2:
                return new ThemeWidgetInfo(1000, 1000, 1000, 6000, 9000);
            case 3:
                return new ThemeWidgetInfo(1000, 1000, 1000, 9000, 12000);
            case 4:
                return new ThemeWidgetInfo(1000, 1000, 1000, 12000, 15000);
        }
        return null;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 15000;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
        float scale = 0.7f;
        int period = (int) (widgetTimeExample.getWidgetInfo().getTime() / 3);
        switch (index) {
            case 0:
                widgetTimeExample.setEnterAction(new AnimationBuilder().setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder().setFade(1, 0));
                widgetTimeExample.adjustScaling(0, 0.85f, scale);
                break;
            case 1:
                widgetTimeExample.setEnterAction(new AnimationBuilder(period).setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder(period).setFade(1, 0));
                widgetTimeExample.adjustScaling(0, 0.85f, scale);
                break;
            case 2:
                widgetTimeExample.setEnterAction(new AnimationBuilder(period).setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder(period).setFade(1, 0));
                widgetTimeExample.adjustScaling(0, 0.85f, scale);
                break;
            case 3:
                scale = 1.2f;
                widgetTimeExample.setEnterAction(new AnimationBuilder(period).setScale(0.01f, 1f).setStartX(0).setStartY(0.8f));
                widgetTimeExample.setOutAction(new AnimationBuilder(period).setFade(1, 0));
                widgetTimeExample.adjustScaling(0, 0.8f, scale);
                break;
            case 4:
                widgetTimeExample.setEnterAction(new AnimationBuilder(period).setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder(period).setFade(1, 0));
                widgetTimeExample.adjustScaling(0, 0.85f, scale);
                break;
        }

    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        int period = (int) (duration / 3);
        switch (index % createActions()) {
            case 0:
                list.add(new AnimationBuilder(period, width, height, true).setMoveBlurRange(20, 10).
                        setScale(2f, 1f).setMoveAction(new EaseOutCubic(false, true, false)).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).setScale(1f, 1.5f).build());
                list.add(new AnimationBuilder(period - 100, width, height, true).setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0, 0, 1.5f, 0.5f)
                        .build());
                break;
            case 1:
                list.add(new AnimationBuilder(period, width, height, true).
                        setCoordinate(-0.6f, -0.2f, 0, 0).setScale(1.2f, 1.2f).build());
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.2f, 1.4f).build());
                list.add(new AnimationBuilder(period, width, height, true).setConors(
                        AnimateInfo.ROTATION.Z_ANXIS, 0, 90).setMoveAction(new EaseInCubic(true, false, false))
                        .setScale(1.2f, 1.8f).build());
                break;
            case 2:
                list.add(new AnimationBuilder(period, width, height, true).setScale(1f, 1.5f).setMoveAction(new EaseInCubic(true, false, false)).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).setConors(
                        AnimateInfo.ROTATION.Z_ANXIS, 0, 5)
                        .setScale(1.5f, 1.4f).build());
                list.add(new AnimationBuilder((int) period - 100, width, height, true)
                        .setRotaCenter(-1, 1)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 30).setMoveAction(new EaseInCubic(true, false, false)).build());
                break;
            case 3:
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.4f, 1.4f)
                        .setRotaCenter(0, 2f).setMoveAction(new EaseOutCubic(true, false, false)).
                                setConors(AnimateInfo.ROTATION.Z_ANXIS, -10, 0).build());
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.4f, 1.2f).build());
                list.add(new AnimationBuilder((int) period, width, height, true).setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0, 0, 1.5f, 0).build());
                break;
            case 4:
                list.add(new AnimationBuilder((int) period, width, height, true).setScale(1.2f, 1.2f)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setCoordinate(-1.5f, 0, 0, 0).build());
                list.add(new AnimationBuilder(period - 100, width, height, true).setScale(1.2f, 1.4f).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).
                        setScale(1.0f, 2.8f).build());
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 5;
    }


    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
    }
}
