package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.greeting;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Greeting6PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/greet6_padding1", "/greet6_text1", "/greet6_tableware"},
            {"/greet6_padding2", "/greet6_text4", "/greet6_cloud"},
            {"/greet6_padding3", "/greet6_text1", "/greet6_heart"},
            {"/greet6_padding4", "/greet6_text4", "/greet6_cheer"},
            {"/greet6_padding5", "/greet6_text1", "/greet6_hearts"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/greet6_padding1_16_9", "/greet6_text1", "/greet6_tableware"},
            {"/greet6_padding2_16_9", "/greet6_text4", "/greet6_cloud"},
            {"/greet6_padding3_16_9", "/greet6_text1", "/greet6_heart"},
            {"/greet6_padding4_16_9", "/greet6_text4", "/greet6_cheer"},
            {"/greet6_padding5_16_9", "/greet6_text1", "/greet6_hearts"},
    };

    private static final String[][] SRCS_1_1 = new String[][]{
            {"/greet6_padding1_1_1", "/greet6_text1", "/greet6_tableware"},
            {"/greet6_padding2_1_1", "/greet6_text4", "/greet6_cloud"},
            {"/greet6_padding3_1_1", "/greet6_text1", "/greet6_heart"},
            {"/greet6_padding4_1_1", "/greet6_text4", "/greet6_cheer"},
            {"/greet6_padding5_1_1", "/greet6_text1", "/greet6_hearts"},
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
