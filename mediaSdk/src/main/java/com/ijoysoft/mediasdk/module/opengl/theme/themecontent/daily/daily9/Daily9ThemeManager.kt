package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily9

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.*
import com.ijoysoft.mediasdk.module.playControl.GifDecoder

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
class Daily9ThemeManager : BaseTimeThemeManager() {
    init {
        val themeWidgetInfos: MutableList<ThemeWidgetInfo> = ArrayList()
        themeWidgetInfos.add(ThemeWidgetInfo(0, 600))
        //480
        themeWidgetInfos.add(ThemeWidgetInfo(600, 1080))
        themeWidgetInfos.add(ThemeWidgetInfo(1080, 1560))
        themeWidgetInfos.add(ThemeWidgetInfo(1560, 2040))
    }

    override fun computeThemeCycleTime(): Int {
        return 4440
    }

    override fun blurBackground(): Boolean {
        return true
    }

    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample) {}
    override fun onDrawFrame(currenPostion: Int) {
        super.onDrawFrame(currenPostion)
        //        fireworkBoomDrawer.onDrawFrame(currenPostion);
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * case 0:
     * return 600;
     * case 1:
     * case 2:
     * case 3:
     * return 480;
     * case 4:
     * return 2400;
     * }
     */
    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        //        index = 4;
        currentDuration = 480
        when (index % createActions()) {
            0, 2, 4, 6 -> {
                currentDuration = 600
                //600ms
                //旋转缓慢进入
                list.add(
                    AnimationBuilder(
                        (currentDuration / 2), width, height, true
                    ).setScale(1.5f, 1.5f).setRotaCenter(-1f, 0f)
                        .setMoveAction(EaseOutCubic(true, false, true))
                        .setRotateAction(EaseOutPow())
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, -4f, 4f)
                        .setCoordinate(-0.2f, 0f, 0.2f, 0f).build()
                )
                if (duration > currentDuration) {
                    list.add(
                        AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                            .build()
                    )
                }
                //左出
                list.add(
                    AnimationBuilder(
                        (currentDuration / 2), width, height, true
                    ).setMoveAction(EaseInMultiple(5, true, false, false))
                        .setCoordinate(0.2f, 0f, -1.5f, 0f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 4f, -8f)
                        .setRotateAction(EaseInPow()).setRotaCenter(-1f, 0f)
                        .build()
                )
            }
            1, 3, 5 -> {
                //左进
                list.add(
                    AnimationBuilder(
                        (currentDuration / 2), width, height, true
                    ).setScale(1.5f, 1.5f).setRotaCenter(1f, 0f)
                        .setMoveAction(EaseOutCubic(true, false, true)).setMoveBlurRange(10f, 1f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, -4f, 4f)
                        .setCoordinate(0.2f, 0f, -0.2f, 0f).build()
                )
                if (duration > currentDuration) {
                    list.add(
                        AnimationBuilder((duration - currentDuration).toInt(), width, height, true)
                            .build()
                    )
                }
                //右出
                list.add(
                    AnimationBuilder(
                        (currentDuration / 2), width, height, true
                    ).setMoveAction(EaseInMultiple(5, true, false, false))
                        .setCoordinate(-0.2f, 0f, 2f, 0f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 4f, -4f)
                        .setMoveBlurRange(0f, 0f)
                        .setRotateAction(EaseInPow()).setRotaCenter(1f, 0f)
                        .build()
                )
            }
            else -> if (index and 1 == 1) {
                //弹簧效果，针对y轴的弹簧效果
                list.add(
                    AnimationBuilder(duration.toInt(), width, height, true).setScale(1.2f, 1.0f)
                        .setScaleAction(EaseOutMultiple(5, false, true, false))
                        .setMoveAction(SpringYEnter()).setIsSprintY(true).build()
                )
            } else {
                //弹簧效果，针对x轴的弹簧效果
                list.add(
                    AnimationBuilder(duration.toInt(), width, height, true).setScale(1.2f, 1.0f)
                        .setScaleAction(EaseOutMultiple(5, false, true, false))
                        .setMoveAction(SpringYEnter()).setIsSprintX(true).build()
                )
            }
        }
        return list
    }

    override fun createActions(): Int {
        return 20
    }


    override fun customCreateGifWidget(
        gifMipmaps: MutableList<MutableList<GifDecoder.GifFrame>>?,
        timeLineStart: Int
    ) {
        val gifOriginFilter1 = GifOriginFilter()
        gifOriginFilter1.setFrames(gifMipmaps!![0])
        gifOriginFilter1.setOnSizeChangedListener { width, height ->
            gifOriginFilter1.adjustScaling(width, height, AnimateInfo.ORIENTATION.LEFT_TOP, 0f, 2f)
        }
        gifOriginFilter1.onCreate()
        gifOriginFilter1.onSizeChanged(width, height)


        val gifOriginFilter2 = GifOriginFilter()
        gifOriginFilter2.setFrames(gifMipmaps[1])
        gifOriginFilter2.setOnSizeChangedListener { w, h ->
            gifOriginFilter2.adjustScaling(w, h, 0f, 0.5f, 1.5f, 1.5f)
        }
        gifOriginFilter2.onCreate()
        gifOriginFilter2.onSizeChanged(width, height)



        globalizedGifOriginFilters = listOf(gifOriginFilter1, gifOriginFilter2)
    }
}