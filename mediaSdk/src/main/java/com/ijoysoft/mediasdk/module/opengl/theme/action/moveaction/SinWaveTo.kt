package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction

import kotlin.math.PI
import kotlin.math.sin

/**
 * 经过cycle个周期的摇摆之后到从0到达1的位置
 */
class SinWaveTo(val cycle: Int, transition: Boolean, scale: Boolean, rotate: Boolean, skew: Boolean) :
    AbstractEase(transition, scale, rotate, skew) {


    override fun function(x: Double) =
        sin((0.5 + 2 * cycle) * x * PI)
}