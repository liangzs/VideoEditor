package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday2;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HDTwoThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.FLASH_WHITE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new HDTwoThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HDTwoThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HDTwoThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HDTwoThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HDTwoThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }

        return actionRender;
    }
}
