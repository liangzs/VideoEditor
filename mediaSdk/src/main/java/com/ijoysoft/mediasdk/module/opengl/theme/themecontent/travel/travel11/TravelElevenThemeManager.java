package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel11;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

/**
 * @ author       lfj
 * @ date         2020/10/29
 * @ description
 */
public class TravelElevenThemeManager extends ThemeOpenglManager {

    @Override
    public TransitionType themeTransition() {
        return TransitionType.HAHA_MIRROR;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new TravelElevenThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new TravelElevenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new TravelElevenThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new TravelElevenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new TravelElevenThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}