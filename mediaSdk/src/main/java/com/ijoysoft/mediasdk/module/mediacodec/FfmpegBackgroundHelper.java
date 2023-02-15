package com.ijoysoft.mediasdk.module.mediacodec;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 目前暂时两个功能提供移除功能
 * 一个是视频抽取功能，有可能吧视频删除，taskid挂载在videoMediaItem中
 * 另外一个是音频的再切割和拼接功能，可能会把音频取消，taskid挂载在audioitem中
 * <p>
 * 如果目的方法时executeList则把List<String[]> cmdList遍历分别传入tasks中,
 * 目前对于
 */
public class FfmpegBackgroundHelper {
    private static final String TAG = "FfmpegBackgroundService";
    // 做个轮询动作
    private CopyOnWriteArrayList<BackroundTask> tasks = new CopyOnWriteArrayList<>();
    private BackroundTask currentTask;
    private Context mApplicationContext;
    private Handler mHandler = new Handler();
    private ExecuteCallback callback;
    private SingleCommanCallback singleCommanCallback;

    private static class SingleTonHoler {
        private static FfmpegBackgroundHelper INSTANCE = new FfmpegBackgroundHelper();
    }

    public static FfmpegBackgroundHelper getInstance() {
        return FfmpegBackgroundHelper.SingleTonHoler.INSTANCE;
    }

    // private FfmpegBackgroundHelper() {
    // isFinish = true;
    // }

    // private boolean isFinish;
    private FfmpegStatus ffmpegStatus;
    private boolean isConnect;
    private boolean isStop;

    // serviceMessenger表示的是Service端的Messenger，其内部指向了MyService的ServiceHandler实例
    // 可以用serviceMessenger向MyService发送消息
    private Messenger serviceMessenger = null;

    // clientMessenger是客户端自身的Messenger，内部指向了ClientHandler的实例
    // MyService可以通过Message的replyTo得到clientMessenger，从而MyService可以向客户端发送消息，
    // 并由ClientHandler接收并处理来自于Service的消息
    private Messenger clientMessenger = new Messenger(new ClientHandler());

    // 客户端用ClientHandler接收并处理来自于Service的消息
    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "ClientHandler -> handleMessage->" + msg.what);
            switch (msg.what) {
                case FFmpegBGConstant.SEND_MESSAGE_FINISHI:
                    String taskId = msg.getData().getString(FFmpegBGConstant.TASK_ID);
                    if (!ObjectUtils.isEmpty(tasks)) {
                        BackroundTask task = checkExistTask(taskId);
                        if (tasks != null) {
                            tasks.remove(task);
                        }
                        Log.i(TAG, "tasks.size: " + tasks.size());
                    }

                    if (ObjectUtils.isEmpty(tasks)) {
                        ffmpegStatus = FfmpegStatus.EXCUTE_IDLE;
                        currentTask = null;
                        Log.i(TAG, "客户端收到Service的消息isFinish: " + ffmpegStatus + ",singleCommanCallback:" + singleCommanCallback);
                        if (singleCommanCallback != null) {
                            singleCommanCallback.finish();
                            singleCommanCallback = null;
                        }
                        return;
                    }
                    switchNext();
                    break;
                case FFmpegBGConstant.SEND_MESSAGE_TERMINAL:
                    if (currentTask != null) {
                        Log.i(TAG, "客户端收到service的消息异常指令:" + Arrays.toString(currentTask.getCommands()));
                    }
                    // 重启服务
                    if (!ObjectUtils.isEmpty(tasks)) {
                        tasks.remove(currentTask);
                    }
                    currentTask = null;
                    restartService();
                    singleCommanCallback = null;
//                    ffmpegStatus = FfmpegStatus.EXCUTE_IDLE;
                    break;
                case FFmpegBGConstant.SEND_MESSAGE_CRASH:
                    // 重启服务
                    if (!ObjectUtils.isEmpty(tasks)) {
                        tasks.remove(currentTask);
                    }
                    // 清理任务，清理
                    currentTask = null;
                    if (callback != null) {
                        callback.error();
                    }
                    if (singleCommanCallback != null) {
                        singleCommanCallback.error();
                    }
                    singleCommanCallback = null;
                    break;
                case FFmpegBGConstant.SEND_MESSEGE_PROGRESS:
                    if (singleCommanCallback != null) {
                        singleCommanCallback.progress(msg.arg1);
                    }
                    break;
            }
        }
    }

    private BackroundTask checkExistTask(String taskId) {
        if (ObjectUtils.isEmpty(tasks) || taskId == null) {
            return null;
        }
        for (BackroundTask task : tasks) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // 客户端与Service建立连接
            Log.i(TAG, "客户端 onServiceConnected:" + +System.currentTimeMillis());
            // 我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
            serviceMessenger = new Messenger(binder);
            isConnect = true;
            switchNext();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 客户端与Service失去连接
            serviceMessenger = null;
            // isFinish = true;
            isConnect = false;
            // 后台服务端发生崩溃时，把状态进行重置，这样可以继续下一个任务
            ffmpegStatus = FfmpegStatus.EXCUTE_IDLE;
            Log.i(TAG, "客户端 onServiceDisconnected:" + System.currentTimeMillis());
        }
    };

    public void init(Context mApplicationContext) {
        this.mApplicationContext = mApplicationContext;
    }

    public void startService() {
        LogUtils.v("FfmpegBackgroundHelper", "params:isConnect" + ":" + isConnect);
        if (mApplicationContext != null && !isConnect) {
            ThreadPoolMaxThread.getInstance().execute(() -> {
                Intent intent = new Intent(mApplicationContext, FfmpegBackgroundService.class);
                mApplicationContext.bindService(intent, conn, BIND_AUTO_CREATE);
                isStop = false;
            });


        }
    }

    public void restartService() {
        if (!isConnect || !isServiceRunning()) {
            stopService();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService();
                }
            }, 500);
        }
    }

    public void restartService(boolean force) {
        if (!isConnect || !isServiceRunning() || force) {
            stopService();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService();
                }
            }, 500);
        }
    }

    public void stopService() {
        if (isConnect && !isStop) {
            try {
                mApplicationContext.unbindService(conn);
                isConnect = false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void stopTask() {
        sendCommands("", FfmpegTaskType.TERMINAL_TASK.getId(), new String[]{});
    }

    private void sendCommands(String taskid, int id, String[] params) {
        Log.i(TAG, "sendCommands:" + System.currentTimeMillis() + ",currentThread:" + Thread.currentThread().getName());
        // isFinish = false;
        ffmpegStatus = FfmpegStatus.EXECUTE_START;
        Log.i(TAG, "启动任务: " + ffmpegStatus);

        Message msg = Message.obtain();
        msg.what = id;

        // 此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        // msg.obj = "你好，MyService，我是客户端";
        Bundle data = new Bundle();
        data.putCharSequenceArray(FFmpegBGConstant.COMMAND, params);
        data.putString(FFmpegBGConstant.TASK_ID, taskid);
        msg.setData(data);
        // 需要将Message的replyTo设置为客户端的clientMessenger，
        // 以便Service可以通过它向客户端发送消息
        msg.replyTo = clientMessenger;
        try {
            Log.i(TAG, "客户端向service发送信息+" + Arrays.toString(params) + ",FfmpegTaskType:" + id);
            if (serviceMessenger != null) {
                serviceMessenger.send(msg);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i(TAG, "客户端向service发送消息失败: " + e.getMessage());
        }
    }

    /**
     * 执行单个指令
     *
     * @param task
     */
    public void exeCuteTask(BackroundTask task) {
        tasks.add(task);
        if (ffmpegStatus == FfmpegStatus.EXCUTE_IDLE && isConnect) {
            currentTask = tasks.get(0);
            sendCommands(task.getId(), task.getFfmpegTaskType().getId(), task.getCommands());
        }
    }

    /**
     * 执行单个指令
     *
     * @param task
     * @param singleCommanCallback
     */
    public void exeCuteTask(BackroundTask task, SingleCommanCallback singleCommanCallback) {
        tasks.add(task);
        this.singleCommanCallback = singleCommanCallback;
        if (ffmpegStatus == FfmpegStatus.EXCUTE_IDLE && isConnect) {
            currentTask = tasks.get(0);
            sendCommands(task.getId(), task.getFfmpegTaskType().getId(), task.getCommands());
        }
    }

    /**
     * 命令集合
     *
     * @param commands
     * @param singleCommanCallback
     */
    public void exeCuteTask(List<String[]> commands, SingleCommanCallback singleCommanCallback) {
        this.singleCommanCallback = singleCommanCallback;
        for (String[] command : commands) {
            BackroundTask task = new BackroundTask(command, FfmpegTaskType.COMMON_TASK);
            tasks.add(task);
        }
        if (ffmpegStatus == FfmpegStatus.EXCUTE_IDLE && isConnect && !ObjectUtils.isEmpty(tasks)) {
            currentTask = tasks.get(0);
            sendCommands(currentTask.getId(), currentTask.getFfmpegTaskType().getId(), currentTask.getCommands());
        }
    }


    /**
     * 更新进度的任务
     */
    public void exeCuteTaskProgress(BackroundTask task, SingleCommanCallback singleCommanCallback) {
        tasks.add(task);
        this.singleCommanCallback = singleCommanCallback;
        if (ffmpegStatus == FfmpegStatus.EXCUTE_IDLE && isConnect) {
            currentTask = tasks.get(0);
            ffmpegStatus = FfmpegStatus.EXECUTE_START;
            Message msg = Message.obtain();
            msg.what = FfmpegTaskType.PROGRESS_TASK.getId();
            msg.arg1 = task.getDuration();
            Bundle data = new Bundle();
            data.putCharSequenceArray(FFmpegBGConstant.COMMAND, task.getCommands());
            data.putString(FFmpegBGConstant.TASK_ID, task.getId());
            msg.setData(data);
            msg.replyTo = clientMessenger;
            try {
                Log.i(TAG, "客户端向service发送信息+" + Arrays.toString(task.getCommands()) + ",serviceMessenger:" + serviceMessenger);
                if (serviceMessenger != null) {
                    serviceMessenger.send(msg);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i(TAG, "客户端向service发送消息失败: " + e.getMessage());
            }
        }


    }

    /**
     * 如果任务正在作业中，需要同时把任务移除和杀死重启服务
     * 如果当前不在作业或者已经完成作业，则遍历移除任务即可
     *
     * @param taskId
     * @return
     */
    public boolean removeTask(String taskId) {
        if (ObjectUtils.isEmpty(taskId)) {
            return false;
        }
        if (currentTask != null && currentTask.getId().equals(taskId)) {
            stopTask();
            return true;
        }
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(taskId)) {
                tasks.remove(i);
                break;
            }
        }
        return true;
    }

    public void removeTask(List<BackroundTask> list) {
        boolean hasCurrent = false;
        for (BackroundTask task : list) {
            if (ObjectUtils.isEmpty(task) || ObjectUtils.isEmpty(tasks)) {
                continue;
            }
            if (tasks.contains(task)) {
                tasks.remove(task);
                if (currentTask != null && currentTask.getId().equals(task.getId())) {
                    hasCurrent = true;
                }
            }
        }
        if (hasCurrent) {
            stopTask();
        }
    }

    public boolean checkIsExcuting(String taskId) {
        if (ObjectUtils.isEmpty(currentTask)) {
            return false;
        }
        if (ObjectUtils.isEmpty(taskId)) {
            return false;
        }
        return currentTask.getId().equals(taskId);
    }

    /**
     * 切换下一个任务
     */
    private void switchNext() {
        if (tasks.size() > 0 && isConnect) {
            currentTask = tasks.get(0);
            sendCommands(currentTask.getId(), currentTask.getFfmpegTaskType().getId(), currentTask.getCommands());
        } else {
            // isFinish = true;
            ffmpegStatus = FfmpegStatus.EXCUTE_IDLE;
            Log.i(TAG, "无任务： " + ffmpegStatus);

            currentTask = null;
        }
    }

    public FfmpegStatus getFfmpegStatus() {
        return ffmpegStatus;
    }

    /**
     * 检测是否有音频抽取任务，如果有抽取任务，则后续音量修改的任务则进行等待
     *
     * @return
     */
    public boolean checkExistExtractAudio() {
        if (ObjectUtils.isEmpty(tasks)) {
            return false;
        }
        for (BackroundTask backroundTask : tasks) {
            if (backroundTask.getFfmpegTaskType() == FfmpegTaskType.EXTRACT_AUDIO) {
                return true;
            }
        }
        return false;

    }


    public void setFfmpegStatus(FfmpegStatus ffmpegStatus) {
        this.ffmpegStatus = ffmpegStatus;
    }

    public void onDestroy() {
        tasks.clear();
        currentTask = null;
        callback = null;
        singleCommanCallback = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 当合成取消时，取消一系列的后台任务，不过音频的抽取需要保留
     */
    public void clearTasks() {
        List<BackroundTask> bak = (List<BackroundTask>) tasks.clone();
        List<BackroundTask> removeList = new ArrayList<>();
        for (BackroundTask backroundTask : bak) {
            if (backroundTask.getFfmpegTaskType() != FfmpegTaskType.EXTRACT_AUDIO) {
                removeList.add(backroundTask);
            }
        }
        tasks.removeAll(removeList);
        if (currentTask != null && currentTask.getFfmpegTaskType() != FfmpegTaskType.EXTRACT_AUDIO) {
            stopTask();
        }
        mHandler.removeCallbacksAndMessages(null);
    }


    public void setCallback(ExecuteCallback callback) {
        this.callback = callback;
    }

    /**
     * 清除回调
     */
    public void clearCallback() {
        this.callback = null;
        this.singleCommanCallback = null;
    }

    public interface ExecuteCallback {
        void error();
    }

    public interface SingleCommanCallback extends ExecuteCallback {
        void finish();

        default void progress(int progress) {
        }
    }

    /**
     * 杀进程
     */
    public void stopTaskAndTerminalService() {
        sendCommands("", FfmpegTaskType.TERMINAL_AND_KILL.getId(), new String[]{});
    }

    public boolean isServiceRunning() {
        String className = "com.ijoysoft.mediasdk.module.mediacodec.FfmpegBackgroundService";
        ActivityManager activityManager = (ActivityManager) mApplicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (serviceList.isEmpty()) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceList) {
            LogUtils.v(TAG, "params:" + "info.service.getClassName():" + info.service.getClassName() + "," + "className:" + className);
            if (info.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

}
