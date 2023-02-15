package com.ijoysoft.mediasdk.module.opengl.theme

import android.opengl.GLES20
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.MatrixUtils
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter
import com.ijoysoft.mediasdk.module.opengl.filter.NoFilter
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.particle.PAGOneBgParticle
import com.ijoysoft.mediasdk.module.playControl.IRender
import org.libpag.PAGPlayer
import org.libpag.PAGSurface

class ThemePagParticleDrawerProxy(var width: Int, var height: Int): IRender {


    private var fTexture = IntArray(1)

    private var timeParticle: Long = 0

    private var fFrame = IntArray(1)


    private var pagParticleDuration: Long = 0


    private var mParticlePAGPlayer: PAGPlayer? = null


    private var pagParticleFilter: AFilter? = null


    //pag 粒子
    var pagParticle: PAGNoBgParticle? = null
        private set


    var rotateVertical: Boolean = false


    override fun onSurfaceCreated() {
    }

    override fun onSurfaceChanged(
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int,
        screenWidth: Int,
        screenHeight: Int
    ) {
        this.width = width
        this.height = height
        createFBo(width, height)
        if (mParticlePAGPlayer != null) {
            mParticlePAGPlayer!!.release()
            mParticlePAGPlayer = null
        }
        checkPagParticlePlay(width, height)
        pagParticle?.let {
            it.onSurfaceChanged(
                offsetX,
                offsetY,
                width,
                height,
                screenWidth,
                screenHeight
            )
            mParticlePAGPlayer!!.composition = it.pagFile
            it.pagFilePrepare()
        }
    }

    override fun onDestroy() {
        if (mParticlePAGPlayer != null) {
            mParticlePAGPlayer!!.release()
        }
        if (fTexture[0] != -1) {
            deleteFrameBuffer()
        }
    }

    /**
     * 创建pag的pagComposite、surface信息
     *
     * @param mWidth
     * @param mHeight
     */
    private fun checkPagParticlePlay(mWidth: Int, mHeight: Int) {
        if (mParticlePAGPlayer == null) {
            mParticlePAGPlayer = PAGPlayer()
            val pagSurface = PAGSurface.FromTexture(fTexture[0], mWidth, mHeight)
            mParticlePAGPlayer!!.surface = pagSurface
            pagParticleFilter = NoFilter().apply {
                create()
                if (rotateVertical) {
                    matrix = MatrixUtils.rotate(matrix, 180f)
                }
                textureId = fTexture[0]
            }
        }
    }



    /**
     * 挂载的pag数据
     */
    fun drawPagParticle(textureId: Int = -1) {
        pagParticle?.let {
            pagParticleDuration = it.pagFile!!.duration()
            if (timeParticle == 0L) {
                timeParticle = System.currentTimeMillis()
            }
            val playTime: Long = (System.currentTimeMillis() - timeParticle) * 1000
            val progress: Float = playTime % pagParticleDuration * 1.0f / pagParticleDuration
            LogUtils.v("MediaDrawer", "progress:$progress")
            mParticlePAGPlayer!!.progress = progress.toDouble()
            if (it is PAGOneBgParticle) {
                it.updateBgTeture(textureId)
            }
            mParticlePAGPlayer!!.flush()
            pagParticleFilter!!.draw()
        }
    }


    fun switchParticle(pag: PAGNoBgParticle?) {

        //设置pag素材粒子
        //设置pag素材粒子
        pagParticle = pag
        pag?.let {
            checkPagParticlePlay(width, height)
            it.onSurfaceChanged(
                0,
                0,
                width,
                height,
                0,
                0
            )
            mParticlePAGPlayer!!.composition = it.pagFile
            it.pagFilePrepare()
        }
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
        if (fTexture.get(0) == -1) {
            GLES20.glGenTextures(1, fTexture, 0)
        }
        for (i in 0 until 1) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture.get(i))
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, null
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
            )
        }
    }

    private fun deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0)
        /* 9.0以上的手机问题，纹理不进行回收了！ */
        GLES20.glDeleteTextures(1, fTexture, 0)
        fTexture[0] = -1
    }

}