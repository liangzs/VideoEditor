package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment;

import com.ijoysoft.mediasdk.R;

/**
 * @author hayring
 * @date 2021/12/16  19:50
 */
public class EmptyPreTreatment extends LocalPreTreatmentTemplate{
    private static final int[][] EMPTY = new int[][]{
            {},
    };


    @Override
    protected int[][] getSources() {
        return EMPTY;
    }


}
