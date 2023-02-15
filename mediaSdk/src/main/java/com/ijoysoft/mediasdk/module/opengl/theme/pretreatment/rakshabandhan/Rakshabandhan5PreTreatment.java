package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Rakshabandhan5PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan5_top_tag", "/rakshabandhan5_title", "/rakshabandhan5_salute"},
            {"/rakshabandhan5_bottom_tag", "/rakshabandhan5_top_gradient", "/rakshabandhan5_flower"},
            {"/rakshabandhan5_tie", "/rakshabandhan5_bottom_gradient", "/rakshabandhan5_circle", "/rakshabandhan5_flower"},
            {"/rakshabandhan5_tie", "/rakshabandhan5_circle",
                    "/rakshabandhan5_particle1", "/rakshabandhan5_particle2",
                    "/rakshabandhan5_particle3", "/rakshabandhan5_particle4",
                    "/rakshabandhan5_particle5", "/rakshabandhan5_particle6",},
            {"/rakshabandhan5_top_tag", "/rakshabandhan5_bottom_tag", "/rakshabandhan5_title", "/rakshabandhan5_flower"},
            {"/rakshabandhan5_tie_rotated", "/rakshabandhan5_tie_rotated", "/rakshabandhan5_circle",
                    "/rakshabandhan5_particle1", "/rakshabandhan5_particle2",
                    "/rakshabandhan5_particle3", "/rakshabandhan5_particle4",
                    "/rakshabandhan5_particle5", "/rakshabandhan5_particle6"},
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
