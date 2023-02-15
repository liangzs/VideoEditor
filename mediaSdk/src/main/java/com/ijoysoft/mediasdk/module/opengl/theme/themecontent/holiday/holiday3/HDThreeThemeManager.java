package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday3;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HDThreeThemeManager extends ThemeOpenglManager {




    @Override
    public TransitionType themeTransition() {
        return TransitionType.SQUARE_3D;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 4) {
            case 0:
                actionRender = new HDThreeThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new HDThreeThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new HDThreeThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new HDThreeThemeFour((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
