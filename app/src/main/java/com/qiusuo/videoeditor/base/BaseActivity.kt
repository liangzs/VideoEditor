package com.qiusuo.videoeditor.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.qiusuo.videoeditor.util.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**·
 * 基类
 */
abstract class BaseActivity<T : ViewBinding>(var inflater: (inflater: LayoutInflater) -> T) :
    AppCompatActivity() {


    protected lateinit var viewBinding: T;

    //发射器
    var dispose = CompositeDisposable();


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflater(layoutInflater);
        setContentView(viewBinding.root);
        setStatusBar()
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
        StatusBarUtil.setTransparent(this, isWhiteStatusl())
    }

    open fun isWhiteStatusl(): Boolean {
        return false
    }
}