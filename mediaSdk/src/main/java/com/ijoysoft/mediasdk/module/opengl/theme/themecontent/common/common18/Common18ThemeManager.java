package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common18;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common14.Common14ThemeManager;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hayring
 * @date 2022/1/13  14:39
 */
public class Common18ThemeManager extends Common14ThemeManager {


    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
        if (imageOriginFilter != null) {
            imageOriginFilter.draw();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageOriginFilter != null) {
            imageOriginFilter.onDestroy();
            imageOriginFilter = null;
        }
    }




    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        if (imageOriginFilter != null) {
            imageOriginFilter.onSizeChanged(width, height);
            float[] cube;

            if (width < height) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0, 2f);
            } else if (height < width) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0, 3f);
            } else {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0, 3f);
            }

            imageOriginFilter.setVertex(ImageOriginFilter.transitionRatioPosArray(cube, -0.35f, -0.5f));
        }
    }



    ImageOriginFilter imageOriginFilter = null;

    /**
     * 素材高度
     */
    int bitmapWidth;

    /**
     * 素材宽度
     */
    int bitmapHeight;


    List<GifDecoder.GifFrame> singleGIF = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (imageOriginFilter == null && index == 0) {
            imageOriginFilter = new ImageOriginFilter();
            imageOriginFilter.create();
            Bitmap bitmap = mediaItem.getMimapBitmaps().get(0);
            ImageOriginFilter.init(imageOriginFilter, bitmap, width, height);
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();
            float[] cube;

            if (width < height) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0, 2f);
            } else if (height < width) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0, 3f);
            } else {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0, 3f);
            }
            imageOriginFilter.setVertex(ImageOriginFilter.transitionRatioPosArray(cube, -0.35f, -0.5f));
        }

    }



    @Override
    protected void onGifDrawerCreated(MediaItem mediaItem, int index) {
        GifOriginFilter gifOriginFilter = new GifOriginFilter();
        globalizedGifOriginFilters = Collections.singletonList(gifOriginFilter);
        gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
    }


    @Override
    protected void onGifSizeChanged(int index, int width, int height) {
        super.onGifSizeChanged(index, width, height);
        if (globalizedGifOriginFilters == null || index != 0) {
            return;
        }
        GifOriginFilter gifOriginFilter = globalizedGifOriginFilters.get(0);
        float[] cube;
        if (width < height) {
            cube = gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 1.6f);
        } else if (height < width) {
            cube = gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2.2f);
        } else {
            cube = gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2.4f);
        }
        //手动修复位置
        gifOriginFilter.setVertex(ImageOriginFilter.transitionRatioPosArray(cube, 0.2f, 0.3f));
    }
}
