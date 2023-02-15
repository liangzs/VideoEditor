package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan7PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan7_tie", "/rakshabandhan7_title"},
            {"/rakshabandhan7_padding1"},
            {"/rakshabandhan7_circle1"},
            {"/rakshabandhan7_padding2"},
            {"/rakshabandhan7_circle1"}
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
