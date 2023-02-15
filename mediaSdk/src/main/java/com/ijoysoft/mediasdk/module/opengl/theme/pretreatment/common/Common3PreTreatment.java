package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;


import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.LocalPreTreatmentTemplate;

public class Common3PreTreatment extends DownloadedPreTreatmentTemplate {


//    private static final int[][] SRCS = new int[][]{
//            {R.mipmap.common3_padding_gradient1_9_16},
//            {R.mipmap.common3_padding_gradient2_9_16},
//            {R.mipmap.common3_padding_gradient3_9_16},
//            {R.mipmap.common3_padding_gradient4_9_16},
//            {R.mipmap.common3_padding_gradient5_9_16},
//    };
//
//    private static final int[][] SRCS_16_9 = new int[][]{
//            {R.mipmap.common3_padding_gradient1_16_9},
//            {R.mipmap.common3_padding_gradient2_16_9},
//            {R.mipmap.common3_padding_gradient3_16_9},
//            {R.mipmap.common3_padding_gradient4_16_9},
//            {R.mipmap.common3_padding_gradient5_16_9},
//    };
//
//    private static final int[][] SRCS_1_1 = new int[][]{
//            {R.mipmap.common3_padding_gradient1_1_1},
//            {R.mipmap.common3_padding_gradient2_1_1},
//            {R.mipmap.common3_padding_gradient3_1_1},
//            {R.mipmap.common3_padding_gradient4_1_1},
//            {R.mipmap.common3_padding_gradient5_1_1},
//    };


    private static final String[][] SRCS = new String[][]{
            {"/common3_padding_gradient1_9_16"},
            {"/common3_padding_gradient2_9_16"},
            {"/common3_padding_gradient3_9_16"},
            {"/common3_padding_gradient4_9_16"},
            {"/common3_padding_gradient5_9_16"},
    };
    private static final String[][] SRCS_16_9 = new String[][]{
            {"/common3_padding_gradient1_16_9"},
            {"/common3_padding_gradient2_16_9"},
            {"/common3_padding_gradient3_16_9"},
            {"/common3_padding_gradient4_16_9"},
            {"/common3_padding_gradient5_16_9"},
    };
    private static final String[][] SRCS_1_1 = new String[][]{
            {"/common3_padding_gradient1_1_1"},
            {"/common3_padding_gradient2_1_1"},
            {"/common3_padding_gradient3_1_1"},
            {"/common3_padding_gradient4_1_1"},
            {"/common3_padding_gradient5_1_1"},
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
    protected String[] getSources_1_1(int index) {
        return SRCS_1_1[index];
    }

    @Override
    protected String[] getSources_16_9(int index) {
        return SRCS_16_9[index];
    }

    private static final String[][] GIF = new String[][]{
            {"/common3_dynamic_sound_line"}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return GIF;
    }

}
