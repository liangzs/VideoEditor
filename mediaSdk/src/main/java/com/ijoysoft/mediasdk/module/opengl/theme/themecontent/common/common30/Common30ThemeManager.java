package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common30;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Common30ThemeManager extends Common15ThemeManager {

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }


    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        //粒子 2400+2400+2400
        IParticleRender render = new ParticleBuilder(ParticleType.SCALE_LOOP, new ArrayList<>(widgetMipmaps.subList(1, 3))).setParticleCount(24)
                .setPointSize(3, 20)
                .setAlpha(true)
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(0, 1600), new ThemeWidgetInfo(3200, 4800)), render));
        render = new ParticleBuilder(ParticleType.EXPAND,
                new ArrayList<>(widgetMipmaps.subList(0, 1))).setParticleCount(30)
                .setPointSize(4, 20)
                .setMinPointSize(2)
                .setExpanSpeedMulti(16)
                .setScale(true)
                .setAlpha(true)
                .setSelfRotate(true)
                .setLoadAllParticleInFirstFrame(true)
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(1600, 3200), new ThemeWidgetInfo(4800, 5400)), render));
        return true;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 5400;
    }


    @Override
    protected void onGifDrawerCreated(MediaItem mediaItem, int index) {
        GifOriginFilter gifOriginFilter1 = new GifOriginFilter();
        gifOriginFilter1.setFrames(mediaItem.getDynamicMitmaps().get(0));
        GifOriginFilter gifOriginFilter2 = new GifOriginFilter();
        gifOriginFilter2.setFrames(mediaItem.getDynamicMitmaps().get(1));
        GifOriginFilter gifOriginFilter3 = new GifOriginFilter();
        gifOriginFilter3.setFrames(mediaItem.getDynamicMitmaps().get(2));

        globalizedGifOriginFilters = Arrays.asList(gifOriginFilter1, gifOriginFilter2, gifOriginFilter3);


    }

    @Override
    protected void onGifSizeChanged(int index, int width, int height) {

        if (globalizedGifOriginFilters == null) {
            return;
        }
        globalizedGifOriginFilters.get(0).adjustScaling(width, height, -0.6f, -0.7f, 2f, 2f);
        globalizedGifOriginFilters.get(1).adjustScaling(width, height, -0.6f, 0.8f, 2f, 2f);
        globalizedGifOriginFilters.get(2).adjustScaling(width, height, 0.2f, 0.8f, 2f, 2f);
    }
}
