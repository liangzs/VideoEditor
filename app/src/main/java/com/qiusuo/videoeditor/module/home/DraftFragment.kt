package com.qiusuo.videoeditor.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.databinding.FragmentDraftBinding
import com.qiusuo.videoeditor.view.adapter.DraftAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DraftFragment : BaseFragment<FragmentDraftBinding>(FragmentDraftBinding::inflate) {
    private var draftAdapter: DraftAdapter? = null
    private val viewModel :HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun initView() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
        draftAdapter = DraftAdapter(viewModel.loadDraft())
        viewBinding.rv.adapter = draftAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draft, container, false)
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