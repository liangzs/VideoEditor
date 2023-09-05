package com.qiusuo.videoeditor.module.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.nan.xarch.bean.BannerBean
import com.nan.xarch.bean.VideoBean
import com.nan.xarch.item.BannerViewData
import com.nan.xarch.item.LargeVideoViewData
import com.nan.xarch.item.VideoViewData
import com.qiusuo.videoeditor.base.BaseRecyclerViewModel
import com.qiusuo.videoeditor.base.BaseViewData
import com.qiusuo.videoeditor.common.room.Project
import com.qiusuo.videoeditor.common.bean.ThemeEntity
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.common.constant.VideoType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : BaseRecyclerViewModel() {
    override fun loadData(isLoadMore: Boolean, isReLoad: Boolean, page: Int) {
        viewModelScope.launch {
            delay(1000L)
            val viewDataList = mutableListOf<BaseViewData<*>>()
            if (!isLoadMore) {
                viewDataList.add(
                    BannerViewData(
                        BannerBean(
                            listOf(
                                "https://img1.baidu.com/it/u=2148838167,3055147248&fm=26&fmt=auto",
                                "https://img1.baidu.com/it/u=2758621636,2239499009&fm=26&fmt=auto",
                                "https://img2.baidu.com/it/u=669799662,2628491047&fm=26&fmt=auto"
                            )
                        )
                    )
                )
                for (i in 0..10) {
                    if (i != 0 && i % 6 == 0) {
                        viewDataList.add(
                            LargeVideoViewData(
                                VideoBean(
                                    "aaa",
                                    "我是标题",
                                    "xxx",
                                    "aaa",
                                    "up",
                                    10000L,
                                    VideoType.LARGE
                                )
                            )
                        )
                    } else {
                        viewDataList.add(
                            VideoViewData(
                                VideoBean(
                                    "aaa",
                                    "我是标题",
                                    "xxx",
                                    "aaa",
                                    "up",
                                    10000L,
                                    VideoType.NORMAL
                                )
                            )
                        )
                    }
                }
                postData(isLoadMore, viewDataList)
            } else {
                for (i in 0..10) {
                    if (i != 0 && i % 6 == 0) {
                        viewDataList.add(
                            LargeVideoViewData(
                                VideoBean(
                                    "aaa",
                                    "我是标题",
                                    "xxx",
                                    "aaa",
                                    "up",
                                    10000L,
                                    VideoType.LARGE
                                )
                            )
                        )
                    } else {
                        viewDataList.add(
                            VideoViewData(
                                VideoBean(
                                    "aaa",
                                    "我是标题",
                                    "xxx",
                                    "aaa",
                                    "up",
                                    10000L,
                                    VideoType.NORMAL
                                )
                            )
                        )
                    }
                }
                postData(isLoadMore, viewDataList)
            }
        }
    }


    var themeLiveData = MutableLiveData<List<ThemeEntity>>()
    fun loadLatestTheme() {
        viewModelScope.launch {
            LogUtils.i("thread", Thread.currentThread().name)
            delay(500)
            val list = listOf<ThemeEntity>(ThemeEntity(), ThemeEntity(), ThemeEntity(), ThemeEntity(), ThemeEntity())
            themeLiveData.postValue(list)
        }
    }

    fun loadTab(): List<String> {
        return (1..10).map {
            "group" + it.toString()
        }
    }

    fun loadDraft():List<Project>{
      return  (1..5).map {index-> Project().also { it.projectName="name$index" } }
    }

    @PageName
    override fun getPageName() = PageName.HOME
}
