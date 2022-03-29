package com.qiusuo.videoeditor.constant

import androidx.annotation.StringDef
import com.google.android.material.tabs.TabLayout

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