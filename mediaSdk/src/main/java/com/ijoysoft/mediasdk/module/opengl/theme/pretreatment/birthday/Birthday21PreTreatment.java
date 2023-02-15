package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday;


import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

public class Birthday21PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/birthday21_pink_gradient", "/birthday21_green_gradient", "/birthday21_blue_gradient", "/birthday21_red_balloon",
                    "/birthday21_candle1", "/birthday21_candle2", "/birthday21_candle3", "/birthday21_candle4", "/birthday21_candle5", "/birthday21_candle6"},
            {"/birthday21_pink_gradient", "/birthday21_green_gradient", "/birthday21_blue_gradient", "/birthday21_yellow_title",
                    "/birthday21_candle1", "/birthday21_candle2", "/birthday21_candle3", "/birthday21_candle4", "/birthday21_candle5", "/birthday21_candle6"},
            {"/birthday21_pink_gradient", "/birthday21_green_gradient", "/birthday21_blue_gradient", "/birthday21_blue_balloon",
                    "/birthday21_candle1", "/birthday21_candle2", "/birthday21_candle3", "/birthday21_candle4", "/birthday21_candle5", "/birthday21_candle6"},
            {"/birthday21_pink_gradient", "/birthday21_green_gradient", "/birthday21_blue_gradient", "/birthday21_pink_title",
                    "/birthday21_candle1", "/birthday21_candle2", "/birthday21_candle3", "/birthday21_candle4", "/birthday21_candle5", "/birthday21_candle6"},
            {"/birthday21_pink_gradient", "/birthday21_green_gradient", "/birthday21_blue_gradient", "/birthday21_star_balloon",
                    "/birthday21_candle1", "/birthday21_candle2", "/birthday21_candle3", "/birthday21_candle4", "/birthday21_candle5", "/birthday21_candle6"},
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
