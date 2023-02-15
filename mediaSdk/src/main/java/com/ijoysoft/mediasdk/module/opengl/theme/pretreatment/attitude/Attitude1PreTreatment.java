package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

import java.util.List;

/**
 * 态度1
 */
public class Attitude1PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/attitude1_background_gradient", "/attitude1_blue_gradient", "/attitude1_title1"},
            {"/attitude1_background_gradient", "/attitude1_yellow_gradient", "/attitude1_title2"},
            {"/attitude1_background_gradient", "/attitude1_pink_gradient", "/attitude1_spectrum"},
            {"/attitude1_background_gradient", "/attitude1_cyan_gradient", "/attitude1_glass"},
            {"/attitude1_background_gradient", "/attitude1_yellow_gradient", "/attitude1_title5"},
            {"/attitude1_background_gradient", "/attitude1_cyan_gradient", "/attitude1_title6"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/attitude1_background_gradient_16_9", "/attitude1_blue_gradient", "/attitude1_title1"},
            {"/attitude1_background_gradient_16_9", "/attitude1_yellow_gradient", "/attitude1_title2"},
            {"/attitude1_background_gradient_16_9", "/attitude1_pink_gradient", "/attitude1_spectrum_16_9"},
            {"/attitude1_background_gradient_16_9", "/attitude1_cyan_gradient", "/attitude1_glass"},
            {"/attitude1_background_gradient_16_9", "/attitude1_yellow_gradient", "/attitude1_title5"},
            {"/attitude1_background_gradient_16_9", "/attitude1_cyan_gradient", "/attitude1_title6"},
    };


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

    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }
}
