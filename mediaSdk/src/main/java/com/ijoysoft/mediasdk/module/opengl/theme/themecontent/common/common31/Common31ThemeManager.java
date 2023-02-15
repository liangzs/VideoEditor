package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common31;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common5.Common5ThemeManagerTemplate;

import java.util.Collections;
import java.util.List;

public class Common31ThemeManager extends Common5ThemeManagerTemplate {

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
//        switch (index) {
//            case 0:
//                widgetTimeExample.adjustScalingFixX(AnimateInfo.ORIENTATION.TOP);
//                break;
//        }
    }


    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        //控件，因为这个控件时全局控件，直接在这里写了
//        createWidgetTimeExample(new ThemeWidgetInfo(0, computeThemeCycleTime(), widgetMipmaps.get(0)));
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
        float[] cube = globalizedGifOriginFilters.get(0).adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0, 2f);
        globalizedGifOriginFilters.get(0).setVertex(ImageOriginFilter.transitionRatioPosArray(cube, 0, 0.25f));
    }
}
