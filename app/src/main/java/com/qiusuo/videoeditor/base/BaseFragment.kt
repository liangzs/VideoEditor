package com.qiusuo.videoeditor.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<T : ViewBinding>(var inflater: (inflater: LayoutInflater, container: ViewGroup?, attchRoot: Boolean) -> T) :
    Fragment(), IGetPageName {

    protected lateinit var viewBinding: T
    val dispose = CompositeDisposable()
    lateinit var activity: BaseActivity<*>;


    abstract fun initView();
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as BaseActivity<*>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = this.inflater(inflater, container, false);
        initView();
        return viewBinding.root;
    }

    override fun onDestroyView() {
        dispose.dispose()
        super.onDestroyView()
    }

    fun addDisPose(disposable: Disposable) {
        dispose.add(disposable)
    }

    override fun getPageName(): String {
        return ""
    }


}
