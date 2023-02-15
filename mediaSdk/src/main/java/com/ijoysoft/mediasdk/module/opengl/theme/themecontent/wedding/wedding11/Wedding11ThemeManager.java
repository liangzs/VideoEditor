package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding11;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.SameActionOpenglThemeManager;

/**
 * @author hayring
 * @date 2021/12/14  16:31
 */
public class Wedding11ThemeManager extends SameActionOpenglThemeManager {

    public Wedding11ThemeManager() {
        super(Wedding11Action.class);
    }


    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (index == 0) {
            gifOriginFilter = new GifOriginFilter();
            gifOriginFilter.onCreate();
            gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
            gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f);
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
            gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f);
        }
    }
}
