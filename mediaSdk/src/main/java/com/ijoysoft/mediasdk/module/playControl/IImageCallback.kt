package com.ijoysoft.mediasdk.module.playControl

import com.ijoysoft.mediasdk.module.entity.BGInfo

/**
 * 图片层渲染时的触发回调
 */
interface IThemeCallback {
    //图片类的片段初始化
    //    void imagePrepare();
    //重新初始化后预览效果,采用了previewFilter，使得片段居中显示
    fun imagePreparePreView()
    fun seekToImpl(seekPair: Pair<Int, Int>, renderPair: Triple<Boolean, Boolean,Boolean>)
    fun seekTheme(seekPair: Pair<Int, Int>, renderPair: Triple<Boolean, Boolean,Boolean>)
    fun updateBgInfo(bgInfo: BGInfo?)
}