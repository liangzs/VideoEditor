package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel10;

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
public class TravelTenThemeManager extends ThemeOpenglManager {

    @Override
    public TransitionType themeTransition() {
        return TransitionType.SQUARE_3D;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new TravelTenThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new TravelTenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new TravelTenThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new TravelTenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new TravelTenThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
