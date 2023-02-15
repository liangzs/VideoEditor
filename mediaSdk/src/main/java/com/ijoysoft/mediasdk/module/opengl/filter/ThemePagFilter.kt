package com.ijoysoft.mediasdk.module.opengl.filter

import android.opengl.GLES20
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import org.libpag.PAGFile
import org.libpag.PAGPlayer
import org.libpag.PAGSurface

/**
 * 带时间轴显示的显示
 * 或者主题片段显示
 */
class ThemePagFilter(val info: ThemeWidgetInfo?) : NoFilter() {
    private val widgetInfo: ThemeWidgetInfo? = info
    private var mParticlePAGPlayer: PAGPlayer? = null
    var pagFile: PAGNoBgParticle? = null;
    private var pagDuration = 1L;

    //挂载纹理
    private var fTexture: IntArray? = IntArray(1);
    private val fFrame = IntArray(1)
    fun drawFrame(position: Int) {
        widgetInfo?.let { widget ->
            if (widget.isInRange(position)) {
                mParticlePAGPlayer!!.progress = (position - widget.startTime).toDouble() / pagDuration.toDouble()
                LogUtils.v("ThemePagFilter", "position:$position,widget.startTime:${widget.startTime},pagDuration:$pagDuration,progress:" + mParticlePAGPlayer!!.progress);
                mParticlePAGPlayer!!.flush()
                draw()
            }
        }
    }

    override fun onSizeChanged(width: Int, height: Int) {
        super.onSizeChanged(width, height)
        createFBo(width, height)
        createPagParticle(width, height)
        pagFile?.onSurfaceChanged(0, 0, width, height, width, height)
        pagFile?.pagFilePrepare()
    }

    /**
     * 创建pag资源
     */
    private fun createPagParticle(width: Int, height: Int) {
        mParticlePAGPlayer?.release()
        mParticlePAGPlayer = PAGPlayer()
        val pagSurface = PAGSurface.FromTexture(fTexture!![0], width, height)
        mParticlePAGPlayer?.setSurface(pagSurface)
        mParticlePAGPlayer?.composition = pagFile?.pagFile
        textureId = fTexture!![0]
    }

    /**
     * 创建FBO
     *
     * @param width
     * @param height
     */
    private fun createFBo(width: Int, height: Int) {
        deleteFrameBuffer()
        GLES20.glGenFramebuffers(1, fFrame, 0)
        genTextures(width, height)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    // 生成Textures
    private fun genTextures(width: Int, height: Int) {
        GLES20.glGenTextures(1, fTexture, 0)
        if (fTexture!![0] == -1) {
            GLES20.glGenTextures(1, fTexture, 0)
        }
        for (i in 0 until 1) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture!![i])
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, null)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        }
    }

    private fun deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0)
        /* 9.0以上的手机问题，纹理不进行回收了！ */GLES20.glDeleteTextures(1, fTexture, 0)
        for (i in 0 until 1) {
            fTexture!![i] = -1
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mParticlePAGPlayer?.release()
        deleteFrameBuffer()
    }

    fun setPagFile(pag: PAGFile?) {
        LogUtils.i(javaClass.simpleName, "setPagFile")
        this.pagFile = PAGNoBgParticle(pag)
        pagDuration = pag?.duration()!! / 1000
    }


}