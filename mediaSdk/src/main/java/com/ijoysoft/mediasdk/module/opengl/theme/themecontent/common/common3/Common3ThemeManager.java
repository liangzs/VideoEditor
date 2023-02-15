package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common3;

import android.annotation.SuppressLint;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.SameActionOpenglThemeManager;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用3
 *
 * @author hayring
 * @date 2021/12/14  16:31
 */
public class Common3ThemeManager extends SameActionOpenglThemeManager {

    public Common3ThemeManager() {
        super(Common3Action.class);
    }


    List<GifDecoder.GifFrame> singleGIF = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (gifOriginFilter == null) {
            gifOriginFilter = new GifOriginFilter();
            gifOriginFilter.onCreate();
            if (singleGIF.isEmpty()) {
                singleGIF.addAll(mediaItem.getDynamicMitmaps().get(0));
            }
            gifOriginFilter.setFrames(singleGIF);
            if (width < height) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2.2f);
            } else if (height < width) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.88f);
            }
        }
    }


    GifOriginFilter gifOriginFilter = null;


    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        if (gifOriginFilter != null) {
            gifOriginFilter.draw();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gifOriginFilter != null) {
            gifOriginFilter.onDestroy();
            for (GifDecoder.GifFrame frame : singleGIF) {
                frame.image.recycle();
            }
            singleGIF.clear();
            gifOriginFilter = null;
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        if (gifOriginFilter != null) {
            if (width < height) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2.2f);
            } else if (height < width) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.88f);
            }
        }
    }
}
