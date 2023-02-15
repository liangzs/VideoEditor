package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.greeting;


import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

import java.util.ArrayList;
import java.util.List;

public class Greeting7PreTreatment extends DownloadedPreTreatmentTemplate {





    @Override
    protected String[][] getSources() {
        return SRCS;
    }

    /**
     * Opengl转场时间
     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
     */
    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }


    private static final String[] SRC = new String[]{"/greeting7_padding", "/greeting7_title1", "/greeting7_title2"};


    private static final String[] SRC_16_9 = new String[]{"/greeting7_padding_16_9", "/greeting7_title1", "/greeting7_title2"};


    private static final String[] SRC_1_1 = new String[]{"/greeting7_padding_1_1", "/greeting7_title1", "/greeting7_title2"};


    private static final String[][] SRCS = new String[][]{SRC, SRC, SRC, SRC, SRC};


    @Override
    protected String[] getSource(int index) {
        return SRC;
    }

    @Override
    protected String[] getSources_1_1(int index) {
        return SRC_1_1;
    }

    @Override
    protected String[] getSources_16_9(int index) {
        return SRC_16_9;
    }

    @Override
    public int getMipmapsCount() {
        return 5;
    }
}
