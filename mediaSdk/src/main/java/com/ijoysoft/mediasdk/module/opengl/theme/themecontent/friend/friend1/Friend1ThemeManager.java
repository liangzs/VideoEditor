package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend1;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.SameActionOpenglThemeManager;

/**
 * @author hayring
 * @date 2021/12/14  16:31
 */
public class Friend1ThemeManager extends SameActionOpenglThemeManager {

    public Friend1ThemeManager() {
        super(Friend1Action.class);
    }

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (index == 0) {
            gifOriginFilter = new GifOriginFilter();
            gifOriginFilter.onCreate();
            gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
            if (width < height) {
                if (RatioType.getNotNoneRatioType() == RatioType._9_16) {
                    gifOriginFilter.adjustScaling(width, height, 0f, 0.92f, 2.2f, 2.2f);
                } else {
                    gifOriginFilter.adjustScaling(width, height, 0f, 1.08f, 2.2f, 2.2f);
                }
            } else if (height < width) {
                gifOriginFilter.adjustScaling(width, height, 0f, 1.02f, 3.2f, 3.2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, 0f, 0.92f, 3.88f, 3.88f);
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
            gifOriginFilter = null;
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        if (gifOriginFilter != null) {
            if (width < height) {
                if (ConstantMediaSize.ratioType == RatioType._9_16) {
                    gifOriginFilter.adjustScaling(width, height, 0f, 0.92f, 2.2f, 2.2f);
                } else {
                    gifOriginFilter.adjustScaling(width, height, 0f, 1.08f, 2.2f, 2.2f);
                }
            } else if (height < width) {
                gifOriginFilter.adjustScaling(width, height, 0f, 1.02f, 3.2f, 3.2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, 0f, 0.92f, 3.88f, 3.88f);
            }
        }

    }
}
