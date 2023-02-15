package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine

import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment

class Valentine17Pretreatment : BasePreTreatment() {

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/valentine_gif"))
    }

}