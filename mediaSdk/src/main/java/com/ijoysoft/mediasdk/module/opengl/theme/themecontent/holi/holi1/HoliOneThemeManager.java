package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi1;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HoliOneThemeManager extends ThemeOpenglManager {
    @Override
    public TransitionType themeTransition() {
        return TransitionType.CIRCLE_ROTATE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        index = 0;
        switch (index % 7) {
            case 0:
                actionRender = new HoliOneThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliOneThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HoliOneThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HoliOneThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HoliOneThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
            case 5:
                actionRender = new HoliOneThemeSix((int) mediaItem.getDuration(), true, width, height);
                break;
            case 6:
                actionRender = new HoliOneThemeSeven((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
