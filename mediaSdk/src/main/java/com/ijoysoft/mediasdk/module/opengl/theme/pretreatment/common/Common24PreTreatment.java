package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common;

import com.ijoysoft.mediasdk.module.entity.RatioType;

/**
 * @author hayring
 * @date 2022/1/18  13:52
 */
public class Common24PreTreatment extends Common8PreTreatment {


    private static final String[][] GIF = new String[][]{
            {"/common24_dynamic"}
    };

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return GIF;
    }
}
