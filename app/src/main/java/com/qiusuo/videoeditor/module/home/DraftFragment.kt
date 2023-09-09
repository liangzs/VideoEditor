package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.databinding.FragmentDraftBinding
import com.qiusuo.videoeditor.view.adapter.DraftAdapter


class DraftFragment : BaseFragment<FragmentDraftBinding>(FragmentDraftBinding::inflate) {
    private var draftAdapter: DraftAdapter? = null
    private val viewModel :HomeViewModel by viewModels()


    override fun initView() {
        draftAdapter = DraftAdapter(viewModel.loadDraft())
        viewBinding.rv.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        viewBinding.rv.adapter = draftAdapter
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DraftFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}