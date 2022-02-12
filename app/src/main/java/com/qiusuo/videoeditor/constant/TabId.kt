package com.qiusuo.videoeditor.constant

import androidx.annotation.StringDef
import com.google.android.material.tabs.TabLayout

@StringDef(
    TabId.HOME,
    TabId.ACGN,
    TabId.DISCOVERY,
    TabId.MINE
)

@Retention(AnnotationRetention.SOURCE)
annotation class TabId {
    companion object{
        const val HOME="home"
        const val MINE="mine"
        const val DISCOVERY="discovery"
        const val ACGN="acgn"
    }



}