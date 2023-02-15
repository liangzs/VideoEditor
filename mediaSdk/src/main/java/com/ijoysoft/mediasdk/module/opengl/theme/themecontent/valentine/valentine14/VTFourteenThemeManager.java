package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine14;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.HeartTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class VTFourteenThemeManager extends ThemeOpenglManager {


    @Override
    public TransitionType themeTransition() {
        return TransitionType.HEART_SHAPE;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {

        switch (index % 5) {
            case 0:
                actionRender = new VTFourteenThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new VTFourteenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new VTFourteenThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new VTFourteenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new VTFourteenThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
