package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love14;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class LoveFourteenThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.WHOLE_ZOOM;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 7) {
            case 0:
                actionRender = new LoveFourteenThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new LoveFourteenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new LoveFourteenThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new LoveFourteenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new LoveFourteenThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
            case 5:
                actionRender = new LoveFourteenThemeSix((int) mediaItem.getDuration(), true, width, height);
                break;
            case 6:
                actionRender = new LoveFourteenThemeSeven((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
    }


}
