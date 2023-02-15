package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common27;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common10.Common10ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Common27ThemeManager extends Common10ThemeManager {


    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        IParticleRender render = new ParticleBuilder(ParticleType.CENTER_EXPAND, new ArrayList<>(widgetMipmaps.subList(2, 3)))
                .setParticleCount(30)
                .setPointSize(2, 20)
                .setExpanSpeedMulti(2f)
                .setSelfRotate(true)
                .setLoadFirstFrameByGenerator(true)
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(0, 2040)), render));
        //1+2片段
        render = new ParticleBuilder(ParticleType.SCALE_LOOP, new ArrayList<>(widgetMipmaps.subList(0, 2))).setParticleCount(24)
                .setPointSize(3, 10)
                .setLoadAllParticleInFirstFrame(true)
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(2040, 4440)), render));
        return true;
    }


    @Override
    protected void onGifDrawerCreated(MediaItem mediaItem, int index) {
        GifOriginFilter gifOriginFilter = new GifOriginFilter();
        globalizedGifOriginFilters = Collections.singletonList(gifOriginFilter);
        gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
    }

    @Override
    protected void onGifSizeChanged(int index, int width, int height) {
        if (globalizedGifOriginFilters == null) {
            return;
        }
        globalizedGifOriginFilters.get(0).adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2);
    }
}
