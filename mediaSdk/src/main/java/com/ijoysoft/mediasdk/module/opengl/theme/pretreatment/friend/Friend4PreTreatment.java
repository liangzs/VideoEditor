package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.friend;


import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Friend4PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/friend4_padding_9_16", "/friend4_pink_gradient", "/friend4_blue_gradient", "/friend4_cyan_gradient", "/friend4_yellow_gradient", "/friend4_title1"},
            {"/friend4_padding_9_16", "/friend4_pink_gradient", "/friend4_blue_gradient", "/friend4_cyan_gradient", "/friend4_yellow_gradient", "/friend4_title2"},
            {"/friend4_padding_9_16", "/friend4_pink_gradient", "/friend4_blue_gradient", "/friend4_cyan_gradient", "/friend4_yellow_gradient", "/friend4_title3"},
            {"/friend4_padding_9_16", "/friend4_pink_gradient", "/friend4_blue_gradient", "/friend4_cyan_gradient", "/friend4_yellow_gradient", "/friend4_title4"},
            {"/friend4_padding_9_16", "/friend4_pink_gradient", "/friend4_blue_gradient", "/friend4_cyan_gradient", "/friend4_yellow_gradient", "/friend4_title5"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }


    private static final String PADDING_9_16_PATH = "/friend4_padding_9_16";
    private static final String PADDING_16_9_PATH = "/friend4_padding_16_9";
    private static final String PADDING_1_1_PATH = "/friend4_padding_1_1";


    public static final String[][] DYNAMIC_MIMAP_BITMAP_SOURCES = new String[][]{
            {"/friend4_butterfly_dynamic"},
            {"/friend4_butterfly_dynamic"},
            {"/friend4_butterfly_dynamic"},
            {"/friend4_butterfly_dynamic"},
            {"/friend4_butterfly_dynamic"},
    };


    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return DYNAMIC_MIMAP_BITMAP_SOURCES;
    }

    @Override
    protected String[] getSource(int index) {
        SRCS[index][0] = PADDING_9_16_PATH;
        return SRCS[index];
    }

    @Override
    protected String[] getSources_1_1(int index) {
        SRCS[index][0] = PADDING_1_1_PATH;
        return SRCS[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        SRCS[index][0] = PADDING_16_9_PATH;
        return SRCS[index];
    }
}
