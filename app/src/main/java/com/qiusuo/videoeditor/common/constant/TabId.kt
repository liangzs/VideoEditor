package com.qiusuo.videoeditor.common.constant

import androidx.annotation.StringDef

@StringDef(
    TabId.HOME,
    TabId.HOT_THEME,
    TabId.STUDIO
)

@Retention(AnnotationRetention.SOURCE)
annotation class TabId {
    companion object {
        const val HOME = "home"
        const val HOT_THEME = "hot_theme"
        const val STUDIO = "studio"
    }


}