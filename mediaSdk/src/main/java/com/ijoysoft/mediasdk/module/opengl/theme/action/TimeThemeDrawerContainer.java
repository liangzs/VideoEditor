package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.Collections;
import java.util.List;

/**
 * AFilter 或 Render 容器
 * @author hayring
 * @date 2022/1/17  16:11
 */
public abstract class TimeThemeDrawerContainer<T> {


    protected TimeThemeDrawerContainer(ThemeWidgetInfo widgetInfo, T drawer) {
        this.widgetInfoList = Collections.singletonList(widgetInfo);
        this.drawer = drawer;
    }


    protected TimeThemeDrawerContainer(List<ThemeWidgetInfo> widgetInfoList, T drawer) {
        this.widgetInfoList = widgetInfoList;
        this.drawer = drawer;
    }


    /**
     * 时间轴信息
     */
    protected List<ThemeWidgetInfo> widgetInfoList;

    /**
     * AFilter 或 Render
     */
    protected T drawer;


    public void setDrawer(T drawer) {
        this.drawer = drawer;
    }

    public abstract void onSurfaceCreated();

    public abstract void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight);


    public abstract void onDestroy();

    public abstract void onDrawFrame(int position);




    protected ThemeWidgetInfo getRange(int position) {
        for (ThemeWidgetInfo themeWidgetInfo : widgetInfoList) {
            if (themeWidgetInfo.isInRange(position)) {
                return themeWidgetInfo;
            }
        }
        return null;
    }


    public T getDrawer() {
        return drawer;
    }
}
