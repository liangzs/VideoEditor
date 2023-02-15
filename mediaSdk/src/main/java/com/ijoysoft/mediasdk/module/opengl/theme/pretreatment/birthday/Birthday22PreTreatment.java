package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday22PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday22_padding", "/birthday22_pink_title", "/birthday22_butterfly", "/birthday22_red_gradient",
                    "/birthday22_red_gradient"},
            {"/birthday22_padding", "/birthday22_green_title", "/birthday22_butterfly", "/birthday22_red_gradient",
                    "/birthday22_red_gradient"},
            {"/birthday22_padding", "/birthday22_balloon1", "/birthday22_butterfly", "/birthday22_red_gradient",
                    "/birthday22_red_gradient"},
            {"/birthday22_padding", "/birthday22_yellow_title", "/birthday22_butterfly", "/birthday22_flag", "/birthday22_red_gradient",
                    "/birthday22_red_gradient"},
            {"/birthday22_padding", "/birthday22_balloon2", "/birthday22_butterfly", "/birthday22_red_gradient",
                    "/birthday22_red_gradient"},
    };



    private static final String PADDING_9_16_PATH = "/birthday22_padding";
    private static final String PADDING_16_9_PATH = "/birthday22_padding_16_9";
    private static final String PADDING_1_1_PATH = "/birthday22_padding_1_1";
    private static final String FLAG_9_16_PATH = "/birthday22_flag";
    private static final String FLAG_16_9_PATH = "/birthday22_flag_16_9";


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
        if (index % 5 == 3) {
            SRCS[3][3] = FLAG_9_16_PATH;
        }
        SRCS[index][0] = PADDING_9_16_PATH;
        return SRCS[index];
    }

    @Override
    protected String[] getSources_1_1(int index) {
        if (index % 5 == 3) {
            SRCS[3][3] = FLAG_9_16_PATH;
        }
        SRCS[index][0] = PADDING_1_1_PATH;
        return SRCS[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        if (index % 5 == 3) {
            SRCS[3][3] = FLAG_16_9_PATH;
        }
        SRCS[index][0] = PADDING_16_9_PATH;
        return SRCS[index];
    }
}
