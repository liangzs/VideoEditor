package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding10;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.HeartTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class WeddingTenThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.HEART_SHAPE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 4) {
            case 0:
                actionRender = new WedTenThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new WedTenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new WedTenThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new WedTenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
        }

        return actionRender;
    }
}
