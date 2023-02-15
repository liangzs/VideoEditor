package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

public enum ActionBlurType {
    NONE(0),//没有模糊
    MOVE_BLUR(1),//动感模糊
    RATIO_BLUR(2),//径向模糊


    ;
    int type;

    ActionBlurType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
