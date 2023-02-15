package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.onam;

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

/**
 * 欧南节4
 * TODO: 缺少粒子
 */
public class Onam4PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            /*, "/onam4_particle"*/
            {"/onam4_orange_circle", "/onam4_orange_circle"},
            //重写代码以复用素材
            {"/onam4_purple_flower", "/onam4_orange_flower", "/onam4_red_flower"},
            {"/onam4_orange_circle", "/onam4_orange_circle", "/onam4_double_umbrella", "/onam4_blue_fireworks", "/onam4_pink_fireworks", "/onam4_orange_fireworks"},
            //重写代码以复用素材
            {"/onam4_red_flower", "/onam4_purple_flower"},
            {"/onam4_orange_circle", "/onam4_title", "/onam4_pink_fireworks", "/onam4_orange_fireworks", "/onam4_blue_fireworks"},
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
