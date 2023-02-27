package com.qiusuo.videoeditor.common.bean

import android.os.Build
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum
import com.qiusuo.videoeditor.BuildConfig
import java.io.Serializable
import java.util.*

data class ThemeEntity(var resName: String?, val resRequestPath: String?,
                       val mThemeEnum: ThemeEnum?) : Serializable {
    var index = 0
    var name: String? = null
    var path: String? = null
    var zipPath: String? = null

    /**
     * 音乐文件开放出来，不采用hashcode的隐藏文件做法
     * 并且音频文件放置sd中，开发媒体数据
     */
    private var musicDownPath: String? = null
    var musicLocalPath: String? = null
    var size = 0
    var musicDuration = 0
    var resLocalPath: String? = null
    var musicName: String? = null
    var themeEnum: ThemeEnum? = null
    var themeConstanType = 0

    /**
     * 在hot分类下的
     */
    var isHot = false


    constructor() : this(null,null,null) {}

    fun getMusicDownPath(): String? {
        return if (BuildConfig.VERSION_CODE < Build.VERSION_CODES.M) {
            musicDownPath!!.replace(" ", "%20")
        } else musicDownPath
    }

    fun setMusicDownPath(musicDownPath: String?) {
        this.musicDownPath = musicDownPath
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val entity = o as ThemeEntity
        return themeEnum == entity.themeEnum && index == entity.index
    }

    override fun hashCode(): Int {
        return Objects.hash(themeEnum)
    }
}