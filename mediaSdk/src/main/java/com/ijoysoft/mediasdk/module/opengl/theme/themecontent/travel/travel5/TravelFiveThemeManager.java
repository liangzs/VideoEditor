package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel5;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class TravelFiveThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.SQUARE_3D;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new TravelFiveThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new TravelFiveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new TravelFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new TravelFiveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new TravelFiveThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
