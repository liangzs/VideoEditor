package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine1;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 纹理切换时，采用glsl的转场方案，适用于纹理铺满显示，即纹理坐标等同方案
 */
public class VTOneThemeManager extends ThemeOpenglManager {

    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 5) {
            case 0:
                actionRender = new VTOneThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new VTOneThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new VTOneThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new VTOneThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new VTOneThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }


}
