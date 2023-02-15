package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi2;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HoliTwoThemeManager extends ThemeOpenglManager {
    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public HoliTwoThemeManager() {
    }

    @Override
    public TransitionType themeTransition() {
        return TransitionType.WIPE_LEFT;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 7) {
            case 0:
                actionRender = new HoliTwoThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliTwoThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HoliTwoThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HoliTwoThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HoliTwoThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
            case 5:
                actionRender = new HoliTwoThemeSix((int) mediaItem.getDuration(), true, width, height);
                break;
            case 6:
                actionRender = new HoliTwoThemeSeven((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
