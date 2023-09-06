package com.nan.xarch.bean

import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.TabId
import kotlin.reflect.KClass

data class Tab(
    @TabId
    val id: String,
    val title: String,
    val icon: Int,
    val fragmentClz: KClass<out BaseFragment<*>>?
)