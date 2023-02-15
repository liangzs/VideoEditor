package com.ijoysoft.mediasdk.module.opengl.theme.action

import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation

class ThemeClipHelper {
    companion object {
        /**
         * 常用主片段缩放悬停
         */
        fun normalStay(): BaseEvaluate {
            return StayScaleAnimation(BaseBlurThemeExample.DEFAULT_STAY_TIME, false, 1f, 0.1f, 1.1f).apply {
                setZView(0f)
            }
        }

    }
}