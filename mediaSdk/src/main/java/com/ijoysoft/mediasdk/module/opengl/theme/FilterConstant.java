package com.ijoysoft.mediasdk.module.opengl.theme;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FilterConstant {
    public static final int NONE = -1;
    public static final int DAILY = 0;
    public static final int PEOPLE = 1;
    public static final int COLORFUL = 2;
    public static final int DOWNLOAD = 3;
    public static final int SKY = 4;

    public static final int BW = 5;
    public static final int ART = 6;
    public static final int MOVIE = 7;

    public static final int FOOD = 8;

    public static final int NATURAL = 9;
    public static final int POPULAR = 90;

    @IntDef({DAILY, PEOPLE, COLORFUL, DOWNLOAD, SKY, BW, ART, MOVIE, FOOD, NATURAL, POPULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FilterType {
    }
}
