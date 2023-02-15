package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HDSixThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.CIRCLE_ROTATE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new HDSixThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new HDSixThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new HDSixThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new HDSixThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new HDSixThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
