package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.nan.xarch.bean.Tab
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.common.constant.TabId
import com.qiusuo.videoeditor.databinding.ActivityMainBinding
import com.qiusuo.videoeditor.ui.widgegt.TabIndicatorView
import com.qiusuo.videoeditor.ui.widgegt.guide.util.ScreenUtils
import com.qiusuo.videoeditor.util.dp


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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(false)
            navigationBarDarkIcon(true)
        }
        ConstantMediaSize.screenWidth = ScreenUtils.getScreenWidth(this);
        viewBinding.fragmentContainer
    }

    private fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    fun updateTitle() {
        val title = when (currentTabId) {
            TabId.HOT_THEME -> getString(R.string.main_hot_theme)
            TabId.HOME -> getString(R.string.main_home)
            TabId.STUDIO -> getString(R.string.main_studio)
            else -> ""
        }
    }

    fun initTab() {
        val tabs = listOf<Tab>(
            Tab(
                TabId.HOT_THEME,
                getString(R.string.main_hot_theme),
                R.drawable.selector_main_hot_theme,
                HomeFragment::class
            ),
            Tab(
                TabId.HOME,
                getString(R.string.main_home),
                R.drawable.selector_main_home,
                HomeFragment::class
            ),
            Tab(
                TabId.STUDIO,
                getString(R.string.main_studio),
                R.drawable.selector_main_studio,
                HomeFragment::class
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
                        if(id==TabId.HOME){
                            val layoutParams=viewBinding.tabIcon.layoutParams;
                            layoutParams.width= 48.dp.toInt()
                            layoutParams.height= 48.dp.toInt()
                            viewBinding.tabTitle.visibility= View.GONE
                            viewBinding.tabIcon.layoutParams=layoutParams
                        }
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

    override fun initData(savedInstanceState: Bundle?) {
    }

}