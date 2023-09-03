package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.gyf.immersionbar.ktx.immersionBar
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.nan.xarch.bean.Tab
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.common.constant.TabId
import com.qiusuo.videoeditor.databinding.ActivityMainBinding
import com.qiusuo.videoeditor.databinding.ViewTabIndicatorBinding
import com.qiusuo.videoeditor.ui.widgegt.TabIndicatorView
import com.qiusuo.videoeditor.ui.widgegt.guide.util.ScreenUtils
import com.qiusuo.videoeditor.util.dp


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @TabId
    private var currentTabId = TabId.HOME

    val tabs :List<Tab> by lazy {
        listOf(
        Tab(
            TabId.HOT_THEME,
            getString(R.string.main_hot_theme),
            R.drawable.selector_main_hot_theme,
            HotThemeFragment::class
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
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystembar()
        initView()
    }

    fun initView() {
        viewBinding.fragmentViewpager.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment =
                when (position) {
                    0 -> HotThemeFragment()
                    1 -> HomeFragment()
                    else -> {
                        HomeFragment()
                    }
                }
        }
       TabLayoutMediator(viewBinding.tabMenu,viewBinding.fragmentViewpager){
                tab: TabLayout.Tab, position: Int->
           tab.setIcon(tabs.get(position).icon)
           tab.setText(tabs.get(position).title)
           if(position==1){
               val tabBiding=ViewTabIndicatorBinding.inflate(LayoutInflater.from(this),null,false)
            tabBiding.tabIcon.setImageResource(tabs.get(position).icon)
            tabBiding.tabTitle.setText(tabs.get(position).title)
            tab.setCustomView(tabBiding.root)
            val layoutParams = tabBiding.tabIcon.layoutParams;
            layoutParams?.width = 48.dp.toInt()
            layoutParams?.height = 48.dp.toInt()
           }
    }.attach()
        viewBinding.fragmentViewpager.currentItem=1


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

//fun initTab() {
//    tabs.forEach {
//       val tab= viewBinding.tabMenu.newTab();
//
//        if (it.id == TabId.HOME) {
//            val tabBiding=ViewTabIndicatorBinding.inflate(LayoutInflater.from(this),null,false)
//            tabBiding.tabIcon.setImageResource(it.icon)
//            tabBiding.tabTitle.setText(it.title)
//            tab.setCustomView(tabBiding.root)
//            val layoutParams = tabBiding.tabIcon.layoutParams;
//            layoutParams?.width = 48.dp.toInt()
//            layoutParams?.height = 48.dp.toInt()
//            tabBiding.tabTitle.setText("")
//        }else{
//            tab.setIcon(it.icon)
//            tab.setText(it.title)
//        }
//        viewBinding.tabMenu.addTab(tab)
//    }
//}

override fun initData(savedInstanceState: Bundle?) {
}

}