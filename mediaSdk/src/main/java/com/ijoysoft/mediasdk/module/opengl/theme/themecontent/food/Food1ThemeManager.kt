package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food

import android.content.res.Resources.Theme
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.opengl.theme.ternary

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
class Food1ThemeManager : BaseTimeThemeManager() {
    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return when (index) {
            0 -> ThemeWidgetInfo(0,2000,0,0, 2000);
            1 -> ThemeWidgetInfo(0,2000,0,2000, 4000);
            2 -> ThemeWidgetInfo(0,2000,0,4000, 6000);
            else -> {
                null
            }
        }
    }

    override fun computeThemeCycleTime(): Int {
        return 6000
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample) {
        when (index) {
            0 -> {
                widgetTimeExample.adjustScaling(0.7f, 0.8f, 3f);
                widgetTimeExample.setStayAction(ThemeWidgetHelper.springMove(widgetTimeExample.totalTime, AnimateInfo.ORIENTATION.BOTTOM))
            }
            1 -> {
                widgetTimeExample.adjustScaling(0.4f, -0.7f, (width < height).ternary(2f, 2.5f));
                widgetTimeExample.setStayAction(ThemeWidgetHelper.fade(true, widgetTimeExample.totalTime))
            }
            2 -> {
                widgetTimeExample.adjustScaling(-0.4f, 0.7f, (width < height).ternary(2f, 2.5f));
                widgetTimeExample.setStayAction(ThemeWidgetHelper.springMove(widgetTimeExample.totalTime, AnimateInfo.ORIENTATION.BOTTOM))
            }
        }
    }


    override fun blurBackground(): Boolean {
        return true
    }

    override fun isTextureInside(): Boolean {
        return true
    }


    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * //左右移动
     * case 0:
     * return 600;
     * case 1:
     * return 400;
     * case 3:
     * case 14:
     * case 15:
     * case 16:
     * case 2:
     * return 1200;
     * //缩放
     * case 7:
     * case 8:
     * case 9:
     * case 10:
     * case 11:
     * case 12:
     * return 400;
     * case 13:
     * //缩放平移
     * return 800;
     * case 17:
     * //左右移动，旋转
     * return 800;
     * case 26:
     * return 800;
     * case 27:
     * return 1200;
     * case 28:
     * return 1500;
     * case 29:
     * return 2000;
     * default:
     * return 600;
     * }
     */
    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        currentDuration = 600
        when (index % createActions()) {
            0 -> {
                currentDuration = 600
                list.addAll(EvaluatorHelper.moveBLurLeft2Left(currentDuration.toLong(), duration, width, height))
            }
            1 -> {
                currentDuration = 400
                list.addAll(EvaluatorHelper.moveBlurRight2Right(currentDuration.toLong(), duration, width, height))
            }
            2 -> {
                currentDuration = 1200
                //动感模糊缩小，向上平移，动感模糊放大
                list.addAll(EvaluatorHelper.zoomOut2MoveTop2ZoomIn(currentDuration.toLong(), duration, width, height))
            }
            3 -> {
                currentDuration = 1200
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 3f, 0f)
                    .setMoveAction(EaseOutMultiple(5, false, true, false))
                    .setScale(1.7f, 1f).build())
                if (duration > currentDuration) {
                    list.add(AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build())
                }
                //@TODO 后续换弧度出场
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setMoveAction(EaseInCubic(true, false, false))
                    .setCoordinate(0f, 0f, -2f, 0f)
                    .build())
            }
            4 -> {
                currentDuration = 1200
                //右进，径向模糊缩小
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setMoveAction(EaseOutCubic(true, false, false))
                    .setCoordinate(2f, 0f, 0f, 0f)
                    .setScale(1.4f, 1.4f)
                    .build())
                if (duration > currentDuration) {
                    list.add(AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build())
                }
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setRatioBlurRange(0f, 1f, true)
                    .setScale(1.4f, 0.8f)
                    .build())
            }
            5 -> {
                //径向模糊缩小，旋转
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setScaleAction(EaseOutCubic())
                    .setRatioBlurRange(1f, 0f, true)
                    .setScale(1.7f, 1.0f)
                    .build())
                if (duration > currentDuration) {
                    list.add(AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build())
                }
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0f, 90f)
                    .build())
            }
            6 -> {
                //旋转,径向模糊放大
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 90f, 0f)
                    .build())
                if (duration > currentDuration) {
                    list.add(AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build())
                }
                list.add(AnimationBuilder((currentDuration * 0.5).toInt(), width, height, true)
                    .setScaleAction(EaseInCubic())
                    .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0f, 1f)
                    .setScale(1.0f, 1.7f)
                    .build())
            }
            7, 8, 9, 10, 11, 12, 18, 19, 20 -> {
                currentDuration = 400
                //径向模糊缩小*径向模糊缩小
                list.addAll(EvaluatorHelper.zoomOut2ZoomOut(duration, width, height))
            }
            13, 26 -> {
                currentDuration = 800
                //缩小上移放大
                list.addAll(EvaluatorHelper.zoomOut2MoveTop2ZoomIn(currentDuration.toLong(), duration, width, height))
            }
            14 -> {
                currentDuration = 1200
                //左进左出
                list.addAll(EvaluatorHelper.moveBLurLeft2Left(currentDuration.toLong(), duration, width, height))
            }
            15 -> {
                currentDuration = 1200
                list.addAll(EvaluatorHelper.moveBlurRight2Right(currentDuration.toLong(), duration, width, height))
            }
            16 -> {
                currentDuration = 1200
                //左进左出
                list.addAll(EvaluatorHelper.moveBLurLeft2Left(currentDuration.toLong(), duration, width, height))
            }
            17 -> {
                currentDuration = 800
                //右进大角度旋转右出
                list.add(AnimationBuilder((currentDuration * 0.5f).toInt(), width, height, true)
                    .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0f)
                    .setMoveAction(EaseOutCubic(true, false, true))
                    .setRotaCenter(0.3f, -0.7f)
                    .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2f, 1f)
                    .setRotateAction(EaseCustom { x: Float -> Easings.smoothStepDelay(x) })
                    .setCoordinate(0.6f, 0f, 0f, 0f).build())
                if (duration > currentDuration) {
                    list.add(AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                        .build())
                }
                list.add(AnimationBuilder((currentDuration * 0.5f).toInt(), width, height, true)
                    .setRotaCenter(0.3f, -0.7f)
                    .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1f, -2f)
                    .setRotateAction(EaseCustom { x: Float -> Easings.smoothStepAhead(x) })
                    .setMoveAction(EaseInCubic(true, false, false))
                    .setCoordinate(0.0f, 0f, 2f, 0f).build())
            }
            21 ->                 //缩小-放大
                list.addAll(EvaluatorHelper.zoomOut2ZoomIn(duration, width, height))
            22, 23, 24, 25 -> list.add(AnimationBuilder(duration.toInt(), width, height, true)
                .setScale(0.8f, 1.0f)
                .build())
            27, 28, 29 -> list.addAll(EvaluatorHelper.zoomOut2ZoomIn(duration, width, height))
        }
        return list
    }

    override fun createActions(): Int {
        return 30
    }

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        val vertex = globalizedGifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.TOP,
            (width < height).ternary(-0.6f, -0.7f), (width < height).ternary(2.8f,
                (width == height).ternary(3.0f, 2.2f)))
        AFilter.vertextMove(vertex, 0.1f, AnimateInfo.ORIENTATION.TOP);
        globalizedGifOriginFilters[0].vertex = vertex;


    }

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
        }
    }


}