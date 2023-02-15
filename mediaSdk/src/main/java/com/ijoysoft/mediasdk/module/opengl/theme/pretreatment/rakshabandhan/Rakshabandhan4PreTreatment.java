package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan4PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan4_padding", "/rakshabandhan4_shine", "/rakshabandhan4_circle"},
            {"/rakshabandhan4_padding2", "/rakshabandhan4_tie", "/rakshabandhan4_circle3", "/rakshabandhan4_circle3", "/rakshabandhan4_shine", "/rakshabandhan4_circle2"},
            {"/rakshabandhan4_happy", "/rakshabandhan4_raksha", "/rakshabandhan4_bandhan", "/rakshabandhan4_tie", "/rakshabandhan4_circle3", "/rakshabandhan4_circle3", "/rakshabandhan4_shine", "/rakshabandhan4_circle2"},
            {"/rakshabandhan4_rb_padding", "/rakshabandhan4_tie2", "/rakshabandhan4_circle3", "/rakshabandhan4_circle3", "/rakshabandhan4_shine", "/rakshabandhan4_circle2"},
            {"/rakshabandhan4_rt_padding", "/rakshabandhan4_shine", "/rakshabandhan4_circle"}
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }
}
