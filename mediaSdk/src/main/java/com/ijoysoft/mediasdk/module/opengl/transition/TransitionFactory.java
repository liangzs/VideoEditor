package com.ijoysoft.mediasdk.module.opengl.transition;

import static com.ijoysoft.mediasdk.module.opengl.transition.TransitionType.MOVE_LEFT;
import static com.ijoysoft.mediasdk.module.opengl.transition.TransitionType.MOVE_RIGHT;
import static com.ijoysoft.mediasdk.module.opengl.transition.TransitionType.NONE;

public class TransitionFactory {
    private static TransitionType transitionType = NONE;

    public static TransitionFilter initFilters(TransitionType type) {
        if (type == null) {
            return null;
        }
        transitionType = type;
        switch (type) {
            case MOVE_LEFT:
                return new MoveTransitionFilter(MOVE_LEFT, MoveTransitionFilter.MoveFilter.LEFT);
            case MOVE_RIGHT:
                return new MoveTransitionFilter(MOVE_RIGHT, MoveTransitionFilter.MoveFilter.RIGHT);
            case MOVE_TOP:
                return new MoveTransitionFilter(TransitionType.MOVE_TOP, MoveTransitionFilter.MoveFilter.TOP);
            case MOVE_BOTTOM:
                return new MoveTransitionFilter(TransitionType.MOVE_BOTTOM, MoveTransitionFilter.MoveFilter.BOTTOM);
            case MOVE_LEFT_BOTTOM:
                return new MoveTransitionFilter(TransitionType.MOVE_LEFT_BOTTOM, MoveTransitionFilter.MoveFilter.LEFT_BOTTOM);
            case MOVE_LEFT_TOP:
                return new MoveTransitionFilter(TransitionType.MOVE_LEFT_TOP, MoveTransitionFilter.MoveFilter.LEFT_TOP);
            case MOVE_RIGHT_BOTTOM:
                return new MoveTransitionFilter(TransitionType.MOVE_RIGHT_BOTTOM, MoveTransitionFilter.MoveFilter.RIGHT_BOTTOM);
            case MOVE_RIGHT_TOP:
                return new MoveTransitionFilter(TransitionType.MOVE_RIGHT_TOP, MoveTransitionFilter.MoveFilter.RIGHT_TOP);
            case SHADE_LANDSCAPE:
                return new WindowShadeHZTransitionFilter(TransitionType.SHADE_LANDSCAPE);
            case SHADE_PORTRAIT:
                return new WindowShadeVTTransitionFilter(TransitionType.SHADE_PORTRAIT);
            case FADE_IN:
                return new FadeTransitionFilter(TransitionType.FADE_IN);
            case FADE_OUT:
                return new FadeTransitionFilter(TransitionType.FADE_OUT);
            case SQUARE_TWINKLE:
                return new SquareTwinkleTransitionFilter(TransitionType.SQUARE_TWINKLE);
            case HAHA_MIRROR:
                return new HaHaMirrorTransitionFilter(TransitionType.HAHA_MIRROR);
            case CIRCLE_ROTATE:
                return new CircleRotateTransitionFilter(TransitionType.CIRCLE_ROTATE);
            case CIRCLE_ZOOM:
                return new CircleZoomTransitionFilter(TransitionType.CIRCLE_ZOOM);
            case OVAL_ZOOM:
                return new OvalZoomTransitionFilter(TransitionType.OVAL_ZOOM);
            case WHOLE_ZOOM:
                return new WholeZoomTransitionFilter(TransitionType.WHOLE_ZOOM);
            case WIPE_LEFT:
                return new WipeTransitionFilter(TransitionType.WIPE_LEFT);
            case BLUR:
                return new BlurTransitionFilter(TransitionType.BLUR);
            case READ_BOOK:
                return new ReadBookTransitionFilter(TransitionType.READ_BOOK);
            case FLASH_WHITE:
                return new FlashColorTransitionFilter(TransitionType.FLASH_WHITE);
            case FLASH_BLACK:
                return new FlashColorTransitionFilter(TransitionType.FLASH_BLACK);
            case SHAKE:
                return new ShakeTransitionFilter(TransitionType.SHAKE);
            case GRID_SHOP:
                return new GridShopTransitionFilter(TransitionType.GRID_SHOP);
            case NONE:
                return new TransitionFilter();
            default:
                return null;
        }
    }
}
