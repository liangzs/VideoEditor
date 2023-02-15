package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate;

/**
 * 通用1
 */
public class Common1PreTreatment extends DownloadedPreTreatmentTemplate {


//    private static final int[][] SRCS = new int[][]{
//            {R.mipmap.common1_background_gradient, R.mipmap.common1_blue_gradient},
//            {R.mipmap.common1_background_gradient, R.mipmap.common1_yellow_gradient},
//            {R.mipmap.common1_background_gradient, R.mipmap.common1_pink_gradient, R.mipmap.common1_spectrum},
//            {R.mipmap.common1_background_gradient, R.mipmap.common1_cyan_gradient},
//            {R.mipmap.common1_background_gradient, R.mipmap.common1_yellow_gradient},
//            {R.mipmap.common1_background_gradient, R.mipmap.common1_cyan_gradient},
//    };

//    private static final int[][] SRCS_16_9 = new int[][]{
//            {R.mipmap.common1_background_gradient_16_9, R.mipmap.common1_blue_gradient},
//            {R.mipmap.common1_background_gradient_16_9, R.mipmap.common1_yellow_gradient},
//            {R.mipmap.common1_background_gradient_16_9, R.mipmap.common1_pink_gradient, R.mipmap.common1_spectrum_16_9},
//            {R.mipmap.common1_background_gradient_16_9, R.mipmap.common1_cyan_gradient,},
//            {R.mipmap.common1_background_gradient_16_9, R.mipmap.common1_yellow_gradient},
//            {R.mipmap.common1_background_gradient_16_9, R.mipmap.common1_cyan_gradient},
//    };


    /**
     * Opengl转场时间
     *
     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
     */
    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }


    private static final String[][] SRCS = new String[][]{
            {"/common1_background_gradient", "/common1_blue_gradient"},
            {"/common1_background_gradient", "/common1_yellow_gradient"},
            {"/common1_background_gradient", "/common1_pink_gradient", "/common1_spectrum"},
            {"/common1_background_gradient", "/common1_cyan_gradient"},
            {"/common1_background_gradient", "/common1_yellow_gradient"},
            {"/common1_background_gradient", "/common1_cyan_gradient"},
    };
    private static final String[][] SRCS_16_9 = new String[][]{
            {"/common1_background_gradient_16_9", "/common1_blue_gradient"},
            {"/common1_background_gradient_16_9", "/common1_yellow_gradient"},
            {"/common1_background_gradient_16_9", "/common1_pink_gradient", "/common1_spectrum_16_9"},
            {"/common1_background_gradient_16_9", "/common1_cyan_gradient"},
            {"/common1_background_gradient_16_9", "/common1_yellow_gradient"},
            {"/common1_background_gradient_16_9", "/common1_cyan_gradient"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }


    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }


}
