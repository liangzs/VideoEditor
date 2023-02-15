package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan9PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan9_bottom_padding1", "/rakshabandhan9_title", "/rakshabandhan9_rt_flower"},
            {"/rakshabandhan9_padding", "/rakshabandhan9_tie", "/rakshabandhan9_flower"},
            {"/rakshabandhan9_padding", "/rakshabandhan9_right_tie", "/rakshabandhan9_title"},
            {"/rakshabandhan9_bottom_padding1", "/rakshabandhan9_title", "/rakshabandhan9_flowers"},
            {"/rakshabandhan9_bottom_padding2", "/rakshabandhan9_tie", "/rakshabandhan9_flowers", "/rakshabandhan9_flower"}
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
