package com.qiusuo.videoeditor.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.qiusuo.videoeditor.ui.widgegt.LoadingDialog
import com.qiusuo.videoeditor.util.LightStatusBarUtils
import com.qiusuo.videoeditor.util.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch

/**·
 * 基类
 */
abstract class BaseActivity<T : ViewBinding>(var inflater: (inflater: LayoutInflater) -> T) :
    AppCompatActivity() {


    protected lateinit var viewBinding: T;

    //发射器
    var dispose = CompositeDisposable();

    var loading: LoadingDialog? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflater(layoutInflater);
        setContentView(viewBinding.root);
//        setStatusBar()
        initData(savedInstanceState)
    }

    fun addDispose(disposable: Disposable) {
        dispose.add(disposable);
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose.dispose();
    }

    //这里做了两件事情，1.使状态栏透明并使contentView填充到状态栏 2.预留出状态栏的位置，防止界面上的控件离顶部靠的太近。这样就可以实现开头说的第二种情况的沉浸式状态栏了
    open fun setStatusBar() {
        StatusBarUtil.setTransparentStatusBar(this,false)
        StatusBarUtil.setTransparentNavigationBar(this)
//        LightStatusBarUtils.setLightStatusBar(this,false)
    }

    open fun isWhiteStatusl(): Boolean {
        return true
    }

    abstract open fun initData(savedInstanceState: Bundle?);


    /**
     * 显示加载提示
     *
     * @param tips
     */
    open fun showLoading(tips: String?) {
        if (this.isFinishing || this.isDestroyed) {
            return
        }
        if (null == loading) {
            loading = LoadingDialog(this)
        }
        lifecycleScope.launch {
            loading?.apply {
                setTips(tips)
                getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                show()
            }
        }
    }

    /**
     * 显示加载提示
     */
    open fun showLoading(cancel: Boolean, tips: String?) {
        if (this.isFinishing || this.isDestroyed) {
            return
        }
        if (null == loading) {
            loading = LoadingDialog(this)
        }
        lifecycleScope.launch {
            loading?.apply {
                setTips(tips)
                show()
            }
        }
    }

    open fun showLoading(tips: String?, mask: Boolean) {
        if (this.isFinishing || this.isDestroyed) {
            return
        }
        if (null == loading) {
            loading = LoadingDialog(this)
        }
        lifecycleScope.launch {
            loading?.apply {
                setTips(tips)
                show()
            }
        }
        if (mask) {
            loading?.setMask(true)
        }
        loading?.setOnDismissListener { loading?.setMask(false) }
    }

    open fun loadShowing(): Boolean {
        return loading?.isShowing() ?: false
    }

    /**
     * 隐藏loading
     */
    open fun endLoading() {
        loading?.let {
            if (it.isShowing() && !isFinishing) {
                it.dismiss()
            }
        }

    }

}