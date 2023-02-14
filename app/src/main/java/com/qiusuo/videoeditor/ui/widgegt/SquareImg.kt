package com.qiusuo.videoeditor.ui.widgegt

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by hayring on 2022/10/31
 * 正方形的img
 */
class SquareImg : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        //用默认方法测量
        super.onMeasure(widthSpec, heightSpec)
        //模式
        val heightMode = MeasureSpec.getMode(heightSpec)
        //高度
        val heightSize = MeasureSpec.getSize(heightSpec)
        //模式为限制最大值，且宽度超过了高度的限制值
        if (heightMode == MeasureSpec.AT_MOST && measuredWidth > heightSize) {
            //边长以高度为准
            setMeasuredDimension(heightSize, heightSize)
            return
        }
        //模式为指定值，且宽度超过了指定值
        if (heightMode == MeasureSpec.EXACTLY && heightSize < measuredWidth) {
            //边长以高度为准
            setMeasuredDimension(heightSize, heightSize)
            return
        }
        //边长以宽度为准
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}