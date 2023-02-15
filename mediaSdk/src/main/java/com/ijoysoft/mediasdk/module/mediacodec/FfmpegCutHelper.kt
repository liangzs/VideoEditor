package com.ijoysoft.mediasdk.module.mediacodec

import android.annotation.SuppressLint
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.gson.Gson
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import java.util.*

/**
 * 主要用户音视频的裁剪功能， 把执行封装在子进程中
 */
@SuppressLint("StaticFieldLeak")
object FfmpegCutHelper {

    @JvmStatic
    val instance: FfmpegCutHelper
        get() = this

    private const val TAG = "FfmpegCutHelper"

    private var isConnect = false
    private var isStop = false

    // 做个轮询动作
    private var currentTask: BackroundTask? = null
    private var serviceMessenger: Messenger? = null
    var cutCallback: CutCallback? = null

    /**
     * 连接回调
     */
    var connectingCallBack: ConnectingCallBack? = null



    private val clientMessenger: Messenger = Messenger(ClientHandler())

    private class ClientHandler : Handler() {
        override fun handleMessage(msg: Message) {
            Log.i(TAG, "ClientHandler -> handleMessage->" + msg.what)
            when (msg.what) {
                FFmpegBGConstant.SEND_MESSAGE_FINISHI -> {
                    currentTask = null
                    Log.i(TAG, "客户端收到Service的消息isFinish: ")
                    if (cutCallback != null) {
                        cutCallback!!.finish(msg.data.getString(FFmpegBGConstant.TASK_ID))
                        cutCallback!!.finish()
                    }
                }
                FFmpegBGConstant.SEND_MESSAGE_CRASH -> {
                    if (cutCallback != null) {
                        cutCallback!!.error()
                    }
                    currentTask = null
                    currentContext?.let {
                        LogUtils.e(TAG,"service disconnected, going to call error and restart")
                        checkService(it)
                    }
                }
                FFmpegBGConstant.SEND_MESSEGE_PROGRESS -> if (cutCallback != null) {
                    cutCallback!!.progress(msg.arg1)
                }
            }
        }
    }

    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            // 客户端与Service建立连接
            Log.i(TAG, "客户端 onServiceConnected")
            // 我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
            serviceMessenger = Messenger(binder)
            isConnect = true
            connectingCallBack?.connected()
            postExeCute?.let {
                postExeCute = null
                it.run()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            // 客户端与Service失去连接
            serviceMessenger = null
            isConnect = false
            // 后台服务端发生崩溃时，把状态进行重置，这样可以继续下一个任务
            Log.i(TAG, "客户端 onServiceDisconnected:" + System.currentTimeMillis())
            if (cutCallback != null) {
                cutCallback!!.error()
            }
            currentContext?.let {
                LogUtils.e(TAG,"service disconnected, going to call error and restart")
                connectingCallBack?.connecting()
                checkService(it)
            }
        }
    }

    private fun startService(context: Context) {
        val intent = Intent(context, FfmpegCutService::class.java)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
        isStop = false
    }
//
    fun checkService(context: Context) {
        if (!isConnect) {
            connectingCallBack?.connecting()
            startService(context)
        }
    }
//
//    fun stopService(context: Context) {
//        if (!isStop) {
//            isStop = true
//            context.unbindService(conn)
//        }
//    }

    /**
     * 单个指令
     *
     * @param duration
     * @param params
     */
    private fun sendCommands(taskId: String, type: Int, duration: Int, params: Array<String>) {
        Log.i(TAG, "sendCommands:" + System.currentTimeMillis())
        val msg = Message.obtain()
        msg.what = type
        msg.arg1 = duration
        // 此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        // msg.obj = "你好，MyService，我是客户端";
        val data = Bundle()
        data.putCharSequenceArray(FFmpegBGConstant.COMMAND, params)
        msg.data = data
        data.putString(FFmpegBGConstant.TASK_ID, taskId)

        // 需要将Message的replyTo设置为客户端的clientMessenger，
        // 以便Service可以通过它向客户端发送消息
        msg.replyTo = clientMessenger
        try {
            Log.i(
                TAG,
                "客户端向service发送信息+" + params.contentToString() + ",serviceMessenger:" + serviceMessenger + "type: $type"
            )
            if (serviceMessenger != null) {
                serviceMessenger!!.send(msg)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
            Log.i(TAG, "客户端向service发送消息失败: " + e.message)
        }
    }

    /**
     * 指令集
     *
     * @param duration
     * @param params
     */
    private fun sendCommandArray(duration: Int, params: List<Array<String>>) {
        Log.i(TAG, "sendCommands:" + System.currentTimeMillis())
        val msg = Message.obtain()
        msg.what = FfmpegTaskType.LIST_TASK.id
        msg.arg1 = duration
        // 此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        // msg.obj = "你好，MyService，我是客户端";
        val data = Bundle()
        data.putString(FFmpegBGConstant.COMMAND, Gson().toJson(params))
        msg.data = data
        // 需要将Message的replyTo设置为客户端的clientMessenger，
        // 以便Service可以通过它向客户端发送消息
        msg.replyTo = clientMessenger
        try {
            Log.i(TAG, "客户端向service发送信息-list+")
            if (serviceMessenger != null) {
                serviceMessenger!!.send(msg)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
            Log.i(TAG, "客户端向service发送消息失败: " + e.message)
        }
    }

    /**
     * 执行任务
     *
     * @param task
     * @param duration
     * @param cutCallback
     */
    fun exeCuteTask(task: BackroundTask, duration: Int, cutCallback: CutCallback) {
//        this.cutCallback = cutCallback
//        if (isConnect) {
//            currentTask = task
//            sendCommands(task.id, task.ffmpegTaskType.id, duration, task.commands)
//        } else {
//            cutCallback.error()
//        }
        exeCuteTaskWaitingConnect(task, duration, cutCallback)
    }

    /**
     * 连接后执行的任务
     */
    var postExeCute: Runnable? = null

    /**
     * 执行任务
     *
     * @param task
     * @param duration
     * @param cutCallback
     */
    fun exeCuteTaskWaitingConnect(task: BackroundTask, duration: Int, cutCallback: CutCallback?) {
        this.cutCallback = cutCallback
        val runnable = Runnable {
            currentTask = task
            sendCommands(task.id, task.ffmpegTaskType.id, duration, task.commands)
        }
        if (isConnect) {
            runnable.run()
        } else {
            LogUtils.w(TAG, "waiting for connection")
            postExeCute = runnable
        }
    }

    fun executeTast(list: List<Array<String>>, duration: Int, cutCallback: CutCallback) {
//        this.cutCallback = cutCallback
//        if (isConnect) {
//            sendCommandArray(duration, list)
//        } else {
//            cutCallback.error()
//        }
        exeCuteTaskWaitingConnect(list, duration, cutCallback)
    }


    fun exeCuteTaskWaitingConnect(list: List<Array<String>>, duration: Int, cutCallback: CutCallback) {
        this.cutCallback = cutCallback
        val runnable = Runnable {
            sendCommandArray(duration, list)
        }
        if (isConnect) {
            runnable.run()
        } else {
            postExeCute = runnable
        }
    }

    fun checkIsExcuting(taskId: String): Boolean {
        if (ObjectUtils.isEmpty(currentTask)) {
            return false
        }
        return if (ObjectUtils.isEmpty(taskId)) {
            false
        } else currentTask!!.id == taskId
    }

    fun onDestroy() {
        currentTask = null
        cutCallback = null
    }

    interface CutCallback {
        fun error() {}
        fun progress(progress: Int) {}
        fun finish(taskId: String?) {}
        fun finish()
    }

    /**
     * 杀进程
     */
    fun stopTaskAndTerminalService() {
        sendCommands("", FfmpegTaskType.TERMINAL_AND_KILL.id, 0, arrayOf())
    }

    /**
     * 当前页面context
     */
    @Volatile
    var currentContext: Context? = null

    /**
     * 在生命周期的范围内使用CutService
     */
    fun register(lifecycle: Lifecycle, context: Context) {
        lifecycle.addObserver(LifecycleEventObserver { state, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                LogUtils.i(TAG,"with lifecycle event: onCreate, startService")
                currentContext = context
                startService(currentContext!!)
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                LogUtils.i(TAG,"with lifecycle event: onDestroy, stopTaskAndTerminalService")
                currentContext = null
                cutCallback = null
                connectingCallBack = null
                stopTaskAndTerminalService()
            }
        })
    }

    /**
     * 服务连接回调
     */
    interface ConnectingCallBack {
        /**
         * 连接中
         */
        fun connecting()

        /**
         * 已连上
         */
        fun connected()
    }






}