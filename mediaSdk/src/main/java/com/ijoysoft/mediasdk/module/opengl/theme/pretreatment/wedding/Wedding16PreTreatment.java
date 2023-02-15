package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding;


import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Wedding16PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/wedding16_gradient_9_16", "/wedding16_twins_9_16", "/wedding16_pink_gradient"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/wedding16_gradient_16_9", "/wedding16_twins_16_9", "/wedding16_pink_gradient"},
    };

    private static final String[][] SRCS_1_1 = new String[][]{
            {"/wedding16_gradient_1_1", "/wedding16_twins_9_16", "/wedding16_pink_gradient"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }

    @Override
    protected String[] getSources_1_1(int index) {
        return SRCS_1_1[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }

    /**
     * Opengl转场时间
     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
     */
    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }





}
