package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat7;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;

import java.util.ArrayList;
import java.util.List;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Beat7ThemeManager extends BaseTimeThemeManager {


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        ThemeWidgetInfo themeWidgetInfo;

        //背景 四个背景
        themeWidgetInfo = new ThemeWidgetInfo(1000, 500, 1000, 0, 2500);
        themeWidgetInfo.setBitmap(widgetMipmaps.get(0));
        createWidgetTimeExample(themeWidgetInfo);
        themeWidgetInfo = new ThemeWidgetInfo(1000, 500, 1000, 2500, 5000);
        themeWidgetInfo.setBitmap(widgetMipmaps.get(0));
        createWidgetTimeExample(themeWidgetInfo);
        themeWidgetInfo = new ThemeWidgetInfo(1000, 500, 1000, 7500, 10000);
        themeWidgetInfo.setBitmap(widgetMipmaps.get(0));
        createWidgetTimeExample(themeWidgetInfo);

        //控件2个
        themeWidgetInfo = new ThemeWidgetInfo(1000, 1500, 1000, 6000, 9500);
        themeWidgetInfo.setBitmap(widgetMipmaps.get(1));
        createWidgetTimeExample(themeWidgetInfo);
        //1
        themeWidgetInfo = new ThemeWidgetInfo(1000, 1500, 1000, 9000, 12500);
        themeWidgetInfo.setBitmap(widgetMipmaps.get(2));
        createWidgetTimeExample(themeWidgetInfo);
        return true;
    }


    @Override
    protected int computeThemeCycleTime() {
        return 12500;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
        int period = (int) (widgetTimeExample.getWidgetInfo().getTime() / 3);
        float scale = 2;
        switch (index) {
            /*背景*/
            case 0:
                scale = 0.4f;
                widgetTimeExample.setEnterAction(new AnimationBuilder().setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder().setFade(1, 0));
                widgetTimeExample.adjustScaling(-0.7f, 0.2f, scale);
                break;
            case 1:
                scale = 0.4f;
                widgetTimeExample.setEnterAction(new AnimationBuilder().setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder().setFade(1, 0));
                widgetTimeExample.adjustScaling(0.2f, -0.2f, scale);
                break;
            case 2:
                scale = 0.4f;
                widgetTimeExample.setEnterAction(new AnimationBuilder().setFade(0, 1));
                widgetTimeExample.setOutAction(new AnimationBuilder().setFade(1, 0));
                widgetTimeExample.adjustScaling(0.6f, 0.1f, scale);
                break;
            /*文字*/
            case 3:
                scale = 1.5f;
                widgetTimeExample.setEnterAction(new AnimationBuilder(1000).setScale(0.01f, 1).setStartX(0).setStartY(0.8f));
                widgetTimeExample.setOutAction(new AnimationBuilder(1000).setScale(1, 0.001f).setStartX(0).setEndY(0.8f));
                widgetTimeExample.adjustScaling(0, 0.8f, scale);
                break;
            case 4:
                scale = 2;
                widgetTimeExample.setEnterAction(new AnimationBuilder(1000).setCoordinate(1f, 0, 0, 0));
                widgetTimeExample.setOutAction(new AnimationBuilder(1000).setCoordinate(0, 0, -1f, 0));
                widgetTimeExample.adjustScaling(0, 0.8f, scale);
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
                        .setCoordinate(0, 0, -1.5f, 0)
                        .build());
                break;
            case 1:
                list.add(new AnimationBuilder(period, width, height, true).
                        setCoordinate(0.6f, 0, 0, 0).setScale(1.2f, 1.2f).build());
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.2f, 1.4f).build());
                list.add(new AnimationBuilder((int) period, width, height, true)
                        .setRotaCenter(1, 1)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 30).setMoveAction(new EaseInCubic(true, false, false)).build());
                break;
            case 2:
                list.add(new AnimationBuilder(period, width, height, true).setScale(1f, 1.5f).setMoveAction(new EaseInCubic(true, false, false)).build());
                list.add(new AnimationBuilder(period + 100, width, height, true).setConors(
                        AnimateInfo.ROTATION.Z_ANXIS, 0, 5)
                        .setScale(1.5f, 1.4f).build());
                list.add(new AnimationBuilder(period - 100, width, height, true).setConors(
                        AnimateInfo.ROTATION.Z_ANXIS, 0, 90).setMoveAction(new EaseInCubic(true, false, false))
                        .setScale(1.2f, 1.8f).build());
                break;
            case 3:
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.4f, 1.4f)
                        .setRotaCenter(0, 2f).setMoveAction(new EaseOutCubic(true, false, false)).
                                setConors(AnimateInfo.ROTATION.Z_ANXIS, -10, 0).build());
                list.add(new AnimationBuilder(period, width, height, true).setScale(1.4f, 1.2f).build());
                list.add(new AnimationBuilder((int) period, width, height, true).setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0, 0, 0, 1.5f).build());
                break;
            case 4:
                list.add(new AnimationBuilder((int) period, width, height, true).setScale(1.2f, 1.2f)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setCoordinate(0, -0.3f, 0, 0).build());
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
