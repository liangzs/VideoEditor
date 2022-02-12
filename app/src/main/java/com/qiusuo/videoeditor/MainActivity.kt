package com.qiusuo.videoeditor

import android.os.Bundle
import android.widget.TabHost
import com.gyf.immersionbar.ktx.immersionBar
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.constant.TabId
import com.qiusuo.videoeditor.databinding.ActivityMainBinding
import com.qiusuo.videoeditor.entity.Tab

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @TabId
    private var currentTabId = TabId.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystembar()
        updateTitle()
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
            Tab(
                TabId.HOME,
                getString(R.string.page_home),
                R.drawable.selector_btn_home,
                BaseFragment::class
            ),
            Tab(
                TabId.ACGN,
                getString(R.string.page_acgn),
                R.drawable.selector_btn_acgn,
                BaseFragment::class
            ),
            Tab(
                TabId.DISCOVERY,
                getString(R.string.page_discovery),
                R.drawable.selector_btn_discovery,
                BaseFragment::class
            ),
            Tab(
                TabId.MINE,
                getString(R.string.page_mine),
                R.drawable.selector_btn_mine,
                BaseFragment::class
            )
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
            }
        }
    }

}