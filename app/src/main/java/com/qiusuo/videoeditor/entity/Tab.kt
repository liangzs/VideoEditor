package com.qiusuo.videoeditor.entity

import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.constant.TabId
import kotlin.reflect.KClass

data class Tab(
    val id: String,
    val title:String,
    val icon:Int,
//    val fragmentClz:KClass<out BaseFragment(*)>

)