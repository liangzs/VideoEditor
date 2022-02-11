package com.qiusuo.videoeditor.base

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

/**·
 * 基类
 */
abstract class BaseActivity<T : ViewBinding>(var inflater: (inflater: LayoutInflater) -> T) :
    AppCompatActivity() {


    protected lateinit var viewBinding: T;
    //发射器
    var dispose= CompositeDisposable();


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflater(layoutInflater);
        setContentView(viewBinding.root);
    }


    fun addDispose(disposable:Disposable){
        dispose.add(disposable);
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose.dispose();
    }
}