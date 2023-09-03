package com.qiusuo.videoeditor.module.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.databinding.FragmentHotThemeBinding
import com.qiusuo.videoeditor.ui.adapter.HomeFunAdapter
import com.qiusuo.videoeditor.view.adapter.ThemeAdapter
import com.smarx.notchlib.NotchScreenManager


class HotThemeFragment : BaseFragment<FragmentHotThemeBinding>(FragmentHotThemeBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()
    lateinit var launcher: ActivityResultLauncher<Intent>




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = createActivityResultLauncher()
        initView();
    }

    override fun initView() {
        var gridlayout = GridLayoutManager(context, 2);
        gridlayout.isAutoMeasureEnabled = true
        viewBinding.rvTheme.setLayoutManager(gridlayout)
        viewBinding.rvTheme.adapter = ThemeAdapter()
        viewModel.loadTab().forEach {
            val tab=viewBinding.tabTheme.newTab()
            tab.text=it;
            viewBinding.tabTheme.addTab(tab)
        }

    }


    /**
     * 功能表
     */
    fun funItemClick(position: Int) {
        openGallery()
    }


    override fun getPageName(): String {
        return PageName.HOME
    }

    /**
     * 跳转到照片选择页面
     */
    fun openGallery() {
    }


    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    fun createActivityResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    val resultCode = result?.resultCode
                }
            })
    }



}
