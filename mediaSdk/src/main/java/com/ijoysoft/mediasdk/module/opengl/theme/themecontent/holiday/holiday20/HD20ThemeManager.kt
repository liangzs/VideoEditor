package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday20

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
class HD20ThemeManager : BaseTimeThemeManager() {

    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }

    //停留模式的实际状态
    val out = booleanArrayOf(true, true, false, true, false, false, true)
    //入场出场模式的实际状态为同一个状态，时间总计1000
    val enterRatio = intArrayOf(5, 1, 8, 6, 7, 2, 3)
    val outRatio = intArrayOf(5, 9, 2, 4, 3, 8, 7)
    val timeUnit = 100
    val fadeUnit = 0.1f

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample) {
        when (index) {
            0-> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0.75f, 4f)
            }
            1-> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, -0.3f, 3f)
            }
            2 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT, 0.5f, 4.5f)
            }
            3 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0.6f, 6f)
            }
            4 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT, -0.35f, 5f)
            }
            5 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0.8f, 7f)
            }
            6 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0.1f, 4.5f)
            }
            7 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, -0.5f, 6f)
            }
            8 -> {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, -0.5f, 5f)
            }
        }

    }
    override fun blurBackground(): Boolean {
        return true
    }

    override fun isTextureInside(): Boolean {
        return true
    }

    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        super.customCreateWidget(widgetMipmaps)
        val render = ParticleBuilder().setParticleType(ParticleType.RANDOM)
            .setListBitmaps(listOf(widgetMipmaps[1]))
            .setParticleCount(8)
            .setSizeFloatingStart(0.3f)
            .setParticleSizeRatio(0.03f)
            .build()
        renders.add(TimeThemeRenderContainer.createContainer(ThemeWidgetInfo(0, 10000), render))
        val widget = widgetMipmaps[0]

        //
        ThemeWidgetInfo(1000, 0, 1000, 0, 2000).apply {
            bitmap = widget
        }.let {
            createWidgetTimeExample(it)
        }
        ThemeWidgetInfo(1000, 0, 1000, 0, 2000).apply {
            bitmap = widget
        }.let {
            createWidgetTimeExample(it)
        }



        for (i in 0 until 7) {
            ThemeWidgetInfo(enterRatio[i] * timeUnit, 1000, outRatio[i] * timeUnit,
                0, 2000).apply {
                bitmap = widget
            }.let {
                createWidgetTimeExample(it)
            }
        }


        widgetRenders[0].setEnterAction(AnimationBuilder(1000).setFade(0f, 1f))
        widgetRenders[0].setOutAction(AnimationBuilder(1000).setFade(1f, 0f))
        widgetRenders[1].setEnterAction(AnimationBuilder(1000).setFade(1f, 0f))
        widgetRenders[1].setOutAction(AnimationBuilder(1000).setFade(0f, 1f))
        for (i in 0 until 7) {
            if(out[i]) {
                widgetRenders[i+2].setEnterAction(AnimationBuilder(enterRatio[i] * timeUnit).setFade(outRatio[i] * fadeUnit, 1f))
                widgetRenders[i+2].setStayAction(AnimationBuilder(1000).setFade(1f, 0f))
                widgetRenders[i+2].setOutAction(AnimationBuilder(outRatio[i] * timeUnit).setFade(0f, outRatio[i] * fadeUnit))
            } else {
                widgetRenders[i+2].setEnterAction(AnimationBuilder(enterRatio[i] * timeUnit).setFade(enterRatio[i] * fadeUnit, 1f))
                widgetRenders[i+2].setStayAction(AnimationBuilder(1000).setFade(0f, 1f))
                widgetRenders[i+2].setOutAction(AnimationBuilder(outRatio[i] * timeUnit).setFade(0f, enterRatio[i] * fadeUnit))
            }
        }


        return true
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return
     */
    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        list.add(
            AnimationBuilder((duration * 0.1).toInt(), width, height, true)
                .setScaleAction(EaseOutMultiple(8))
                .setRatioBlurRange(5f, 0f, true)
                .setScale(0.75f, 1.0f)
                .build()
        )
        list.add(AnimationBuilder((duration * 0.8).toInt(), width, height, true).build())
        list.add(
            AnimationBuilder((duration * 0.1).toInt(), width, height, true)
                .setScaleAction(EaseInMultiple(8))
                .setRatioBlurRange(0f, 5f, false)
                .setScale(1.0f, 1.5f)
                .build()
        )
        return list
    }

    override fun createActions(): Int {
        return 1
    }

    override fun computeThemeCycleTime(): Int {
        return 2000
    }
}