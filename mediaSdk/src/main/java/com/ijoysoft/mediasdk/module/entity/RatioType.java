package com.ijoysoft.mediasdk.module.entity;

public enum RatioType {
    NONE(0), _1_1(1), _16_9(2), _9_16(3), _4_3(4), _3_4(5);
    int key;

    RatioType(int key) {
        this.key = key;
    }

    public static RatioType getRatioType(int id) {
        switch (id) {
            case 0:
                return NONE;
            case 1:
                return _1_1;
            case 2:
                return _16_9;
            case 3:
                return _9_16;
            case 4:
                return _4_3;
            case 5:
                return _3_4;
        }
        return NONE;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
