package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine2;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

/**
 * 纹理切换时，采用glsl的转场方案，适用于纹理铺满显示，即纹理坐标等同方案
 */
public class VTTwoThemeManager extends ThemeOpenglManager {


    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public VTTwoThemeManager() {
    }

    @Override
    public TransitionType themeTransition() {
        return TransitionType.WHOLE_ZOOM;
    }

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new VTTwoThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new VTTwoThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new VTTwoThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new VTTwoThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new VTTwoThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
