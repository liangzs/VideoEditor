package com.ijoysoft.mediasdk.module.opengl.theme.action

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType

class TimeVideoExample : IAction {
    override fun prepare() {
    }

    override fun drawFrame() {
    }

    override fun drawMatrixFrame() {
    }

    override fun seek(currentDuration: Int) {
    }

    override fun drawWiget() {
    }

    override fun initWidget() {
    }

    override fun drawLast() {
    }

    override fun init(bitmap: Bitmap?, width: Int, height: Int) {
    }

    override fun init(bitmap: Bitmap?, tempBit: Bitmap?, mimaps: MutableList<Bitmap>?, width: Int, height: Int) {
    }

    override fun init(mediaItem: MediaItem?, width: Int, height: Int) {
    }

    override fun getStatus(): ActionStatus {
        return ActionStatus.STAY
    }

    override fun initFilter(filterType: MagicFilterType?, width: Int, height: Int) {
    }

    override fun onDestroy() {
    }

    override fun getTexture(): Int {
        return 0
    }

    override fun getConor(): Int {
       return 0
    }

    override fun setPreTeture(listTexture: MutableList<Int>?, listCornor: MutableList<Int>?, pos: MutableList<FloatArray>?, listAfilters: MutableList<GPUImageFilter>?) {
    }

    override fun setPreTeture(texture: Int, aFilter: AFilter?) {

    }

    override fun setPreTeture(texture: Int) {

    }

    override fun setPreTeture(texture: Int, frame: Int, frameTexture: Int) {
    }

    override fun getPos(): FloatArray? {
        return null;
    }

    override fun getEnterProgress(): Float {
        return 0f
    }


    override fun getEnterTime(): Int {
        return 0
    }

    override fun drawFrameIndex() {
    }

    override fun setFilter(filter: GPUImageFilter?) {
    }

    override fun drawFramePreview() {
    }

    override fun getTotalTime(): Int {
        return 0
    }

    override fun updateVideoTexture(texture: Int) {
    }
}