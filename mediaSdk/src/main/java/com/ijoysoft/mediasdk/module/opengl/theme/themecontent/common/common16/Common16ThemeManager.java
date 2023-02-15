package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common16;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutBounce;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common5.Common5ThemeManagerTemplate;


public class Common16ThemeManager extends Common5ThemeManagerTemplate {


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        switch (index) {
            case 0:
                return new ThemeWidgetInfo(1200, 3600, 0, 0, 4800);
            case 1:
                return new ThemeWidgetInfo(1200, 3600, 0, 0, 4800);
            case 2:
                return new ThemeWidgetInfo(1200, 2900, 0, 4800, 8900);
        }
        return null;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 8900 ;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
//        float scale = 0.7f;
//        int period = (int) (widgetTimeExample.getWidgetInfo().getTime() / 3);
        switch (index) {
            case 0: {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2f);
                widgetTimeExample.setEnterAction(new AnimationBuilder(1200)
                        .setCoordinate(Float.MAX_VALUE, 0f, 0f, 0f));
            }break;
            case 1: {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.5f);
                widgetTimeExample.setEnterAction(new AnimationBuilder(1200)
                        .setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f));
            }break;
            case 2: {
                WidgetTimeExample target = widgetTimeExample;
                widgetTimeExample.setEnterAction(new AnimationBuilder(1200)
                        .setCoordinate(0f, Float.MIN_VALUE, 0f, 0f));
                widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                    if (width1 < height1) {
                        target.adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
                    } else {
                        target.adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0f, 3f);
                    }
                    target.fixEnterOutAnimationStartEndAtScreenEdge();
                    target.getEnterAnimation().setMoveAction(new EaseOutBounce(true, false, false, false));
                });
                widgetTimeExample.onSizeChanged(width, height);

            }return;
        }
        widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
        widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutBounce(true, false, false, false));

    }





 }
