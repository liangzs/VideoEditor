package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.particle.Resettable;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.List;

/**
 * 时间轴上的Render绘制器容器
 *
 * @author hayring
 * @date 2022/1/7  17:57
 */
public class TimeThemeRenderContainer extends TimeThemeDrawerContainer<IRender>{

    public static TimeThemeRenderContainer createContainer(ThemeWidgetInfo widgetInfo, IRender particleRender) {
        if (particleRender instanceof ProgressFormOutSide) {
            return new TimeThemeProgressRenderContainer(widgetInfo, particleRender);
        } else {
            return new TimeThemeRenderContainer(widgetInfo, particleRender);
        }
    }

    public static TimeThemeRenderContainer createContainer(List<ThemeWidgetInfo> widgetInfos, IRender particleRender) {
        if (particleRender instanceof ProgressFormOutSide) {
            return new TimeThemeProgressRenderContainer(widgetInfos, particleRender);
        } else {
            return new TimeThemeRenderContainer(widgetInfos, particleRender);
        }
    }


    public TimeThemeRenderContainer(List<ThemeWidgetInfo> widgetInfoList, IRender drawer) {
        super(widgetInfoList, drawer);
    }

    public TimeThemeRenderContainer(ThemeWidgetInfo widgetInfo, IRender drawer) {
        super(widgetInfo, drawer);
    }








    @Override
    public void onSurfaceCreated() {
        drawer.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        drawer.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
    }


    @Override
    public void onDestroy() {
        drawer.onDestroy();
    }


    /**
     * 根据时间轴绘制
     *
     * @param position 时间轴位置
     */
    @Override
    public void onDrawFrame(int position) {
        if (!ObjectUtils.isEmpty(widgetInfoList)) {
            if (getRange(position) != null) {
                drawer.onDrawFrame();
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
