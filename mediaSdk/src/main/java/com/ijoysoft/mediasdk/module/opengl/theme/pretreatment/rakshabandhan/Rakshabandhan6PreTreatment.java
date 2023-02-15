package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan6PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan6_bottom_padding", "/rakshabandhan6_circle"},
            {"/rakshabandhan6_padding2", "/rakshabandhan6_light", "/rakshabandhan6_light", "/rakshabandhan6_light"},
            {"/rakshabandhan6_padding3", "/rakshabandhan6_flower_with_tie"},
            {"/rakshabandhan6_padding3", "/rakshabandhan6_gradient"},
            {"/rakshabandhan6_top_padding"}
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
