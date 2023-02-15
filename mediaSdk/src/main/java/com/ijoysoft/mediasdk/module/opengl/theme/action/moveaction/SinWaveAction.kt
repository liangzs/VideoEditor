package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction

import kotlin.math.PI
import kotlin.math.sin

/**
 * 摆动但是不改变最终状态的动画
 * @author hayring
 * @date 2022/12/17
 */
class SinWaveAction(
    cycleDuration: Int,
    private val transition: Boolean,
    private val scale: Boolean,
    private val rotate: Boolean,
    private val skew: Boolean
) : WaveNoChangeAction(transition, scale, rotate, skew) {


    private val cycleFrame = (cycleDuration * 25 / 1000.toDouble())


    override fun function(x: Double) =
        sin((2 * x * PI) / cycleFrame)


}