package com.ijoysoft.mediasdk.module.entity;

/**
 * 转场类型
 */
public enum TransationType {
    MOVE_LEFT(0,"左移"), MOVE_RIGHT(2,"右移"), MOVE_TOP(3,"上移"),MOVE_BOTTOM(3,"下移")
    ,FADE_IN(4,"淡入"),FADE_OUT(5,"淡出");


    private int id;
    private String detail;

    TransationType(int id, String detail) {
        this.id = id;
        this.detail = detail;
    }
}
