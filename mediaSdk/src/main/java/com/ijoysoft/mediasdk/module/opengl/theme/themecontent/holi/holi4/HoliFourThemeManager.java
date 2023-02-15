package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi4;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeGifManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

public class HoliFourThemeManager extends ThemeGifManager {
    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public HoliFourThemeManager() {
        super(-1);
//        super(R.raw.gif_transition1);
    }


    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 7) {
            case 0:
                actionRender = new HoliFourThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliFourThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HoliFourThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HoliFourThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HoliFourThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
            case 5:
                actionRender = new HoliFourThemeSix((int) mediaItem.getDuration(), true, width, height);
                break;
            case 6:
                actionRender = new HoliFourThemeSeven((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }
}
