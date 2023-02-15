package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common32;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common14.Common14ThemeManager;

import java.util.Arrays;
import java.util.List;

public class Common32ThemeManager extends Common14ThemeManager {
    //片段切换循环使用
    private GifOriginFilter gifOriginFilter;

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
    }


    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        //粒子 2400+2400+2400
        IParticleRender render = new ParticleBuilder(ParticleType.HOVER, widgetMipmaps)
                .setParticleCount(20)
                .setPointSize(3, 30)
                .setScale(true)
                .setColor(true, new Common32ParticleColor())
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(4000, 10040)), render));
        return true;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 10040;
    }

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {

        if (ObjectUtils.isEmpty(mediaItem.getDynamicMitmaps())) {
            return;
        }
        index = index % 6;
        if (gifOriginFilter == null) {
            gifOriginFilter = new GifOriginFilter();
            gifOriginFilter.onCreate();
        } else {
            gifOriginFilter.onDestroy();
            gifOriginFilter.resetData();
            gifOriginFilter.onCreate();
        }
        gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
        super.drawPrepare(mediaDrawer, mediaItem, index);
    }

    @Override
    protected void onGifSizeChanged(int index, int width, int height) {
        if (gifOriginFilter == null) {
            return;
        }
        switch (index % 6) {
            case 0:
            case 1:
                gifOriginFilter.adjustScaling(width, height, 0f, 0, 1.1f, 1.1f);
                break;
            case 2:
            case 4:
                gifOriginFilter.adjustScaling(width, height, -0.5f, 0.6f, 1.8f, 1.8f);
                break;
            case 3:
            case 5:
                gifOriginFilter.adjustScaling(width, height, 0.6f, -0.7f, 2.5f, 2.5f);
                break;
        }
    }

    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
        if (gifOriginFilter != null) {
            gifOriginFilter.draw();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gifOriginFilter != null) {
            gifOriginFilter.onDestroy();
            gifOriginFilter = null;
        }
    }
}
