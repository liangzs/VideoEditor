package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.EmptyPreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate;

/**
 * 通用2
 */
public class Common2PreTreatment extends DownloadedPreTreatmentTemplate {

//
//    private static final int[][] SRCS = new int[][]{
//            {R.mipmap.common2_cyan_gradient, R.mipmap.common2_yellow_gradient},
//            {R.mipmap.common2_orange_gradient, R.mipmap.common2_pink_gradient},
//            {R.mipmap.common2_pink_gradient, R.mipmap.common2_yellow_gradient},
//            {R.mipmap.common2_yellow_gradient, R.mipmap.common2_orange_gradient},
//            {R.mipmap.common2_pink_gradient, R.mipmap.common2_orange_gradient},
//    };

    private static final String[][] SRCS = new String[][]{
            {"/common2_cyan_gradient", "/common2_yellow_gradient"},
            {"/common2_orange_gradient", "/common2_pink_gradient"},
            {"/common2_pink_gradient", "/common2_yellow_gradient"},
            {"/common2_yellow_gradient", "/common2_orange_gradient"},
            {"/common2_pink_gradient", "/common2_orange_gradient"},
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
