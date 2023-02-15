package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.greeting;


import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

import java.util.ArrayList;
import java.util.List;

public class Greeting8PreTreatment extends DownloadedPreTreatmentTemplate {





    @Override
    protected String[][] getSources() {
        return SRC;
    }

    /**
     * Opengl转场时间
     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
     */
    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }


    private static final String[][] SRC = new String[][]{{"/greeting8_foreground", "/greeting8_title"}};


    private static final String[][] SRC_16_9 = new String[][]{{"/greeting8_foreground_16_9", "/greeting8_title"}};

    private static final String[][] SRC_1_1 = new String[][]{{"/greeting8_foreground_1_1", "/greeting8_title"}};


    @Override
    public int getMipmapsCount() {
        return 1;
    }

    @Override
    protected String[] getSource(int index) {
        return SRC[index];
    }

    @Override
    protected String[] getSources_1_1(int index) {
        return SRC_1_1[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        return SRC_16_9[index];
    }
}
