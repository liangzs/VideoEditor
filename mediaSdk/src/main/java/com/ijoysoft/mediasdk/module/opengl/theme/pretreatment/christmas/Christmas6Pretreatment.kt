package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment
import org.libpag.PAGFile
import java.util.*
import kotlin.collections.ArrayList

class Christmas6Pretreatment : BasePreTreatment() {
    private val MIPCOUNT = 3
    override fun getMipmapsCount(): Int {
        return MIPCOUNT
    }

    override fun getRatio916(index: Int): List<Bitmap?> {
        val list: MutableList<Bitmap?> = ArrayList()
        when (index % MIPCOUNT) {
            0 -> {
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas6_3_tl" + SUFFIX))
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas6_3_tr" + SUFFIX))
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas6_4" + SUFFIX))
            }
            1 -> {
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas6_1" + SUFFIX))
            }
            2 -> {
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas6_2" + SUFFIX))
            }
        }
        return list
    }

    override fun getFinalDynamicMimapBitmapSources(ratioType: RatioType): Array<Array<String>> {
        return arrayOf(arrayOf("/christmas6_gif1"))
    }

    //override fun getThemePags(): MutableList<PAGFile> {
    //    return Arrays.asList(PAGFile.Load(ConstantMediaSize.themePath + "/christmas6_pag1"));
    //}

    override fun createDuration(): Int {
        return super.createDuration()
    }
}