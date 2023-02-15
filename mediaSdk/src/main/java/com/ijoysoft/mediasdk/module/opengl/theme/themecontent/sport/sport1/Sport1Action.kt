package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport1

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.SinWaveAction
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation

class Sport1Action: BaseBlurThemeTemplate {




    @Keep
    constructor(totalTime: Int, width: Int, height: Int) : super(totalTime, width, height, true, true)


    init {
//        this.width = width;
//        this.height = height;
//        stayAction = initStayAction();
//        enterAnimation = initEnterAnimation();
//        outAnimation = initOutAnimation();
//
//        setZView(initZView())
    }



    override fun initStayAction(): BaseEvaluate {
        val res = StayScaleAnimation(DEFAULT_STAY_TIME, false, 1f, 0.1f, 1.1f)
        res.setZView(0f)
        return res
    }

    override fun initWidget() {
        super.initWidget()

        val widget = BaseThemeExample(totalTime, totalTime, 40, 40, true)
        widget.setZView(0f)
        widgets = arrayOf(widget)
    }



//    var widgetsMetas: Array<FloatArray>? = null
//
//
//
//    override fun getWidgetsMeta(): Array<FloatArray> {
//        if (widgetsMetas == null) {
//            widgetsMetas = arrayOf(
//                floatArrayOf(0f, 0f, 1f, 1f, 1f),
//            )
//        }
//        return widgetsMetas!!
//    }

    override fun init(
        bitmap: Bitmap?,
        tempBit: Bitmap?,
        mimaps: MutableList<Bitmap>?,
        width: Int,
        height: Int
    ) {
        super.init(bitmap, tempBit, mimaps, width, height)
        widgets[0].init(mimaps!![0], width, height)
        val scale = if (width < height) 2f else 3f
        val cube = widgets[0].adjustScalingWithoutSettingCube(width, height, widgets[0].bitmap.width, widgets[0].bitmap.height, AnimateInfo.ORIENTATION.TOP, 0f, scale)
        val center = AFilter.getCubeCenter(cube)
        center[1] += 0.1f
        widgets[0].adjustScaling(width, height, widgets[0].bitmap.width, widgets[0].bitmap.height, 0f, 0f, scale, scale);

        widgets[0].enterAnimation =
            AnimationBuilder(totalTime)
                .setStayPos(center)
                .setScale(1f, 1.1f)
                .setScaleAction(SinWaveAction(1000, false, true, false, false))
                .setIsNoZaxis(true)
                .build()
        widgets[0].stayAction = AnimationBuilder(40).setStayPos(center).setIsNoZaxis(true).build()
        widgets[0].outAnimation = AnimationBuilder(40).setStayPos(center).setIsNoZaxis(true).build()
    }




}