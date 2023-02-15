package com.ijoysoft.mediasdk.module.entity;


import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

import java.util.Collections;

public enum RatioType {
    NONE(0, 1, 1, "Original", 0f),
    _1_1(1, 1, 1, "1:1", RatioType.CONST_1_1),
    _4_5(2, 4, 5, "4:5", RatioType.CONST_4_5),
    _16_9(3, 16, 9, "16:9", RatioType.CONST_16_9),
    _9_16(4, 9, 16, "9:16", RatioType.CONST_9_16),
    _3_4(5, 3, 4, "3:4", RatioType.CONST_3_4),
    _4_3(6, 4, 3, "4:3", RatioType.CONST_4_3),
    _2_3(7, 2, 3, "2:3", RatioType.CONST_2_3),
    _3_2(8, 3, 2, "3:2", RatioType.CONST_3_2),
    _2_1(9, 2, 1, "2:1", RatioType.CONST_2_1),
    _1_2(10, 1, 2, "1:2", RatioType.CONST_1_2);
    int key;
    int widthRatio;
    int heightRatio;
    String name;
    float value;

    RatioType(int key, int widthRatio, int heightRatio, String name, float value) {
        this.key = key;
        this.name = name;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        this.value = value;
    }

    public static RatioType getRatioType(int id) {
        for (RatioType ratioType : RatioType.values()) {
            if (ratioType.getKey() == id) {
                return ratioType;
            }
        }
        return _16_9;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public int getHeightRatio() {
        return heightRatio;
    }

    public float getRatioValue() {
        return widthRatio * 1f / heightRatio;
    }

    public boolean isWidthLargeHeight() {
        return widthRatio > heightRatio;
    }

    public boolean isThemeRatio() {
        if (key == 1 || key == 3 || key == 4 || key == 5 || key == 6) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getValue() {
        return value;
    }

    /**
     * 获取宽高比
     *
     * @return 宽高比枚举
     */
    public static RatioType getNotNoneRatioType() {
        if (ConstantMediaSize.ratioType == NONE) {
            return getNotNoneRatioType(ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight);
        } else {
            return ConstantMediaSize.ratioType;
        }
    }

    /**
     * 获取宽高比
     *
     * @return 宽高比枚举
     */
    public static RatioType getNotNoneRatioType(int width, int height) {
        float f = (float)
                width / (float)
                height;
        if (FloatCompare.considerEqual(f, CONST_16_9)) {
            return _16_9;
        } else if (FloatCompare.considerEqual(f, CONST_4_3)) {
            return _4_3;
        } else if (FloatCompare.considerEqual(f, CONST_1_1)) {
            return _1_1;
        } else if (FloatCompare.considerEqual(f, CONST_3_4)) {
            return _3_4;
        } else if (FloatCompare.considerEqual(f, CONST_9_16)) {
            return _9_16;
        } else if (FloatCompare.considerEqual(f, CONST_4_5)) {
            return _4_5;
        } else if (FloatCompare.considerEqual(f, CONST_2_3)) {
            return _2_3;
        } else if (FloatCompare.considerEqual(f, CONST_3_2)) {
            return _3_2;
        } else if (FloatCompare.considerEqual(f, CONST_2_1)) {
            return _2_1;
        } else if (FloatCompare.considerEqual(f, CONST_1_2)) {
            return _1_2;
        } else {
            // throw new IllegalArgumentException("Other ratio are unsupported, width:" + ConstantMediaSize.showViewWidth  + " height:" + ConstantMediaSize.showViewHeight);
            //TODO 如果给外部调用，可抛出异常，内部调用最好不抛出异常，如抛调用时强制消化异常
            return _9_16;
        }
    }

    public static final float CONST_16_9 = 16f / 9f;
    public static final float CONST_4_3 = 4f / 3f;
    public static final float CONST_1_1 = 1f;
    public static final float CONST_3_4 = 3f / 4f;
    public static final float CONST_9_16 = 9f / 16f;
    public static final float CONST_4_5 = 4f / 5f;
    public static final float CONST_2_3 = 2f / 3f;
    public static final float CONST_3_2 = 3f / 2f;
    public static final float CONST_2_1 = 2f;
    public static final float CONST_1_2 = 0.5f;


    public static class FloatCompare {
        private static final float DEFAULT_DELTA = 0.01f; //默认比较精度

        //比较2个float值是否相等（默认精度）
        public static boolean considerEqual(float v1, float v2) {
            return considerEqual(v1, v2, DEFAULT_DELTA);
        }

        //比较2个float值是否相等（指定精度）
        public static boolean considerEqual(float v1, float v2, float delta) {
            return Float.compare(v1, v2) == 0 || considerZero(v1 - v2, delta);
        }

        //判断指定float是否为0（默认精度）
        public static boolean considerZero(float value) {
            return considerZero(value, DEFAULT_DELTA);
        }

        //判断指定float是否为0（指定精度）
        public static boolean considerZero(float value, float delta) {
            return Math.abs(value) <= delta;
        }
    }
}
