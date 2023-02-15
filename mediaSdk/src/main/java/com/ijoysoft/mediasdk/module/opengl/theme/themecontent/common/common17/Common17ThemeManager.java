package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common17;

import android.annotation.SuppressLint;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.ArrayList;

/**
 * 通用9
 * @author hayring
 * @date 2022/1/12
 */
public class Common17ThemeManager extends Common8ThemeManager {



    ArrayList<GifDecoder.GifFrame> gif = null;

    @SuppressLint("ResourceType")
    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (gif == null) {
            gifOriginFilter = new GifOriginFilter();
            gifOriginFilter.onCreate();
            if (gif == null) {
                gif = new ArrayList<GifDecoder.GifFrame>();
                gif.addAll(mediaItem.getDynamicMitmaps().get(0));
            }
            gifOriginFilter.setFrames(gif);
            if (width < height) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
            } else if (height < width) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 4f);
            }
        }
    }


   GifOriginFilter gifOriginFilter = null;




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
            for (GifDecoder.GifFrame frame : gif) {
                frame.image.recycle();
            }
            gif.clear();
            gif = null;
            gifOriginFilter = null;
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        if (gifOriginFilter != null) {
            if (width < height) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
            } else if (height < width) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 4f);
            }
        }
    }
}
