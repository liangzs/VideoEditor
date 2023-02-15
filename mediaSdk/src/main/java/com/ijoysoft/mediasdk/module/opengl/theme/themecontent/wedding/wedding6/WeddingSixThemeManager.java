package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding6;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.HeartTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

/**
 * @ author       lfj
 * @ date         2020/10/24
 * @ description
 */
public class WeddingSixThemeManager extends ThemeOpenglManager {

    @Override
    public TransitionType themeTransition() {
        return TransitionType.HEART_SHAPE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 4) {
            case 0:
                actionRender = new WedSixThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new WedSixThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new WedSixThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new WedSixThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
        }

        return actionRender;
    }
}
