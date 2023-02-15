package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common21;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common14.Common14ThemeManager;

import java.util.Collections;

/**
 * @author hayring
 * @date 2022/1/13  14:39
 */
public class Common21ThemeManager extends Common14ThemeManager {


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
        GifOriginFilter gifOriginFilter = globalizedGifOriginFilters.get(0);
        if (width < height) {
            gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 1.6f);
        } else if (height < width) {
            gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2.2f);
        } else {
            gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2.4f);
        }
    }
}
