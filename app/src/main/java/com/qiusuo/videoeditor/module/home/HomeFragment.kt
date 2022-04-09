package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
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
        var gridlayout=GridLayoutManager(context, SPAN_COUNT);
        gridlayout.orientation=GridLayoutManager.HORIZONTAL;
        gridlayout.isAutoMeasureEnabled=true
        viewBinding.rvMainFunc.setLayoutManager(gridlayout)
        viewBinding.rvMainFunc.addItemDecoration(RecyclerItemDecoration(
            activity.resources.getDimensionPixelOffset(R.dimen.item_padding), true, true))
        viewBinding.rvMainFunc.adapter = HomeFunAdapter { position: Int -> funItemClick(position) }
    }


    fun funItemClick(position: Int) {}


    override fun getPageName(): String {
        return PageName.HOME
    }

}
