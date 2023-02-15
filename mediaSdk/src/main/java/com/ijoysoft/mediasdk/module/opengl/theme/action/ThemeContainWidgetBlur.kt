package com.ijoysoft.mediasdk.module.opengl.theme.action

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.common.utils.LogUtils

private val <A, B, C> Triple<A, B, C>.sum: Int
    get() {
        return (first as Int) + (second as Int) + (third as Int)
    }

/**
 * 主题挂载默认类，主题的片段逻辑移到主题manager管理类中
 */
class ThemeContainWidgetBlur : BaseBlurThemeExample(), IThemeClipAction {

    var widgets: MutableList<BaseThemeExample> = ArrayList();


    init {
        isNoZaxis = true
        setZView(0f)
    }

    override fun creatClip(duration: Long, durationTriple: Triple<Int, Int, Int>) {
        createThemeExample(duration.toInt(), durationTriple.first, durationTriple.second, durationTriple.third)
    }

    override fun createWidget(array: Array<Triple<Int, Int, Int>>, mimaps: List<Bitmap>, width: Int, height: Int) {
        for (index in mimaps.indices) {
            widgets.add(BaseThemeExample(totalTime, array[index].first, array[index].second, array[index].third).apply {
                this.width = width
                this.height = height
                setZView(0f)
                isNoZaxis = true
                init(mimaps[index], width, height)
            })
        }
    }

    /**
     * 主体的动作
     */
    override fun clipAction(actions: Array<BaseEvaluate?>?) {
        actions?.let {
            setEnterAnimation(it[0])
            setStayAction(it[1])
            setOutAnimation(it[2])
        }

    }

    /**
     * 控件动作
     */
    override fun widgetAction(list: Array<Array<BaseEvaluate?>?>?) {
        for (index in widgets.indices) {
            widgets.get(index).apply {
                list?.get(index)?.let {
                    it[0]?.reset()
                    it[1]?.reset()
                    it[2]?.reset()
                    setEnterAnimation(it[0])
                    setStayAction(it[1])
                    setOutAnimation(it[2])
                }

            }
        }
    }

    override fun getWidgetList(): MutableList<BaseThemeExample> {
        return widgets
    }

    override fun drawWiget() {
        super.drawWiget()
        for (wiget in widgets) {
            LogUtils.v(javaClass.simpleName, "status:" + wiget.status);
            wiget.drawFrame()
        }
    }

    ///**
    // * 片段位置
    // */
    //override fun clipLocation(vertext: FloatArray) {
    //    setVertex(vertext)
    //}


    override fun onDestroy() {
        super.onDestroy()
        for (wiget in widgets) {
            wiget.onDestroy()
        }
    }
}