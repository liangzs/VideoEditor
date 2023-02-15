package com.ijoysoft.mediasdk.module.opengl.filter

import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo.ORIENTATION
import java.util.*
import kotlin.math.roundToInt

object ImageFilters {

    fun getRatioWH(bitmapWidth: Int, bitmapHeight: Int, screenWidth: Int, screenHeight: Int, fitX: Boolean? = null): FloatArray {
        // 居中后图片显示的大小
        val finalWidth: Int
        val finalHeight: Int
        if (fitX?:(bitmapWidth.toFloat() / screenWidth > bitmapHeight.toFloat() / screenHeight)) {
            finalWidth = screenWidth
            finalHeight = bitmapHeight * finalWidth / bitmapWidth
        } else {
            finalHeight = screenHeight
            finalWidth = bitmapWidth * finalHeight / bitmapHeight
        }
        // 坐标宽度
        return floatArrayOf(2 * finalWidth / screenWidth.toFloat(), 2 * finalHeight / screenHeight.toFloat())
    }

    fun getCenterScaleCube(bitmapWidth: Int, bitmapHeight: Int, screenWidth: Int, screenHeight: Int, fitX: Boolean? = null, scale: Float = 1f): FloatArray {
        val ratioPos = getRatioWH(bitmapWidth, bitmapHeight, screenWidth, screenHeight, fitX)
        ratioPos[0] /= 2f;
        ratioPos[1] /= 2f;
        return floatArrayOf(
            -ratioPos[0] / scale, ratioPos[1] / scale,
            -ratioPos[0] / scale, -ratioPos[1] / scale,
            ratioPos[0] / scale, ratioPos[1] / scale,
            ratioPos[0] / scale, -ratioPos[1] / scale
        )
    }

    fun ImageOriginFilter.applyCube(cube: FloatArray) {
        this.cube = cube
        mVerBuffer.clear()
        mVerBuffer.put(cube).position(0)
    }


    fun ImageOriginFilter.measureCornerPosition(
        bitmapWidth: Int,
        bitmapHeight: Int,
        screenWidth: Int,
        screenHeight: Int,
        orientation: ORIENTATION,
        scale: Float,
        fitX: Boolean? = null,
        apply: Boolean = true
    ): FloatArray {
        val ratioPos = getRatioWH(bitmapWidth, bitmapHeight, screenWidth, screenHeight, fitX)
        val result = when (orientation) {
            ORIENTATION.LEFT_TOP -> floatArrayOf(
                -1.0f, ratioPos[1] / scale - 1.0f,
                -1.0f, -1.0f,
                ratioPos[0] / scale - 1.0f, ratioPos[1] / scale - 1.0f,
                ratioPos[0] / scale - 1.0f, -1.0f
            )
            ORIENTATION.LEFT_BOTTOM -> floatArrayOf(
                -1.0f,
                1.0f,
                -1.0f,
                1.0f - ratioPos[1] / scale,
                ratioPos[0] / scale - 1.0f,
                1.0f,
                ratioPos[0] / scale - 1.0f,
                1.0f - ratioPos[1] / scale
            )
            ORIENTATION.RIGHT_TOP -> floatArrayOf(
                1.0f - ratioPos[0] / scale,
                ratioPos[1] / scale - 1.0f,
                1.0f - ratioPos[0] / scale,
                -1.0f,
                1.0f,
                ratioPos[1] / scale - 1.0f,
                1.0f,
                -1.0f
            )
            ORIENTATION.RIGHT_BOTTOM -> floatArrayOf(
                1.0f - ratioPos[0] / scale,
                1.0f,
                1.0f - ratioPos[0] / scale,
                1.0f - ratioPos[1] / scale,
                1.0f,
                1.0f,
                1.0f,
                1.0f - ratioPos[1] / scale
            )
            else -> {
                vertex.clone()
            }
        }
        LogUtils.i("ImageFilters", "measureCornerPosition: ${result.contentToString()}")
        if (apply) {
            cube = result
            mVerBuffer.clear()
            mVerBuffer.put(cube).position(0)
        }
        return result
    }

    fun ImageOriginFilter.measureEdgeDeltaPosition(
        bitmapWidth: Int,
        bitmapHeight: Int,
        screenWidth: Int,
        screenHeight: Int,
        orientation: ORIENTATION,
        scale: Float,
        deltaX: Float = 0f,
        deltaY: Float = 0f,
        fitX: Boolean? = null,
        apply: Boolean = true
    ): FloatArray {
        val ratioPos = getRatioWH(bitmapWidth, bitmapHeight, screenWidth, screenHeight, fitX)
        val result = when (orientation) {
            ORIENTATION.LEFT -> floatArrayOf(
                deltaX - 1f, deltaY + ratioPos[1] / (2 * scale),
                deltaX - 1f, deltaY - ratioPos[1] / (2 * scale),
                deltaX - 1f + ratioPos[0] / scale, deltaY + ratioPos[1] / (2 * scale),
                deltaX - 1f + ratioPos[0] / scale, deltaY - ratioPos[1] / (2 * scale)
            )
            ORIENTATION.RIGHT -> floatArrayOf(
                deltaX + 1 - ratioPos[0] / scale, deltaY + ratioPos[1] / (2 * scale),
                deltaX + 1f - ratioPos[0] / scale, deltaY - ratioPos[1] / (2 * scale),
                deltaX + 1f, deltaY + ratioPos[1] / (2 * scale),
                deltaX + 1f, deltaY - ratioPos[1] / (2 * scale)
            )
            ORIENTATION.BOTTOM -> floatArrayOf(
                deltaX - ratioPos[0] / (2 * scale), 1f + deltaY,
                deltaX - ratioPos[0] / (2 * scale), 1 - ratioPos[1] / scale + deltaY,
                deltaX + ratioPos[0] / (2 * scale), 1f + deltaY,
                deltaX + ratioPos[0] / (2 * scale), 1 - ratioPos[1] / scale + deltaY
            )
            ORIENTATION.TOP -> floatArrayOf(
                deltaX - ratioPos[0] / (2 * scale), ratioPos[1] / scale - 1f + deltaY,
                deltaX - ratioPos[0] / (2 * scale), -1f + deltaY,
                deltaX + ratioPos[0] / (2 * scale), ratioPos[1] / scale - 1 + deltaY,
                deltaX + ratioPos[0] / (2 * scale), -1f + deltaY
            )
            else -> {
                vertex.clone()
            }
        }
        LogUtils.i("ImageFilters", "measureCornerPosition: ${result.contentToString()}")
        if (apply) {
            cube = result
            mVerBuffer.clear()
            mVerBuffer.put(cube).position(0)
        }
        return result
    }

    fun ImageOriginFilter.measureFixEdgeDeltaPosition(
        bitmapWidth: Int,
        bitmapHeight: Int,
        screenWidth: Int,
        screenHeight: Int,
        orientation: ORIENTATION,
        scale: Float,
        deltaRatioX: Float = 0f,
        deltaRatioY: Float = 0f,
        fitX: Boolean? = null,
        apply: Boolean = true
    ): FloatArray {
        val ratioPos = getRatioWH(bitmapWidth, bitmapHeight, screenWidth, screenHeight, fitX)
        val deltaX = deltaRatioX * (1 - ratioPos[0] / 2f)
        val deltaY = deltaRatioY * (1 - ratioPos[1] / 2f)
        val result = when (orientation) {
            ORIENTATION.LEFT -> floatArrayOf(
                deltaX - 1f, deltaY + ratioPos[1] / (2 * scale),
                deltaX - 1f, deltaY - ratioPos[1] / (2 * scale),
                deltaX - 1f + ratioPos[0] / scale, deltaY + ratioPos[1] / (2 * scale),
                deltaX - 1f + ratioPos[0] / scale, deltaY - ratioPos[1] / (2 * scale)
            )
            ORIENTATION.RIGHT -> floatArrayOf(
                deltaX + 1 - ratioPos[0] / scale, deltaY + ratioPos[1] / (2 * scale),
                deltaX + 1f - ratioPos[0] / scale, deltaY - ratioPos[1] / (2 * scale),
                deltaX + 1f, deltaY + ratioPos[1] / (2 * scale),
                deltaX + 1f, deltaY - ratioPos[1] / (2 * scale)
            )
            ORIENTATION.BOTTOM -> floatArrayOf(
                deltaX - ratioPos[0] / (2 * scale), 1f + deltaY,
                deltaX - ratioPos[0] / (2 * scale), 1 - ratioPos[1] / scale + deltaY,
                deltaX + ratioPos[0] / (2 * scale), 1f + deltaY,
                deltaX + ratioPos[0] / (2 * scale), 1 - ratioPos[1] / scale + deltaY
            )
            ORIENTATION.TOP -> floatArrayOf(
                deltaX - ratioPos[0] / (2 * scale), ratioPos[1] / scale - 1f + deltaY,
                deltaX - ratioPos[0] / (2 * scale), -1f + deltaY,
                deltaX + ratioPos[0] / (2 * scale), ratioPos[1] / scale - 1 + deltaY,
                deltaX + ratioPos[0] / (2 * scale), -1f + deltaY
            )
            else -> {
                vertex.clone()
            }
        }
        LogUtils.i("ImageFilters", "measureCornerPosition: ${result.contentToString()}")
        if (apply) {
            cube = result
            mVerBuffer.clear()
            mVerBuffer.put(cube).position(0)
        }
        return result
    }
    
    
}