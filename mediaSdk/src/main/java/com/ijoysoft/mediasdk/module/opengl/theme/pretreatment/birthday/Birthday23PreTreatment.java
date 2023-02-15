package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday23PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday23_lb_salute", "/birthday23_rb_salute"},
            {Birthday23PreTreatment.FLAG_9_16_PATH, "/birthday23_title"},
            {"/birthday23_gift", "/birthday23_gift"},
            {Birthday23PreTreatment.FLAG_9_16_PATH, "/birthday23_hart"},
            {Birthday23PreTreatment.FLAG_9_16_PATH, "/birthday23_balloon"},
    };


    private static final String FLAG_9_16_PATH = "/birthday23_flag";


    private static final String FLAG_16_9_PATH = "/birthday23_flag_16_9";


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

    @Override
    protected String[] getSource(int index) {
        if (index == 1 || index == 3 || index == 4) {
            SRCS[index][0] = FLAG_9_16_PATH;
        }
        return SRCS[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        if (index == 1 || index == 3 || index == 4) {
            SRCS[index][0] = FLAG_16_9_PATH;
        }
        return SRCS[index];
    }
}
