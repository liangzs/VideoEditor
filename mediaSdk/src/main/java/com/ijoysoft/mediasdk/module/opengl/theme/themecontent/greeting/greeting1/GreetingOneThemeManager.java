package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting1;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class GreetingOneThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.GRID_SHOP;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 4) {
            case 0:
                actionRender = new GreetOneThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new GreetOneThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new GreetOneThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new GreetOneThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
