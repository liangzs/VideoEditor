package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.onam;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

/**
 * 欧南节3
 */
public class Onam3PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            //重写父类，复用此张图
            {"/onam3_yellow_flower"},
            {"/onam3_yellow_flower2", "/onam3_title", "/onam3_purple_flower"},
            {"/onam3_rotate_dark_flower", "/onam3_rotate_flower"},
            {"/onam3_flag", "/onam3_rotate_flower", "/onam3_petals_up"},

            //重写父类，复用此张图
            {"/onam3_petals_up", "/onam3_petals_down",
                    "/onam3_purple_flower", "/onam3_yellow_flower2"},

            {"/onam3_yellow_flower" ,},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }
}
