package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;


import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate;

public class Common4PreTreatment extends DownloadedPreTreatmentTemplate {


//    private static final int[][] SRCS = new int[][]{
//            {R.mipmap.common4_gradient1},
//            {R.mipmap.common4_gradient2},
//            {R.mipmap.common4_gradient3},
//            {R.mipmap.common4_gradient4},
//            {R.mipmap.common4_gradient5},
//    };
//
//    private static final int[][] SRCS_16_9 = new int[][]{
//            {R.mipmap.common4_gradient1_16_9},
//            {R.mipmap.common4_gradient2_16_9},
//            {R.mipmap.common4_gradient3_16_9},
//            {R.mipmap.common4_gradient4_16_9},
//            {R.mipmap.common4_gradient5_16_9},
//    };


    private static final String[][] SRCS = new String[][]{
            {"/common4_gradient1"},
            {"/common4_gradient2"},
            {"/common4_gradient3"},
            {"/common4_gradient4"},
            {"/common4_gradient5"},
    };
    private static final String[][] SRCS_16_9 = new String[][]{
            {"/common4_gradient1_16_9"},
            {"/common4_gradient2_16_9"},
            {"/common4_gradient3_16_9"},
            {"/common4_gradient4_16_9"},
            {"/common4_gradient5_16_9"},
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }

    /**
     * Opengl转场时间
     *
     * @return {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment#THEME_TWO_DURATION}
     */
    @Override
    public int createDuration() {
        return THEME_TWO_DURATION;
    }

    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }
}
