package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine9;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.HeartTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class VTNineThemeManager extends ThemeOpenglManager {

    @Override
    public TransitionType themeTransition() {
        return TransitionType.HEART_SHAPE;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new VTNineThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new VTNineThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new VTNineThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new VTNineThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new VTNineThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
