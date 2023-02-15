package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding8;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.HeartTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class WeddingEightThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.HEART_SHAPE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 4) {
            case 0:
                actionRender = new WedEightThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new WedEightThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new WedEightThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new WedEightThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
        }

        return actionRender;
    }
}
