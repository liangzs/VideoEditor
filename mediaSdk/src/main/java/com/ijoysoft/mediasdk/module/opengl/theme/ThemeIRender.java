package com.ijoysoft.mediasdk.module.opengl.theme;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.List;

public interface ThemeIRender extends IRender {

    void initBackgroundTexture();

    /**
     * 传入mediaDrawer获取前一帧数据
     *
     * @param mediaDrawer
     * @param mediaItem
     * @param index
     */
    void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index);

    void setActionRender(IAction actionRender);

    IAction getNextAction(MediaItem mediaItem, int index);

    IAction getAction();

    void draFrameExtra();


    void previewAfilter(MediaItem mediaItem);


    void previewMediaIem();

    void setPreAfilter(List<MediaItem> list, int index);

    void ratioChange();

    void seekTo(int currentDuration);

    void onDestroyFragment();

    boolean checkSupportTransition();

    default TransitionType themeTransition() {
        return TransitionType.NONE;
    }

    default void onFinish() {

    }

    default public ActionStatus getActionStatus() {
        return ActionStatus.STAY;
    }


}
