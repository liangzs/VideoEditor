package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday21

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveActionWrapper
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager
import java.util.*

class HD21ThemeManager : Common15ThemeManager() {
    private val bgImageFilter: ImageOriginFilter = ImageOriginFilter()



    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample) {
        when (index) {
            0 -> widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, 1f)
            1,2 -> if (width > height) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0f, 5f)
            } else if (height > width) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, -0.2f, 5f)
            } else {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, -0.4f, 5f)
            }
            3,4 -> widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1f)
            5,6 -> if (width > height) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0f, 5f)
            } else if (height > width) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0.2f, 5f)
            } else {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT, 0.4f, 5f)
            }
            7 -> widgetTimeExample.adjustScaling(0f, 0.5f, 1f)

        }
        if (index in 1 until 7) {
            val center = widgetTimeExample.centerAfterScaling
            widgetTimeExample.setEnterAction(AnimationBuilder(400).setScale(1.5f, 0.8f).setStartX(center[0]).setStartY(center[1]))
            widgetTimeExample.setStayAction(AnimationBuilder(1600).setScale(0.8f,1f))
            widgetTimeExample.setOutAction(AnimationBuilder(400).setScale(1f,0f).setEndX(center[0]).setEndY(center[1]))
        }
    }

    override fun customCreateWidget(widgetMipmaps: List<Bitmap>): Boolean {
        super.customCreateWidget(widgetMipmaps)
        val render = ParticleBuilder().setParticleType(ParticleType.RANDOM)
            .setListBitmaps(
                listOf(widgetMipmaps[5])
            )
            .setParticleCount(32)
            .setSizeFloatingStart(0.1f)
            .setParticleSizeRatio(0.015f)
            .build()
        renders.add(TimeThemeRenderContainer(listOf(ThemeWidgetInfo(0, 4800)), render))

        //blood
        createWidgetTimeExample(ThemeWidgetInfo(0, 4800, 0, 0, 4800).also { it.bitmap = widgetMipmaps[0] })
        //left hand in rightBottom
        for (i in intArrayOf(1,2,4)) {
            createWidgetTimeExample(ThemeWidgetInfo(400, 1600, 400, 0, 2400).also { it.bitmap = widgetMipmaps[i] }).also {
//                it.setEnterAction(AnimationBuilder(400).setScale(1.5f, 0.8f))
//                it.setStayAction(AnimationBuilder(1600).setScale(0.8f,1f))
//                it.setOutAction(AnimationBuilder(400).setScale(1f,0f))
            }
            createWidgetTimeExample(ThemeWidgetInfo(400, 1600, 400, 2400, 4800).also { it.bitmap = widgetMipmaps[i] }).also {
//                it.setEnterAction(AnimationBuilder(400).setScale(1.5f, 0.8f))
//                it.setStayAction(AnimationBuilder(1600).setScale(0.8f,1f))
//                it.setOutAction(AnimationBuilder(400).setScale(1f,0f))
            }
        }
        createWidgetTimeExample(ThemeWidgetInfo(1600, 1600, 1600, 0, 4800)
            .also { it.bitmap = widgetMipmaps[3] }).also {
                it.setEnterAction(AnimationBuilder(1600).setScale(1.4f, 1f))
                it.setStayAction(AnimationBuilder(1600).setScale(1f, 1.4f))
                it.setOutAction(AnimationBuilder(1600).setScale(1.4f, 1f))
        }





        return true
    }

    override fun computeThemeCycleTime(): Int {
        return 4800
    }





    override fun onDrawFrame(currenPostion: Int) {
        bgImageFilter.draw()
        super.onDrawFrame(currenPostion)
    }

    override fun onDestroy() {
        super.onDestroy()
        bgImageFilter.onDestroy()
    }

    override fun initBackgroundTexture() {
        when (ConstantMediaSize.ratioType) {
            RatioType._16_9, RatioType._4_3 -> bgImageFilter.setBackgroundTexture(
                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_bg169")
            )
            RatioType._3_4, RatioType._9_16 -> bgImageFilter.setBackgroundTexture(
                BitmapUtil.decriptImage(
                    ConstantMediaSize.themePath + "/holiday21_bg916"
                )
            )
            RatioType._1_1 -> bgImageFilter.setBackgroundTexture(
                BitmapUtil.decriptImage(
                    ConstantMediaSize.themePath + "/holiday21_bg11"
                )
            )
            else -> bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday21_bg916"))
        }
    }

    override fun onSurfaceCreated() {
        bgImageFilter.create()
        super.onSurfaceCreated()
    }

    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        bgImageFilter.onSizeChanged(width, height)
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
    }



    override fun blurBackground() = false

    override fun getPureColor() = ""


    /**
     * 将图片缩小
     */
    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate>? {
        val list = ArrayList<BaseEvaluate>()
        when (index % createActions()) {
            0, 3, 9 ->                 //右下角弹动，弹动角度归正
                list.add(
                    AnimationBuilder(
                        duration.toInt(), width, height, true
                    )
                        .setRotaCenter(0.8f, 0.8f)
                        .setRotateAction(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15Conor(
                                x,
                                2f
                            )
                        })
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setIsSprintY(true)
                        .setMoveAction(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15(
                                x,
                                0.1f,
                                -1f
                            )
                        })
                        .setMoveActionX(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15X(
                                x,
                                -1f
                            )
                        })
                        .setIsSprintX(true) //模糊
                        .setCusAction(EaseOutMultiple(5))
                        .setCusStartEnd(10f, 0f)
                        .setCusAction2(EaseOutMultiple(5))
                        .setCusStartEnd2(90f, 45f)
                        .setScale(0.8f, 0.8f)
                        .build()
                )
            1 -> {
                //左进
                list.add(
                    AnimationBuilder(
                        duration.toInt(), width, height, true
                    )
                        .setCusAction(EaseOutMultiple(5))
                        .setCusStartEnd(10f, 0f)
                        .setIsSprintX(true)
                        .setMoveActionX(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15(
                                x,
                                0.6f,
                                -1f
                            )
                        })
                        .setScale(0.8f, 0.8f)
                        .build()
                )
                //右进
                list.add(
                    AnimationBuilder(
                        duration.toInt(), width, height, true
                    )
                        .setCusAction(EaseOutMultiple(5))
                        .setCusStartEnd(10f, 0f)
                        .setIsSprintX(true)
                        .setMoveActionX(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15(
                                x,
                                0.6f,
                                1f
                            )
                        })
                        .setScale(0.8f, 0.8f)
                        .build()
                )
            }
            2 -> list.add(
                AnimationBuilder(
                    duration.toInt(), width, height, true
                )
                    .setCusAction(EaseOutMultiple(5))
                    .setCusStartEnd(10f, 0f)
                    .setIsSprintX(true)
                    .setMoveActionX(EaseCustomNoOffset { x: Float ->
                        Easings.easeSpringCommon15(
                            x,
                            0.6f,
                            1f
                        )
                    })
                    .setScale(0.8f, 0.8f)
                    .build()
            )
            4, 10, 11 ->                 //左下角弹动，带角度弹动，角度中心点为左下角
                list.add(
                    AnimationBuilder(
                        duration.toInt(), width, height, true
                    )
                        .setRotaCenter(0.8f, 0.8f)
                        .setRotateAction(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15Conor(
                                x,
                                2f
                            )
                        })
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setIsSprintY(true)
                        .setMoveAction(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15(
                                x,
                                0.1f,
                                1f
                            )
                        })
                        .setMoveActionX(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15X(
                                x,
                                1f
                            )
                        })
                        .setIsSprintX(true) //模糊
                        .setCusAction(EaseOutMultiple(5))
                        .setCusStartEnd(10f, 0f)
                        .setCusAction2(EaseOutMultiple(5))
                        .setCusStartEnd2(90f, 45f)
                        .setScale(0.8f, 0.8f)
                        .build()
                )
            5 -> {
                //径向模糊放大
                list.add(
                    AnimationBuilder((duration * 0.7).toInt(), width, height, true)
                        .setScaleAction(EaseOutCubic())
                        .setRatioBlurRange(1f, 0f, false)
                        .setScale(0.8f, 1.2f)
                        .build()
                )
                list.add(AnimationBuilder((duration * 0.3).toInt(), width, height, true)
                    .setScale(0.8f, 0.8f).build())
            }
            6 -> {
                //缩放进来，z轴spring旋转
                list.add(
                    AnimationBuilder((duration * 0.6f).toInt(), width, height, true)
                        .setIsSprintX(true)
                        .setScaleAction(EaseOutCubic())
                        .setScale(1.04f, 0.8f)
                        .setMoveActionX(object : IMoveActionWrapper() {
                            override fun action(
                                duration: Float,
                                start: Float,
                                end: Float
                            ): FloatArray {
                                //定义自定域在0-1.5之间
                                val framesNum =
                                    Math.ceil((duration * ConstantMediaSize.FPS / 1000f).toDouble())
                                        .toInt()
                                val frames = FloatArray(framesNum)
                                var x: Float
                                var i = 0
                                while (i < framesNum) {
                                    x = i * 1.0f / (framesNum - 1)
                                    x = x * 0.4f + 0.1f
                                    frames[i] = Easings.easeSpringCommon15A(x, 0.1f, 1f)
                                    i++
                                }
                                LogUtils.v("", Arrays.toString(frames))
                                return frames
                            }
                        })
                        .build()
                )
                list.add(
                    AnimationBuilder((duration * 0.4f).toInt(), width, height, true)
                        .setRotateAction(object : IMoveActionWrapper() {
                            override fun action(
                                duration: Float,
                                start: Float,
                                end: Float
                            ): FloatArray {
                                //定义自定域在0.6~1之间
                                val framesNum =
                                    Math.ceil((duration * ConstantMediaSize.FPS / 1000f).toDouble())
                                        .toInt()
                                val frames = FloatArray(framesNum)
                                var x: Float
                                var i = 0
                                while (i < framesNum) {
                                    x = i * 1.0f / (framesNum - 1)
                                    x = x * 0.4f + 0.6f
                                    frames[i] = Easings.easeSpring(x) * 0.8f
                                    i++
                                }
                                LogUtils.v("", Arrays.toString(frames))
                                return frames
                            }
                        })
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setScale(0.8f, 0.8f)
                        .build()
                )
            }
            7 ->                 //z轴srping 缩放进入
                //缩放进来，z轴spring旋转
                list.add(
                    AnimationBuilder(
                        duration.toInt(), width, height, true
                    )
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setScaleAction(EaseOutCubic())
                        .setScale(1.36f, 0.8f)
                        .setRotateAction(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15B(
                                x,
                                5f,
                                1f
                            )
                        })
                        .build()
                )
            8 ->                 // 从上下来弹动，
                list.add(
                    AnimationBuilder(
                        duration.toInt(), width, height, true
                    )
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setIsSprintY(true)
                        .setMoveAction(EaseCustomNoOffset { x: Float ->
                            Easings.easeSpringCommon15(
                                x,
                                1f,
                                -1f
                            )
                        }) //模糊
                        .setScale(0.8f, 0.8f)
                        .setCusAction(EaseOutMultiple(5))
                        .setCusStartEnd(10f, 0f)
                        .setCusAction2(EaseOutMultiple(5))
                        .setCusStartEnd2(90f, 90f)
                        .build()
                )
        }
        return list
    }
}