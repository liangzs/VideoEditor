package com.ijoysoft.mediasdk.module.opengl.transition;

public enum TransitionType {
    NONE(0),
    MOVE_LEFT(2),
    MOVE_RIGHT(3),
    MOVE_TOP(4),
    MOVE_BOTTOM(5),
    MOVE_LEFT_TOP(21),
    MOVE_LEFT_BOTTOM(22),
    MOVE_RIGHT_TOP(23),
    MOVE_RIGHT_BOTTOM(24),
    FADE_IN(1),
    FADE_OUT(25),
    SHADE_PORTRAIT(7),
    SHADE_LANDSCAPE(6),
    SQUARE_TWINKLE(8),
    HAHA_MIRROR(9),
    CIRCLE_ROTATE(10),
    CIRCLE_ZOOM(11),
    OVAL_ZOOM(12),
    WHOLE_ZOOM(13),
    WIPE_LEFT(14),
    BLUR(15),
    READ_BOOK(16),
    FLASH_WHITE(17),
    FLASH_BLACK(18),
    SHAKE(19),
    GRID_SHOP(20);
    
    int index;
    TransitionType(int index){
        this.index =index;
    }
    
    public int getIndex() {
        return index;
    }
}

