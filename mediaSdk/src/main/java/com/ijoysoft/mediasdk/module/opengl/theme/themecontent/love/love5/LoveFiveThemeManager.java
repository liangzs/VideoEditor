package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love5;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

/**
 * @ author       lfj
 * @ date         2020/10/20
 * @ description
 */
public class LoveFiveThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.GRID_SHOP;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new LoveFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new LoveFiveThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new LoveFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new LoveFiveThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new LoveFiveThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
