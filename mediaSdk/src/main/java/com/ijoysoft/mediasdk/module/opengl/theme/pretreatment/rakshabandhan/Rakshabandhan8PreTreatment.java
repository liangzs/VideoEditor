package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan8PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan8_lb_padding", "/rakshabandhan8_lt_tie2"},
            {"/rakshabandhan8_title", "/rakshabandhan8_lt_flower", "/rakshabandhan8_rb_flower", "/rakshabandhan8_lt_tie", "/rakshabandhan8_rb_tie"},
            {"/rakshabandhan8_rt_padding", "/rakshabandhan8_rb_tie2"},
            {"/rakshabandhan8_center_tie", "/rakshabandhan8_lt_flower", "/rakshabandhan8_rb_flower"},
            {"/rakshabandhan8_title", "/rakshabandhan8_flower_with_tie"}
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }


    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }
}
