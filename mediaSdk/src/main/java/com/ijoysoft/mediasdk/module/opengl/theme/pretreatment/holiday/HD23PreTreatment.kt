package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday

import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment
import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class HD23PreTreatment : BaseTimePreTreatment() {
    override fun dealDuration(index: Int): Long {
        return 2240
    }


    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?): Array<Array<String>>? {
        return arrayOf(arrayOf("/hd23action1.gif"), emptyArray(), arrayOf("/hd23action3.gif"), emptyArray())
    }

    override fun getMipmapsCount() = 4



    override fun createMimapBitmaps() = null
}