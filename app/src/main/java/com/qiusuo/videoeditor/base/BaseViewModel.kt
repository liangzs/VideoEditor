package com.qiusuo.videoeditor.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class BaseViewModel : ViewModel() {

    var dispose =CompositeDisposable();

    override fun onCleared() {
        super.onCleared()
        dispose.dispose()
    }

    fun addDisPosable(dis:Disposable){
        dispose.add(dis);
    }

}