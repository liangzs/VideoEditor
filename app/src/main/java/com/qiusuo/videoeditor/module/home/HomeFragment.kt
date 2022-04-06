package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nan.xarch.base.list.XRecyclerView
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.base.BaseViewData
import com.qiusuo.videoeditor.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.widgegt.GridItemDecoration

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
        viewBinding.rvMainFunc.setLayoutManager(GridLayoutManager(context, SPAN_COUNT))
        viewBinding.rvMainFunc.addItemDecoration(GridItemDecoration(activity, SPAN_COUNT))
       
    }


    override fun getPageName(): String {
        return PageName.HOME
    }

}
