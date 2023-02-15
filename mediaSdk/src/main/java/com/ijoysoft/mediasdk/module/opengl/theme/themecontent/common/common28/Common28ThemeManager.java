package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common28;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Common28ThemeManager extends Common11ThemeManager {

    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        if (ObjectUtils.isEmpty(widgetMipmaps) || widgetMipmaps.size() < 6) {
            return false;
        }
        //1+2片段
        IParticleRender render = new ParticleBuilder(ParticleType.SCALE_LOOP, new ArrayList<>(widgetMipmaps.subList(0, 1))).setParticleCount(24)
                .setPointSize(3, 10)
                .setLoadAllParticleInFirstFrame(true)
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(0, 2000), new ThemeWidgetInfo(4000, 6000)), render));
        //3+4片段
        render = new ParticleBuilder(ParticleType.HOVER, new ArrayList<>(widgetMipmaps.subList(1, 6))).setParticleCount(15)
                .setPointSize(3, 25)
                .setSelfRotate(true)
                .setScale(true)
                .setAlpha(true)
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(2000, 4000), new ThemeWidgetInfo(6000, 7800)), render));
        return true;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 7800;
    }


    @Override
    protected void onGifDrawerCreated(MediaItem mediaItem, int index) {
        GifOriginFilter gifOriginFilter = new GifOriginFilter();
        globalizedGifOriginFilters = Collections.singletonList(gifOriginFilter);
        gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
    }


    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
        if (globalizedGifOriginFilters == null) {
            return;
        }
        GifOriginFilter gifOriginFilter = globalizedGifOriginFilters.get(0);
        if (width < height) {
            //改变位置，再画一次
            gifOriginFilter.adjustScalingFitX(width, height, AnimateInfo.ORIENTATION.TOP);
            gifOriginFilter.draw();
            //重新改变位置
            gifOriginFilter.adjustScalingFitX(width, height, AnimateInfo.ORIENTATION.BOTTOM);
        }

    }


    @Override
    protected void onGifSizeChanged(int index, int width, int height) {

        if (globalizedGifOriginFilters == null) {
            return;
        }
        globalizedGifOriginFilters.get(0).adjustScalingFitX(width, height, AnimateInfo.ORIENTATION.BOTTOM);
    }
}
