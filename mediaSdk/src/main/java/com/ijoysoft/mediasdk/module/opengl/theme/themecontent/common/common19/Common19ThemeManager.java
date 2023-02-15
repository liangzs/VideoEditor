package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common19;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11ThemeManager;

import java.util.List;

/**
 * @author hayring
 * @date 2022/1/13  16:04
 */
public class Common19ThemeManager extends Common11ThemeManager {

    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        return false;
    }

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
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
        if (imageOriginFilter != null) {
            imageOriginFilter.onSizeChanged(width, height);
            float[] cube;

            if (width < height) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.BOTTOM, 0, 1.2f);
            } else if (height < width) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.BOTTOM, 0, 2.4f);
            } else {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.BOTTOM, 0, 1.4f);
            }

            imageOriginFilter.setVertex(ImageOriginFilter.transitionOpenglPosArray(cube, 0f, -0.1f));
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
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.BOTTOM, 0, 1.2f);
            } else if (height < width) {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.BOTTOM, 0, 2.4f);
            } else {
                cube = ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.BOTTOM, 0, 1.4f);
            }
            imageOriginFilter.setVertex(ImageOriginFilter.transitionOpenglPosArray(cube, 0f, -0.1f));
        }

    }
}
