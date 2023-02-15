package com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate

/**
 * 做顶点变化来实现缩放效果，如果采用matrix缩放，中点是对不上的
 *默认缩、放时间都是500ms
 */
class ZoomByVertex(duration: Long,vertex:FloatArray) : BaseEvaluate() {
    val DEFAULT_PERIOD = 500;
    val PERIOD_COUNT = (DEFAULT_PERIOD * ConstantMediaSize.FPS / 1000)
    var revearse: Boolean = false;
    var origin = FloatArray(8)
    private var posOffset = FloatArray(8) //计算控件出现动画



    init {
        frameCount = Math.ceil((duration * ConstantMediaSize.FPS / 1000f).toDouble()).toInt()
        origin=vertex;
        posOffset = evaluateShade(origin)!!
    }


    /**
     * 返回的是顶点数组
     */
    override fun draw(): FloatArray {
        if (revearse) {
            arrayAdd()
        } else {
            arraySub()
        }
        if (frameIndex > PERIOD_COUNT) {
            revearse = !revearse;
            frameIndex = 0;
        }
        frameIndex++
        return origin;
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    fun evaluateShade(origin: FloatArray): FloatArray? {
        val offset = FloatArray(8)
        val end = FloatArray(8)
        end[0] = origin[0] - 0.02f
        end[1] = origin[1] + 0.02f
        //end[1]为顶点
        end[2] = origin[2] - 0.02f
        end[3] = origin[3] - 0.02f
        end[4] = origin[4] + 0.02f
        end[5] = origin[5] + 0.02f
        //end[5]为1顶点
        end[6] = origin[6] + 0.02f
        end[7] = origin[7] - 0.02f
        for (i in end.indices) {
            offset[i] = (end[i] - origin[i]) / PERIOD_COUNT
        }
        return offset
    }

    private fun arrayAdd() {
        for (i in origin.indices) {
            origin[i] = origin[i] + posOffset[i]
        }
    }

    private fun arraySub() {
        for (i in origin.indices) {
            origin[i] = origin[i] - posOffset[i]
        }
    }

}