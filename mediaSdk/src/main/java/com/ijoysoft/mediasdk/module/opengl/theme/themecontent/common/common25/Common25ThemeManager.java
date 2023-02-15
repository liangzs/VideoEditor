package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common25;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutElastic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutSine;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager;

/**
 * @author hayring
 * @date 2022/1/18  16:32
 */
public class Common25ThemeManager extends Common15ThemeManager {


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        switch (index) {
            case 0:
                return new ThemeWidgetInfo(1200, 1600, 0, 0, 2800);
            case 1:
                return new ThemeWidgetInfo(1200, 1200, 0, 2800, 5200);
            case 2:
                return new ThemeWidgetInfo(1200, 1200, 0, 5200, 7600);
            case 3:
                return new ThemeWidgetInfo(2400, 1200, 0, 7600, 11200);
            case 4:
                return new ThemeWidgetInfo(1200, 1200, 0, 11200, 13600);
            case 5:
                return new ThemeWidgetInfo(1200, 1200, 0, 13600, 17200);
        }
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {





        if (index == 3) {
            widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                if (width1 < height1) {
                    widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.5f);
                } else {
                    widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2f);
                }
                widgetTimeExample.setEnterAction(new AnimationBuilder(2400)
                        .setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f));

                widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutSine(true, false, false, false));
            });



        } else {
            widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                if (width1 < height1) {
                    widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.5f);
                } else {
                    widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2f);
                }
            });
            widgetTimeExample.setEnterAction(new AnimationBuilder(1200)
                    .setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f));
            widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
            widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutElastic(true, false, false, false));
        }
        widgetTimeExample.onSizeChanged(width, height);


    }

    @Override
    protected int createActions() {
        return 2*super.createActions();
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




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
            imageOriginFilter.setOnSizeChangedListener(((width1, height1) -> {
                if (width < height) {
                    ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.LEFT_TOP, 0, 0.5f);
                } else if (height < width) {
                    ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.LEFT_TOP, 0, 1f);
                } else {
                    ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.LEFT_TOP, 0, 1f);
                }
                imageOriginFilter.setVertex(ImageOriginFilter.transitionRatioPosArray(imageOriginFilter.getCube(), -0.2f, 0.1f));
            }));
            imageOriginFilter.onSizeChanged(width, height);

        }

    }
}
