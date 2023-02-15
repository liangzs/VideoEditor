package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

/**
 * 进场动画
 * 进场动画和出场动画都包括四边，死角八个方向移动，然后x，z，y正反六个方向转动，所以一共是48个组合
 */
public class MoveAnimation {
    private long duration;
    private float coordX;
    private float coordY;
    private float conor;
    private AnimateInfo.ROTATION rotationType;
    private AnimateInfo.ORIENTATION orientationType;
    private AnimateInfo.TYPE actionType;
    public static int COORD_NORMAL = 2;


    public MoveAnimation() {

    }

    public MoveAnimation(AnimationBuilder builder) {
        this.duration = builder.duration;
        this.conor = builder.conor;
        this.rotationType = builder.rotationType;
        if (actionType != null) {
            switch (actionType) {
                case OUT:
                    COORD_NORMAL = 3;
                    break;
                case ENTER:
                    COORD_NORMAL = 2;
                    break;
                case STAY:
                    break;
            }
        }
        if (orientationType != null) {
            switch (orientationType) {
                case TOP:
                    coordX = 0;
                    coordY = -1 * COORD_NORMAL;
                    break;
                case LEFT:
                    coordX = -1 * COORD_NORMAL;
                    coordY = 0;
                    break;
                case BOTTOM:
                    coordX = 0;
                    coordY = COORD_NORMAL;
                    break;
                case RIGHT:
                    coordX = COORD_NORMAL;
                    coordY = 0;
                    break;
                case LEFT_TOP:
                    coordX = -1 * COORD_NORMAL;
                    coordY = -1 * COORD_NORMAL;
                    break;
                case LEFT_BOTTOM:
                    coordX = -1 * COORD_NORMAL;
                    coordY = COORD_NORMAL;
                    break;
                case RIGHT_BOTTOM:
                    coordX = COORD_NORMAL;
                    coordY = COORD_NORMAL;
                    break;
                case RIGHT_TOP:
                    coordX = COORD_NORMAL;
                    coordY = -1 * COORD_NORMAL;
                    break;
            }
        }

        if (rotationType != null) {
            switch (rotationType) {

            }
        }
    }

}
