package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration8;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class CDEightThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.SQUARE_TWINKLE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new CDEightThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new CDEightThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new CDEightThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new CDEightThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new CDEightThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }

}
