package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.Common6PreTreatment

class HD26Pretreatment: Common6PreTreatment() {

    val dynamicMipmap = arrayOf(arrayOf("/holiday26_dynamic"))

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?) = dynamicMipmap


}