package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.friend;


import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.Collections;
import java.util.List;

public class Friend1PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/friend1_padding_gradient1", "/friend1_title"},
            {"/friend1_padding_gradient2", "/friend1_title"},
            {"/friend1_padding_gradient3", "/friend1_title"},
            {"/friend1_padding_gradient4", "/friend1_title"},
            {"/friend1_padding_gradient5", "/friend1_title"},
    };

    private static final String[][] SRCS_16_9 = new String[][]{
            {"/friend1_padding_gradient1_16_9", "/friend1_title"},
            {"/friend1_padding_gradient2_16_9", "/friend1_title"},
            {"/friend1_padding_gradient3_16_9", "/friend1_title"},
            {"/friend1_padding_gradient4_16_9", "/friend1_title"},
            {"/friend1_padding_gradient5_16_9", "/friend1_title"},
    };

    private static final String[][] SRCS_1_1 = new String[][]{
            {"/friend1_padding_gradient1_1_1", "/friend1_title"},
            {"/friend1_padding_gradient2_1_1", "/friend1_title"},
            {"/friend1_padding_gradient3_1_1", "/friend1_title"},
            {"/friend1_padding_gradient4_1_1", "/friend1_title"},
            {"/friend1_padding_gradient5_1_1", "/friend1_title"},
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


    private static final String[][] GIF_SRCS = new String[][]{
            {"/friend1_sound_line_dynamic"}, {},{},{},{}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return GIF_SRCS;
    }
}
