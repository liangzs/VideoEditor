package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi7;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HoliSevenThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.BLUR;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
//        index = 1;
        switch (index % 5) {
            case 0:
                actionRender = new HoliSevenThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliSevenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HoliSevenThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HoliSevenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HoliSevenThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }

}
