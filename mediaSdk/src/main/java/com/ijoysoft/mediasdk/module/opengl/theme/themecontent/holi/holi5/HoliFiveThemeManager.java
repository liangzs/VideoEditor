package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi5;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HoliFiveThemeManager extends ThemeOpenglManager {
    private ImageOriginFilter bgImageFilter;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public HoliFiveThemeManager() {
        bgImageFilter = new ImageOriginFilter();
    }

    @Override
    public TransitionType themeTransition() {
        return TransitionType.HAHA_MIRROR;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new HoliFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliFiveThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HoliFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HoliFiveThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HoliFiveThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }

    @Override
    public void initBackgroundTexture() {
        super.initBackgroundTexture();
        bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_five_particle"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        bgImageFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        bgImageFilter.onSizeChanged(width, height);
    }

    @Override
    public void draFrameExtra() {
        super.draFrameExtra();
        bgImageFilter.draw();
    }
}