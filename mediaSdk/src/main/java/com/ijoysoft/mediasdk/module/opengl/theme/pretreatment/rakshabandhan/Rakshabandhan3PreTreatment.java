package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan3PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan3_top_flower", "/rakshabandhan3_bottom_flower", "/rakshabandhan3_pendant1"},
            {"/rakshabandhan3_bottom_gradient", "/rakshabandhan3_pendant6", "/rakshabandhan3_flower4", "/rakshabandhan3_flower3", "/rakshabandhan3_flower4"},
            {"/rakshabandhan3_top_gradient", "/rakshabandhan3_pendant3", "/rakshabandhan3_flower"},
            {"/rakshabandhan3_title", "/rakshabandhan3_pendant3", "/rakshabandhan3_pendant4", "/rakshabandhan3_pendant1", "/rakshabandhan3_pendant5", "/rakshabandhan3_pendant2"},
            {"/rakshabandhan3_horizon_pendant", "/rakshabandhan3_flower2"}
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
