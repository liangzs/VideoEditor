package com.qiusuo.videoeditor

import android.os.Bundle
import com.gyf.immersionbar.ktx.immersionBar
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.constant.TabId
import com.qiusuo.videoeditor.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @TabId
    private var currentTabId=TabId.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystembar()
        updateTitle()
    }


    fun initSystembar(){
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(true)
            navigationBarColor(R.color.white)
            navigationBarDarkIcon(true)
        }
    }

    fun updateTitle(){
        val title = when (currentTabId) {
            TabId.HOME -> getString(R.string.page_home)
            TabId.MINE -> getString(R.string.page_mine)
            TabId.DISCOVERY -> getString(R.string.page_discovery)
            else -> ""
        }
        viewBinding.root
    }

    fun initTab(){

    }

}