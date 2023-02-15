package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday10;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HDTenThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.HAHA_MIRROR;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new HdTenThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new HdTenThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new HdTenThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new HdTenThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new HdTenThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
