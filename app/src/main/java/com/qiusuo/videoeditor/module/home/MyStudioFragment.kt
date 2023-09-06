package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.databinding.FragmentStudioBinding


class MyStudioFragment : BaseFragment<FragmentStudioBinding>(FragmentStudioBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView();
    }

    override fun initView() {
        viewBinding.viewPager.adapter=StudioFragmentStateAdapter()
        viewBinding.tabType.addTab(viewBinding.tabType.newTab().apply { this.text=getString(R.string.video) })
        viewBinding.tabType.addTab(viewBinding.tabType.newTab().apply { this.text=getString(R.string.studio) })

        TabLayoutMediator(viewBinding.tabType,viewBinding.viewPager){tab,position->

        }.attach()


    }




    inner class StudioFragmentStateAdapter:FragmentStateAdapter(this){
        override fun getItemCount(): Int =2

        override fun createFragment(position: Int): Fragment {
            return DraftFragment().apply {  }
        }

    }



}
