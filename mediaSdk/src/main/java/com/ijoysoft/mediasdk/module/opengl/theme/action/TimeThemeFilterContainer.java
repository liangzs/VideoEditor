package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.Resettable;

import java.util.List;

/**
 * @author hayring
 * @date 2022/1/17  16:20
 */
public class TimeThemeFilterContainer extends TimeThemeDrawerContainer<AFilter> {

    public TimeThemeFilterContainer(ThemeWidgetInfo widgetInfo, AFilter drawer) {
        super(widgetInfo, drawer);
    }

    public TimeThemeFilterContainer(List<ThemeWidgetInfo> widgetInfoList, AFilter drawer) {
        super(widgetInfoList, drawer);
    }



    @Override
    public void onSurfaceCreated() {
        drawer.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        drawer.onSizeChanged(width, height);
    }

    @Override
    public void onDestroy() {
        drawer.onDestroy();
    }

    @Override
    public void onDrawFrame(int position) {
        if (!ObjectUtils.isEmpty(widgetInfoList)) {
            if (getRange(position) != null) {
                drawer.draw();
            } else {
                if (drawer instanceof Resettable && widgetInfoList.size() == 1) {
                    Resettable render = (Resettable) drawer;
                    if (render.isResettable()) {
                        render.reset();
                    }
                }
            }
        }
    }
}
