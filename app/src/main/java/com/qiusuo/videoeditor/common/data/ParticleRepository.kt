package com.ijoysoft.videoeditor.theme.manager

import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles
import com.ijoysoft.videoeditor.model.download.DownloadHelper
import com.ijoysoft.videoeditor.model.download.DownloadPath
import org.libpag.PAGFile

object ParticleRepository {
    /**
     * 请求地址
     */
    fun dealRequestPath(globalParticle: GlobalParticles): String {
        return DownloadPath.BASE_PATH + "/" + globalParticle.offsetPath
    }

    /**
     * 请求地址本地处处路径
     */
    fun dealSavePath(globalParticle: GlobalParticles): String {
        return DownloadHelper.getDownloadPath(DownloadPath.BASE_PATH + "/" + globalParticle.offsetPath)
    }

    /**
     * 缩略图地址
     */
    fun dealreSourcePath(globalParticle: GlobalParticles): String {
        return DownloadPath.BASE_PATH + "/" + globalParticle.onlineIconOffset
    }


    /**
     * 对线上的进行赋值
     */
    val globalParticle: Array<GlobalParticles> by lazy {
        GlobalParticles.values().also {
            it.filter {
                it.isOnline
            }.forEach {
                it.downPath = dealRequestPath(it)
                it.savePath = dealSavePath(it)
                it.onlineIconPath = dealreSourcePath(it)
            }
        }
    }

    /**
     * 单个下载数据初始
     */
    fun dealDownloadPath(globalParticle: GlobalParticles) {
        if (globalParticle.isOnline && globalParticle.downPath == null) {
            globalParticle.downPath = dealRequestPath(globalParticle);
        }
    }

}