package com.ijoysoft.mediasdk.module.opengl.theme.action

import android.graphics.Bitmap
import java.time.Duration

/**
 * 通用主题片段动作
 */
interface IThemeClipAction {
    /**
     * 创建片段
     */
    fun creatClip(duration: Long, durationTriple: Triple<Int, Int, Int>)

    /**
     * 创建控件
     */
    fun createWidget(array: Array<Triple<Int, Int, Int>>, mimaps: List<Bitmap>,width:Int,height:Int)

    /**
     * 主体的动作,固定三组
     */
    fun clipAction(actions: Array<BaseEvaluate?>?)

    /**
     * 控件动作
     */
    fun widgetAction(list: Array<Array<BaseEvaluate?>?>?)


    fun getWidgetList(): List<BaseThemeExample>{
        return emptyList()
    }
    ///**
    // * 主题位置
    // */
    //fun clipLocation(vertext: FloatArray)

    ///**
    // * 片段位置
    // */
    //fun widgetLocation(array: Array<FloatArray?>)
}