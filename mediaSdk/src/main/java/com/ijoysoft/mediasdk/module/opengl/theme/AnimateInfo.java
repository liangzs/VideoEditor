package com.ijoysoft.mediasdk.module.opengl.theme;

/**
 * 初始化时候，纹理在哪个位置
 */

public class AnimateInfo {

    public enum ORIENTATION {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM,
        LEFT_BOTTOM,
        LEFT_TOP;
    }

    public enum ROTATION {
        NONE,
        X_ANXIS,
        Y_ANXIS,
        Z_ANXIS,
        X_ANXIS_RE,
        Y_ANXIS_RE,
        Z_ANXIS_RE,
        XY_ANXIS,//按XY轴下左-上右转动
        XY_ANXIS_RE;//按XY上左下右-轴转动
    }

    public enum STAY {
        SPRING_Y,//Y轴弹簧
        ROTATE_Y,//按y轴转动
        ROTATE_X,//按X轴转动
        ROTATE_Z,//按z轴转动
        ROTATE_XY,//按XY轴下左-上右转动
        ROTATE_XY_RE;//按XY上左下右-轴转动
    }

    public enum TYPE {
        ENTER,
        OUT,
        STAY
    }


}

