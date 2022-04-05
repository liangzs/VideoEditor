package com.qiusuo.videoeditor.base

import com.qiusuo.videoeditor.constant.PageName


/**
 * 获取页面名称通用接口
 */
interface IGetPageName {

    @PageName
    fun getPageName(): String

}
