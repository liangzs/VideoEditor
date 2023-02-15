package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas3

import android.opengl.GLES20
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType
import com.ijoysoft.mediasdk.module.opengl.transition.TurnPageFilter

class Christmas3ThemeManager : ThemeOpenglManager() {


    // 是否为纯色背景
    var isPureBg = false
    var blurLevel = -1 //模糊度(1-20)

    //        val background = PureColorFilter()
    val foreground = ImageOriginFilter().also {
        it.setOnSizeChangedListener { width, height ->
            if (width < height) {
                it.initTexture(
                    com.ijoysoft.mediasdk.common.utils.BitmapUtil.decriptImage(
                        com.ijoysoft.mediasdk.common.global.ConstantMediaSize.themePath + "/christmas3_fg_9_16"))
            } else if (height < width) {
                it.initTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas3_fg_16_9"))
            } else {
                it.initTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/christmas3_fg_1_1"))
            }
        }
    }


    /**
     * 当前照片位置
     */
    val index = 0


    override fun themeTransition(): TransitionType {
        return TransitionType.THEME_TURN_PAGE
    }


    override fun onDestroy() {
        super.onDestroy()
        //transitionFilter.onDestroy()
//            background.onDestroy()
        foreground.onDestroy()
    }


    fun setIsPureColor(isPureColor: Boolean, level: Int, value: FloatArray?) {
        isPureBg = isPureColor
        blurLevel = level
        this.rgba = value
    }


    /**
     * 当当前片段即将结束时，切入下一个场景的临时temp，当当前场景结束时，把temp上的坐标值赋值给下个场景
     * 在调用层判断是否为最后一个片段
     * transitionFilter后续加上片段切换时，转场也做切换动作
     *
     * @param mediaItem
     * @param index
     */
    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem, index: Int) {
        super.drawPrepare(mediaDrawer, mediaItem, index)
        (actionRender as BaseThemeExample).adjustImageScalingStretch(width, height)
    }


    override fun ratioChange() {
        super.ratioChange()
        //transitionFilter.changeRatio()
    }


    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        //transitionFilter.create()
//            background.create()
        foreground.create()
    }

    var offsetX: Int = 0;
    var offsetY: Int = 0

    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
        this.offsetX = offsetX
        this.offsetY = offsetY
        //createFBo(width, height)
        //transitionFilter.onSizeChanged(width, height)
//            background.onSizeChanged(width, height)

        //foreground.setRotation(180)
        foreground.onSizeChanged(width, height)

    }


    override fun onDrawFrame() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        super.onDrawFrame()
        foreground.draw()
    }

    /**
     * 一直用transitiontype 渲染
     */
    override fun getActionStatus(): ActionStatus {
        return ActionStatus.AlWAY_TRAN;
    }

    override fun checkSupportTransition(): Boolean {
        return true
    }


    override fun drawPrepareIndex(
        mediaItem: MediaItem?,
        index: Int,
        width: Int,
        height: Int
    ): IAction {
        return mediaItem!!.duration.toInt().let {
            BaseThemeExample(it, it, 0, 0)
        }
    }
}