package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter.init
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType
import org.libpag.PAGFile
import java.util.*
import kotlin.collections.ArrayList

class Daily1Pretreatment : BasePreTreatment() {

    override fun getMipmapsCount(): Int {
        return 6
    }



    //override fun getThemePags(): MutableList<PAGFile> {
    //    return Arrays.asList(PAGFile.Load(ConstantMediaSize.themePath + "/christmas6_pag1"));
    //}

    override fun createDuration(): Int {
        return 3000
    }

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType?): Array<Array<String>> {
        return arrayOf(
            arrayOf("/haha", "/happy"),
            arrayOf("/haha", "/happy"),
            arrayOf("/magic", "/star"),
            arrayOf("/magic", "/star"),
            arrayOf("/heart", "/heart"),
            arrayOf("/heart", "/heart")
        )
    }

    override fun createTransition(index: Int): TransitionFilter? {
        if ((index % 6).let { it == 1 || it == 4 }) {
            return TransitionFactory.initFilters(TransitionType.PAG_HALO6)
        }
        return TransitionFactory.initFilters(TransitionType.NONE)
    }
}