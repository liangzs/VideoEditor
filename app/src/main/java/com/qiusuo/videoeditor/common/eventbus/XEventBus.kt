package com.nan.xarch.eventbus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.nan.xarch.eventbus.core.EmptyMessage
import com.nan.xarch.eventbus.core.EventLiveData
import com.qiusuo.videoeditor.constant.EventName

object XEventBus {

    private val channels = HashMap<String, EventLiveData<*>>()

    private fun <T> with(@EventName eventName: String): EventLiveData<T> {
        synchronized(channels) {
            if (!channels.containsKey(eventName)) {
                channels[eventName] = EventLiveData<T>()
            }
            return (channels[eventName] as EventLiveData<T>)
        }
    }

    fun <T> post(@EventName eventName: String, message: T) {
        val channel = with<T>(eventName)
        channel.postValue(message!!)
    }

    fun <T> observe(owner: LifecycleOwner, @EventName eventName: String, sticky: Boolean = false, observer: Observer<T>) {
        val channel = with<T>(eventName)
        channel.observe(owner, sticky, observer)
    }

    fun post(@EventName eventName: String) {
        val channel = with<EmptyMessage>(eventName)
        channel.postValue(EmptyMessage)
    }

    fun observe(owner: LifecycleOwner, @EventName eventName: String, sticky: Boolean = false, observer: () -> Unit) {
        val channel = with<EmptyMessage>(eventName)
        channel.observe(owner, sticky) {
            observer()
        }
    }
}
