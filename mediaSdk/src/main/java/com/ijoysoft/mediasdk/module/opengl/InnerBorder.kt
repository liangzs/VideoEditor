package com.ijoysoft.mediasdk.module.opengl

import android.os.Parcelable
import com.ijoysoft.mediasdk.module.entity.RatioType
import kotlinx.parcelize.Parcelize

/**
 * 边框类
 *
 * @date 2022/2/16  17:57
 * @author hayring
 * @param 下标顺序
 * @param file 该边框对应比例的素材路径
 */
@Parcelize
data class InnerBorder(val id: Int, val ratios: Set<RatioType>, val size: Int = 0) : Parcelable {


    /**
     * h获取除none外的路径
     */
    fun requirePath(ratioType: RatioType, width: Int, height: Int) =
//        if (ratioType == RatioType.NONE) {
            getAbsolutePath(RatioType.getNotNoneRatioType(width, height))
//        } else {
//            getAbsolutePath(ratioType)
//        }

    /**
     * 获取路径
     */
    fun getAbsolutePath(current: RatioType) = if (ratios.contains(current)) {
        if (size == 0) {
            "$localPrefix/$id/${current.name}.webp"
        } else {
            "$savePrefix/$id/${current.name}"
        }
    } else {
        val closelyRatio = getNormalCloselyRatio(current.ratioValue)
        if (size == 0) {
            "$localPrefix/$id/${closelyRatio.name}.webp"
        } else {
            "$savePrefix/$id/${closelyRatio.name}"
        }
    }

    /**
     * 获取保存路径
     */
    fun getPath(ratioType: RatioType, prefix: String = savePrefix) =
        if (ratios.contains(ratioType)) {
            "$prefix/$id/${ratioType.name}"
        } else {
            "$prefix/$id/${getNormalCloselyRatio(ratioType.ratioValue).name}"
        }




    /**
     *  在拥有的比例里找
     */
    fun getNormalCloselyRatio(ratioValue: Float): RatioType {
        var closelyRatio = RatioType._1_1
        var closely = Float.MAX_VALUE
        ratios.forEach { ratio ->
            (ratio.ratioValue / ratioValue).let {
                var value: Float = it
                if (it < 1) {
                    value = 1f/it
                }
                if (value < closely) {
                    closely = value
                    closelyRatio = ratio
                }
            }
        }
        return closelyRatio
    }


    companion object {

        /**
         * asset prefix
         */
        const val assetPrefix = "file:///android_asset/"

        /**
         * 本地路径前缀
         */
        const val localPrefix = "${assetPrefix}innerborder"

        /**
         * 没有边框
         */
        val NONE = InnerBorder(0, emptySet())

        /**
         * 常规5种比例
         */
        val normalRatioType = hashSetOf(RatioType.NONE, RatioType._1_1, RatioType._16_9, RatioType._9_16, RatioType._3_4, RatioType._4_3)


        /**
         * 所有边框列表
         */
        val list: MutableList<InnerBorder> = ArrayList<InnerBorder>().apply {
            add(NONE)
            add(InnerBorder(-1, normalRatioType, 29232))
            add(InnerBorder(-2, normalRatioType, 228286))
            add(InnerBorder(-3, normalRatioType, 194562))
        }

        val idMap: MutableMap<Int, InnerBorder> by lazy {
            HashMap<Int, InnerBorder>().also { map ->
                list.forEach { map[it.id] = it }
            }
        }


        /**
         * 初始化idMap
         */
        @Synchronized
        fun init(onLineList: List<InnerBorder>, prefix: String) {
            list.clear()
            list.add(NONE)
            list.addAll(onLineList)
            idMap.clear()
            list.forEach { idMap[it.id] = it }
            savePrefix = prefix
        }


        /**
         * 本地地址
         * DownloadHelper.<init>
         */
        lateinit var savePrefix: String


    }


}