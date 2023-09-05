package com.ijoysoft.mediasdk.module.opengl.theme.action

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo.ORIENTATION
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ZoomByVertex

/**
 * 收录主题中控件的动画
 */
class ThemeWidgetHelper {
    companion object {
        /**
         * 简单的移动
         */
        fun normalMove(duration: Int, orient: ORIENTATION): BaseEvaluate {
            var startX = 0f;
            var startY = 0f;
            var endX = 0f;
            var endY = 0f;
            when (orient) {
                ORIENTATION.BOTTOM -> startY = 1f;
                ORIENTATION.TOP -> startY = -1f;
                ORIENTATION.LEFT -> startX = -1f
                ORIENTATION.RIGHT -> startX = 1f

                else -> {}
            }
            return AnimationBuilder(duration)
                .setCoordinate(startX, startY, endX, endY)
                .setIsNoZaxis(true)
                .build()
        }


        fun normalMoveOut(duration: Int, orient: ORIENTATION): BaseEvaluate {
            var startX = 0f;
            var startY = 0f;
            var endX = 0f;
            var endY = 0f;
            when (orient) {
                ORIENTATION.BOTTOM -> startY = -1f;
                ORIENTATION.TOP -> startY = 1f;
                ORIENTATION.LEFT -> startX = 1f
                ORIENTATION.RIGHT -> startX = -1f

                else -> {}
            }
            return AnimationBuilder(duration)
                .setCoordinate(startX, startY, endX, endY)
                .setIsNoZaxis(true)
                .build()
        }

        /**
         * 弹跳移动
         */
        fun springMove(duration: Int, orient: ORIENTATION): BaseEvaluate {
            val builder = AnimationBuilder(duration)
                .setOrientation(orient)
                .setIsNoZaxis(true)
            when (orient) {
                ORIENTATION.BOTTOM -> {
                    builder.setCoordinate(0f, 1f, 0f, 0f)
                }
                ORIENTATION.TOP -> {
                    builder.setCoordinate(0f, -1f, 0f, 0f)
                }


                else -> {}
            }
            return EnterEaseOutElastic(builder);
        }

        /**
         * 渐变
         */
        fun fade(isIn: Boolean, duration: Int): BaseEvaluate {
            if (isIn) {
                return AnimationBuilder(duration).setZView(0f).setIsNoZaxis(true).setFade(0f, 1f).build()
            }
            return AnimationBuilder(duration).setZView(0f).setIsNoZaxis(true).setFade(1f, 0f).build()
        }


        /**
         * 悬停状态时进行摇摆
         */
        fun shake(duration: Int, width: Int, height: Int, centerX: Float, centerY: Float): BaseEvaluate {
            return StayShakeAnimation(duration, width, height, centerX, centerY, 5, -2.5f)
        }


        /**
         * 缩放操作
         */
        fun zoomScale(duration: Long, vertex: FloatArray): BaseEvaluate {
            return ZoomByVertex(duration, vertex)
        }


    }
}