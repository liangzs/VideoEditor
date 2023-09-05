package com.qiusuo.videoeditor.base

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import com.qiusuo.videoeditor.common.room.AppDataBase

/**
 * Application
 */
class MyApplication : Application() {

    private var applicationStartTime = 0L
    private var db: AppDataBase? = null
    override fun onCreate() {
        super.onCreate()
        initInMainProcess {
            instance = this
        }
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "videoeditor"
        ).build()
    }

    /**
     * 主进程初始化
     */
    private fun initInMainProcess(block: () -> Unit) {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val myPId = Process.myPid()
        val mainProcessName = packageName
        activityManager.runningAppProcesses.forEach {
            if (it.pid == myPId && it.processName == mainProcessName) {
                block()
            }
        }
    }

    companion object {
        lateinit var instance: Application
    }
}