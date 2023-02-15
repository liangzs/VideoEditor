package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas1

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmbeddedFilterEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.opengl.transition.QuarterCylinderOutFilter

abstract class Christmas1Template: BaseTimeThemeManager() {




    override fun computeThemeCycleTime() = 4240

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {}


    override fun isTextureInside(): Boolean {
        return true
    }


    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate>? {
        val list = ArrayList<BaseEvaluate>()
        currentDuration = 2120
        if (index%2 == 0) {
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 17, 32),
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseOutQuart(true, false, false, false))
                    .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                    .setStartConor(-10)
                    .setEndConor(10)
                    .setScale(0.9f, 0.9f)
                    .setStartX(0.5f)
                    .setEndX(-0.3f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 15, 32),
                    width,
                    height,
                    true
                ).setAdjective(false)
                    .build()
            )
            if (duration > currentDuration) {
                list.add(
                    AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build()
                )
            }
        } else {
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 17, 32),
                    width,
                    height,
                    true
                )
                    .setMoveAction(EaseOutQuart(true, false, false, false))
                    .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                    .setStartConor(10)
                    .setEndConor(-10)
                    .setScale(0.9f, 0.9f)
                    .setStartX(-0.5f)
                    .setEndX(0.3f)
                    .build()
            )
            list.add(
                AnimationBuilder(
                    getDuration(currentDuration.toLong(), 15, 32),
                    width,
                    height,
                    true
                ).setAdjective(false)
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
        return 2
    }


    override fun blurBackground(): Boolean {
        return true
    }
}