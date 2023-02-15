package com.ijoysoft.mediasdk.view;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SELF和IMAGE时候，参数是模糊值
 * NONE和color的时候，参数是颜色值
 */
public class BackgroundType {
    public static final int NONE = 0;
    public static final int SELF = 1;
    public static final int COLOR = 2;
    public static final int IMAGE = 3;

    @IntDef({NONE, SELF, COLOR, IMAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }
}
