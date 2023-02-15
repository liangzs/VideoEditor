package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas2

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmbeddedFilterEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.opengl.transition.QuarterCylinderOutFilter

abstract class Christmas2Template: BaseTimeThemeManager() {




    override fun computeThemeCycleTime() = 9065

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {}


    override fun isTextureInside(): Boolean {
        return true
    }


    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate>? {
        val list = ArrayList<BaseEvaluate>()
        if (index % 8 < 5) {
            currentDuration = 1333
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 5, 40),
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseOutSine(false, true, false, false))
                    .setScale(1f, 1.1f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 5, 40),
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseInSine(false, true, false, false))
                    .setScale(1.1f, 1f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    1333 - getDuration(currentDuration.toLong(), 10, 40),
                    width,
                    height,
                    true
                )
                    .build()
            )
            if (duration > currentDuration) {
                list.add(
                    AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build()
                )
            }
        } else {
            currentDuration = 800
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 7, 24),
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseInSine(false, true, false, false))
                    .setScale(1.1f, 1f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    800- getDuration(currentDuration.toLong(), 7, 24),
                    width,
                    height,
                    true
                )
                    .build()
            )
            if (duration > currentDuration) {
                list.add(
                    AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build()
                )
            }
        }
        return list
    }

    override fun createActions(): Int {
        return 8
    }


    override fun blurBackground(): Boolean {
        return true
    }
}