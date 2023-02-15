package com.ijoysoft.mediasdk.module.playControl;

import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public interface IRender {
    void onSurfaceCreated();

    void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight);

    default ActionStatus onDrawFrameStatus() {
        return ActionStatus.STAY;
    }

    default void onDrawFrame() {
    }

    default void onDrawFrame(int position) {
    }

    default void onDrawFrame(ActionStatus status, int position) {
    }

    /**
     * 视频渲染的补充
     */
    default void drawVideoFrame(int videoTeture) {

    }

    void onDestroy();

    default void setCurrentMurPts(Long pts) {
    }
    //转场移到最外层，检测是否为存量数据有转场数据
//    default TransitionType themeTransition() {
//        return TransitionType.NONE;
//    }
}
