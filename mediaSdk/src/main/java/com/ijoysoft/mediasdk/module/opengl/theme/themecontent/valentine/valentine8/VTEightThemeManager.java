package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine8;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.HaHaMirrorTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class VTEightThemeManager extends ThemeOpenglManager {
    private ImageOriginFilter bgImageFilter;

    public VTEightThemeManager() {
        bgImageFilter = new ImageOriginFilter();
    }

    @Override
    public TransitionType themeTransition() {
        return TransitionType.HAHA_MIRROR;
    }

    public void onDestroy() {
        super.onDestroy();
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
    }


    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new VTEightThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new VTEightThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new VTEightThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new VTEightThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new VTEightThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }

    @Override
    public void ratioChange() {
        super.ratioChange();
        switch (ConstantMediaSize.ratioType) {
            case _4_3:
            case _16_9:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eight_theme_bg169" + ConstantMediaSize.SUFFIX));
                break;
            case _3_4:
            case _9_16:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eight_theme_bg" + ConstantMediaSize.SUFFIX));
                break;
            case _1_1:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eight_theme_bg11" + ConstantMediaSize.SUFFIX));
                break;
            default:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_eight_theme_bg" + ConstantMediaSize.SUFFIX));
                break;
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
    public void onDrawFrame() {
        super.onDrawFrame();
        bgImageFilter.draw();
    }


}
