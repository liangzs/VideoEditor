package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.onam;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

/**
 * 欧南节5
 */
public class Onam5PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/onam5_padding1", "/onam5_chain1", "/onam5_top_flowers", "/onam5_bottom_flowers"},
            {"/onam5_padding2", "/onam5_chains", "/onam5_flower", "/onam5_tag", "/onam5_text1", "/onam5_text2", "/onam5_flower"},
            //TODO : 缺少例子
            {"/onam5_padding2", "/onam5_bottom_flowers"},
            {"/onam5_padding2", "/onam5_left_flowers", "/onam5_right_flowers", "/onam5_title", "/onam5_flower"},
            {"/onam5_padding3", "/onam5_circle_tag", "/onam5_text1", "/onam5_text2", "/onam5_lt_flowers", "/onam5_rt_flowers", "/onam5_lb_flowers", "/onam5_rb_flowers"},
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
}
