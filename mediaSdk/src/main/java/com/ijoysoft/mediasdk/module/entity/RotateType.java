package com.ijoysoft.mediasdk.module.entity;

public enum RotateType {
    ROT_0(0), ROT_90(90), ROT_180(180), ROT_270(270);
    private int angle;

    RotateType(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static RotateType getRotate(int angle) {
        switch (angle) {
            case 0:
                return ROT_0;
            case 90:
                return ROT_90;
            case 180:
                return ROT_180;
            case 270:
                return ROT_270;
            default:
                return ROT_0;
        }
    }
}
