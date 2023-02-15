package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common29;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6.Common6ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/30  19:27
 */
public class Common29ThemeManager extends Common6ThemeManager {

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
        switch (index) {
            case 0:
//                widgetTimeExample.onSizeChanged(width, height);
                break;
        }
    }

    //3400+7200;
    @Override
    protected int computeThemeCycleTime() {
        return 10600;
    }

    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {

        //控件，因为这个控件时全局控件，直接在这里写了
        WidgetTimeExample widgetTimeExample = createWidgetTimeExample(new ThemeWidgetInfo(0, computeThemeCycleTime(), widgetMipmaps.get(1)));
        widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
            if (width1 < height1) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0, 1f);
            } else {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 2f);
            }
        });
        return true;
    }


    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
//        widgetRenders.get(0).onSizeChanged(screenWidth, screenHeight);
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
        if (width <= height) {
            globalizedGifOriginFilters.get(0).adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0, 1f);
        } else {
            globalizedGifOriginFilters.get(0).adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0, 2f);
        }
    }


}
