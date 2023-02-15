package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.particle.Resettable;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.List;

/**
 * 包含设置progress的Render container
 * @author hayring
 * @date 2022/1/8  15:25
 */
public class TimeThemeProgressRenderContainer extends TimeThemeRenderContainer {

    protected TimeThemeProgressRenderContainer(ThemeWidgetInfo widgetInfo, IRender particleRender) {
        super(widgetInfo, particleRender);
        if (!(particleRender instanceof ProgressFormOutSide)) {
            throw new IllegalArgumentException("particleRender is not ProgressFormOutSide!");
        }
    }

    public TimeThemeProgressRenderContainer(List<ThemeWidgetInfo> widgetInfoList, IRender particleRender) {
        super(widgetInfoList, particleRender);
    }

    /**
     * 根据时间轴绘制
     *
     * @param position 时间轴位置
     */
    @Override
    public void onDrawFrame(int position) {
        if (!ObjectUtils.isEmpty(widgetInfoList)) {
            ThemeWidgetInfo range = null;
            if ((range = getRange(position)) != null) {
                ((ProgressFormOutSide) drawer).setProgress((float)position/(float)(range.getEndTime()-range.getStartTime()));
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
