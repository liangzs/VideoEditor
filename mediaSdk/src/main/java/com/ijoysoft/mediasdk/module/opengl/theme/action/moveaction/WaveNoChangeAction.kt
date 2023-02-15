package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction

import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.LogUtils
import java.util.*
import kotlin.math.ceil

/**
 * 摆动但是不改变最终状态的动画
 * @author hayring
 * @date 2022/12/17
 */
abstract class WaveNoChangeAction(
    private val transition: Boolean,
    private val scale: Boolean,
    private val rotate: Boolean,
    private val skew: Boolean
) : IMoveAction {
    override fun action(duration: Float, start: Float, max: Float): FloatArray {
        val framesNum = ceil((duration * ConstantMediaSize.FPS / 1000f).toDouble()).toInt()
        val frames = FloatArray(framesNum)
        var frameValue: Double
        val delta = max - start
        val frameScale = 25.toDouble() / ConstantMediaSize.FPS
        for (i in 0 until framesNum) {
            frameValue = function(i.toDouble() * frameScale)
            frames[i] = start + (frameValue * delta).toFloat()
        }
//        LogUtils.v(javaClass.simpleName, "frames: " + frames.contentToString())
        return frames
    }

    /**
     * 具体实现函数
     * @param x x
     * @return y
     */
    abstract fun function(x: Double): Double


    override fun isTranslate(): Boolean {
        return transition
    }

    override fun isRotate(): Boolean {
        return rotate
    }

    override fun isScale(): Boolean {
        return scale
    }

    override fun isSkew(): Boolean {
        return skew
    }
}