package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.Resettable;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemePagParticleDrawerProxy;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.List;

/**
 * 时间轴上的Render绘制器容器
 *
 * @author hayring
 * @date 2022/1/7  17:57
 */
public class TimeThemePagParticleContainer extends TimeThemeRenderContainer {


    public TimeThemePagParticleContainer(List<ThemeWidgetInfo> widgetInfoList, PAGNoBgParticle drawer, ThemePagParticleDrawerProxy proxy) {
        super(widgetInfoList, drawer);
        this.proxy = proxy;
    }

    public TimeThemePagParticleContainer(ThemeWidgetInfo widgetInfo, PAGNoBgParticle drawer, ThemePagParticleDrawerProxy proxy) {
        super(widgetInfo, drawer);
        this.proxy = proxy;
    }

    private ThemePagParticleDrawerProxy proxy;

    @Override
    public void onSurfaceCreated() {
        proxy.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        proxy.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
    }


    @Override
    public void onDestroy() {
        proxy.onDestroy();
    }


    /**
     * 根据时间轴绘制
     *
     * @param position 时间轴位置
     */
    public void onDrawFrame(int position, int textureId) {
        if (!ObjectUtils.isEmpty(widgetInfoList)) {
            ThemeWidgetInfo info = getRange(position);
            if (info != null && info.isInRange(position)) {
                if (proxy.getPagParticle() != drawer) {
                    proxy.switchParticle((PAGNoBgParticle) drawer);
                }
                proxy.drawPagParticle(textureId);
            }
        }
    }

}
