package com.ijoysoft.mediasdk.module.opengl.theme;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

public class ThemeNoneManager extends ThemeOpenglManager {

    public ThemeNoneManager() {
//        transitionFilter = null;
    }


    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        return new ThemeNoneExample(mediaItem,width,height);
    }


    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
    }
}
