package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas

import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment

class Christmas0Pretreatment: BasePreTreatment() {

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?) = gifSource

    companion object {
        val gifSource = arrayOf(arrayOf("/christmas0_light", "/christmas0_tree", "/christmas0_trees"))
    }
}