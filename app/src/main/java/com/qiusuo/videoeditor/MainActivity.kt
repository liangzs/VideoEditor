package com.qiusuo.videoeditor

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TabHost
import com.gyf.immersionbar.ktx.immersionBar
import com.nan.xarch.bean.Tab
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.constant.TabId
import com.qiusuo.videoeditor.databinding.ActivityMainBinding
import com.qiusuo.videoeditor.module.home.HomeFragment
import com.qiusuo.videoeditor.widgegt.TabIndicatorView

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @TabId
    private var currentTabId = TabId.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystembar()
        updateTitle()
        initTab()
    }


    fun initSystembar() {
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(true)
            navigationBarColor(R.color.white)
            navigationBarDarkIcon(true)
        }
    }

    fun updateTitle() {
        val title = when (currentTabId) {
            TabId.HOME -> getString(R.string.page_home)
            TabId.MINE -> getString(R.string.page_mine)
            TabId.DISCOVERY -> getString(R.string.page_discovery)
            else -> ""
        }
        viewBinding.root
    }

    fun initTab() {
        val tabs = listOf<Tab>(
            Tab(TabId.HOT_THEME, getString(R.string.main_hot_theme), R.drawable.selector_btn_discovery, HomeFragment::class),
            Tab(TabId.HOME, getString(R.string.main_home), R.drawable.btn_home, HomeFragment::class),
            Tab(TabId.STUDIO, getString(R.string.main_studio), R.drawable.selector_btn_mine, HomeFragment::class)
        )
        viewBinding.fragmentTabHost.run {
            setup(this@MainActivity, supportFragmentManager, viewBinding.fragmentContainer.id)
            tabs.forEach {
                val (id, title, icon, fragmentClz) = it
                val tabSpec = newTabSpec(id).apply {
                    setIndicator(TabIndicatorView(this@MainActivity).apply {
                        viewBinding.tabIcon.setImageResource(icon)
                        viewBinding.tabTitle.text = title
                    })
                }
                addTab(tabSpec, fragmentClz.java, null)
            }
            setOnTabChangedListener { tabId ->
                currentTabId = tabId;
                updateTitle()
            }
        }

    }

}