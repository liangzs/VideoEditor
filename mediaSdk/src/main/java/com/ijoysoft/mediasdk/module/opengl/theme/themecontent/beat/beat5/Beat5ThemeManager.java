package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat5;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class Beat5ThemeManager extends ThemeOpenglManager {

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public Beat5ThemeManager() {
    }

    @Override
    public TransitionType themeTransition() {
        return TransitionType.SQUARE_TWINKLE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 6) {
            case 0:
                actionRender = new Beat5ThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new Beat5ThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new Beat5ThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new Beat5ThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new Beat5ThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
            case 5:
                actionRender = new Beat5ThemeSix((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }

    @Override
    public void initBackgroundTexture() {
        super.initBackgroundTexture();
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
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
    }

    @Override
    public void draFrameExtra() {
        super.draFrameExtra();
    }
}
