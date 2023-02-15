package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.travel

import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment

class Travel14Pretreatment : BasePreTreatment() {

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/travel14_gif1"))
    }

}