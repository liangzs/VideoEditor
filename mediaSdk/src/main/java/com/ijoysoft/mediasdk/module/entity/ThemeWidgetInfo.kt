package com.ijoysoft.mediasdk.module.entity

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize

/**
 * 组合动画时候的控件显示，挂载在时间轴上
 */
class ThemeWidgetInfo {
    var enter = 0
    var stay = 0
    var out = 0
    var startTime: Long
    var endTime: Long
    var bitmap: Bitmap? = null

    //多区间列表
    var periods: List<Pair<Long, Long>>? = null;

    constructor(enter: Int, stay: Int, out: Int, startTime: Long, endTime: Long) {
        this.enter = enter
        this.stay = stay
        this.out = out
        this.startTime = startTime
        this.endTime = endTime
    }

    constructor(startTime: Long, endTime: Long) {
        this.startTime = startTime
        this.endTime = endTime
    }

    constructor(startTime: Long, endTime: Long, bitmap: Bitmap?) {
        this.startTime = startTime
        this.endTime = endTime
        this.bitmap = bitmap
    }

    fun isInRange(duration: Int): Boolean {
        return duration >= startTime && duration < endTime
    }

    fun isLastFrame(duration: Int): Boolean {
        return duration < endTime && duration + ConstantMediaSize.PHTOTO_FPS_TIME >= endTime
    }

    val time: Long
        get() = endTime - startTime

    fun clone(): ThemeWidgetInfo {
        return ThemeWidgetInfo(enter, stay, out, startTime, endTime)
    }


    fun cloneWithSameBitmap(): ThemeWidgetInfo {
        val clone = clone()
        clone.bitmap = bitmap
        return clone
    }


    /**
     * 创建多少秒之后一致的
     * @param delay
     */
    fun createForNext(delay: Int): ThemeWidgetInfo {
        return ThemeWidgetInfo(enter, stay, out, startTime + delay, endTime + delay)
    }

    /**
     * 创建多少秒之后一致的
     * @param delay
     */
    fun createForNextWithBitmap(delay: Int): ThemeWidgetInfo {
        val clone = createForNext(delay)
        clone.bitmap = bitmap
        return clone
    }
}