package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby2;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class BabyTwoThemeManager extends ThemeOpenglManager {


    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画

    @Override
    public TransitionType themeTransition() {
        return TransitionType.GRID_SHOP;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new BabyTwoThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new BabyTwoThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new BabyTwoThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new BabyTwoThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new BabyTwoThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
