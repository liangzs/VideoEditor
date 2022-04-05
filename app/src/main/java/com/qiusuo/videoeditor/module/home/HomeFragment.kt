package com.qiusuo.videoeditor.module.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nan.xarch.base.list.XRecyclerView
import com.nan.xarch.eventbus.XEventBus
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.base.BaseViewData
import com.qiusuo.videoeditor.constant.EventName
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
        viewBinding.rvList.init(
            XRecyclerView.Config().setViewModel(viewModel)
                .setPullRefreshEnable(true).setPullUploadMoreEnable(true)
                .setLayoutManager(GridLayoutManager(context, SPAN_COUNT))
                .setItemDecoration(GridItemDecoration(activity, SPAN_COUNT))
                .setOnItemClickListener(object : XRecyclerView.OnItemClickListener {
                    override fun onItemClick(parent: RecyclerView, view: View, viewData: BaseViewData<*>, position: Int, id: Long) {
                        Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
                    }

                })
                .setOnItemChildViewClickListener(object :
                    XRecyclerView.OnItemChildViewClickListener {
                    override fun onItemChildViewClick(
                        parent: RecyclerView,
                        view: View,
                        viewData: BaseViewData<*>,
                        position: Int,
                        id: Long,
                        extra: Any?
                    ) {
                        if (extra is String) {
                            Toast.makeText(context, "child item", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        )
        XEventBus.observe(viewLifecycleOwner, EventName.REFRESH_HOME_LIST) { message: String ->
            viewBinding.rvList.refreshList()
            Toast.makeText(context, "home-list消息", Toast.LENGTH_SHORT).show()
        }

        XEventBus.observe(viewLifecycleOwner, EventName.TEST) { message: String ->
            Toast.makeText(context, "test消息", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getPageName(): String {
        return PageName.HOME
    }

}
