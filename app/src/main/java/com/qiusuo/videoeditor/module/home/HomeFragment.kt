package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nan.xarch.base.list.XRecyclerView
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.base.BaseViewData
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

    private fun initView(){
        viewBinding.rvList.init(
            XRecyclerView.Config()
                .setViewModel(viewModel)
                .setLayoutManager(GridLayoutManager(activity, SPAN_COUNT))
                .setItemDecoration(GridItemDecoration(activity, SPAN_COUNT))
                .setPullRefreshEnable(true)
                .setPullUploadMoreEnable(true)
                .setOnItemClickListener(object :XRecyclerView.OnItemClickListener{
                    override fun onItemClick(parent: RecyclerView,
                        view: View,
                        viewData: BaseViewData<*>,
                        position: Int,
                        id: Long) {
                        TODO("Not yet implemented")
                    }

                })
        )
    }

}