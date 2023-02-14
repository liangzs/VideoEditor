package com.qiusuo.videoeditor.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class ViewModelActivity<VM : ViewModel, B : ViewBinding>(
    inflater: (inflater: LayoutInflater) -> B, val vmClass: Class<VM>
) : BaseActivity<B>(inflater) {

    lateinit var viewModel: VM;
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(vmClass)
        super.onCreate(savedInstanceState)
    }

}