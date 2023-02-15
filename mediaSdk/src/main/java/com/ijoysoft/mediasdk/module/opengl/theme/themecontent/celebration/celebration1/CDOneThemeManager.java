package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration1;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class CDOneThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.CIRCLE_ROTATE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new CDOneThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new CDOneThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new CDOneThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new CDOneThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new CDOneThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
