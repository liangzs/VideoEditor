package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.greeting;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Greeting9PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/greeting9_padding1", "/greeting9_title"},
            {"/greeting9_padding2", "/greeting9_title"},
            {"/greeting9_padding3", "/greeting9_title"},
            {"/greeting9_padding4", "/greeting9_title"},
            {"/greeting9_padding5", "/greeting9_title"},
    };

    private static final String[][] SRCS_1_1 = new String[][]{
            {"/greeting9_padding1_1_1", "/greeting9_title"},
            {"/greeting9_padding2_1_1", "/greeting9_title"},
            {"/greeting9_padding3_1_1", "/greeting9_title"},
            {"/greeting9_padding4_1_1", "/greeting9_title"},
            {"/greeting9_padding5_1_1", "/greeting9_title"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/greeting9_padding1_16_9", "/greeting9_title"},
            {"/greeting9_padding2_16_9", "/greeting9_title"},
            {"/greeting9_padding3_16_9", "/greeting9_title"},
            {"/greeting9_padding4_16_9", "/greeting9_title"},
            {"/greeting9_padding5_16_9", "/greeting9_title"},
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
    protected String[] getSources_1_1(int index) {
        return SRCS_1_1[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }
}
