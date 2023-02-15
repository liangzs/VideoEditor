package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common9;

import android.annotation.SuppressLint;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6.Common6ThemeManager;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.ArrayList;

/**
 * 通用9
 * @author hayring
 * @date 2022/1/12
 */
public class Common9ThemeManager extends Common6ThemeManager {



    ArrayList<GifDecoder.GifFrame>[] gifs = null;

    @SuppressLint("ResourceType")
    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (gifOriginFilters == null) {
            gifOriginFilters = new GifOriginFilter[]{new GifOriginFilter(), new GifOriginFilter()};
            gifOriginFilters[0].onCreate();
            gifOriginFilters[1].onCreate();
            if (gifs == null) {
                gifs = new ArrayList[]{new ArrayList<GifDecoder.GifFrame>(), new ArrayList<GifDecoder.GifFrame>()};
                gifs[0].addAll(mediaItem.getDynamicMitmaps().get(0));
                gifs[1].addAll(mediaItem.getDynamicMitmaps().get(1));
            }
            gifOriginFilters[0].setFrames(gifs[0]);
            gifOriginFilters[1].setFrames(gifs[1]);
            if (width < height) {
                gifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f);
                gifOriginFilters[1].adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 1f);
            } else if (height < width) {
                gifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
                gifOriginFilters[1].adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 1f);
            } else {
                gifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
                gifOriginFilters[1].adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2f);
            }
        }
    }


   GifOriginFilter[] gifOriginFilters = null;


    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
        if (gifOriginFilters != null) {
            gifOriginFilters[0].draw();
            gifOriginFilters[1].draw();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gifOriginFilters != null) {
            gifOriginFilters[0].onDestroy();
            gifOriginFilters[1].onDestroy();
            for (GifDecoder.GifFrame frame : gifs[0]) {
                frame.image.recycle();
            }
            for (GifDecoder.GifFrame frame : gifs[1]) {
                frame.image.recycle();
            }
            gifs[0].clear();
            gifs[1].clear();
            gifs = null;
            gifOriginFilters = null;
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        if (gifOriginFilters != null) {
            if (width < height) {
                gifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f);
                gifOriginFilters[1].adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 1f);
            } else if (height < width) {
                gifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
                gifOriginFilters[1].adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 1f);
            } else {
                gifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
                gifOriginFilters[1].adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2f);
            }
        }
    }



    @Override
    public boolean isTextureInside() {
        return true;
    }
}
