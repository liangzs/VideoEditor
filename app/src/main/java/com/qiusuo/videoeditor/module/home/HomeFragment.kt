package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.view.adapter.HOME_STUDIO_INDEX
import com.qiusuo.videoeditor.view.adapter.HOME_THEME_INDEX
import com.qiusuo.videoeditor.view.adapter.HomeFootAdapter
import com.qiusuo.videoeditor.view.adapter.HomeFunAdapter
import com.qiusuo.videoeditor.widgegt.GridItemDecoration
import com.qiusuo.videoeditor.widgegt.RecyclerItemDecoration

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()

    companion object {
        const val SPAN_COUNT = 2;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView();
    }

    private fun initView() {
        var gridlayout = GridLayoutManager(context, SPAN_COUNT);
        gridlayout.orientation = GridLayoutManager.HORIZONTAL;
        gridlayout.isAutoMeasureEnabled = true
        viewBinding.rvMainFunc.setLayoutManager(gridlayout)
        viewBinding.rvMainFunc.addItemDecoration(RecyclerItemDecoration(
            activity.resources.getDimensionPixelOffset(R.dimen.item_padding), true, true))
        viewBinding.rvMainFunc.adapter = HomeFunAdapter { position: Int -> funItemClick(position) }

//        TabLayoutMediator(viewBinding.tabHome,viewBinding.viewPager, object : TabLayoutMediator.TabConfigurationStrategy{
//            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                TODO("Not yet implemented")
//            }
//        }).attach()
        val viewpager = viewBinding.viewPager;
        viewpager.adapter = HomeFootAdapter(this);
        TabLayoutMediator(viewBinding.tabHome, viewpager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()


    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            HOME_THEME_INDEX -> R.drawable.icon_cat
            HOME_STUDIO_INDEX -> R.drawable.icon_cat
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            HOME_THEME_INDEX -> getString(R.string.main_home)
            HOME_STUDIO_INDEX -> getString(R.string.main_home)
            else -> null
        }
    }

    fun funItemClick(position: Int) {}


    override fun getPageName(): String {
        return PageName.HOME
    }

}
