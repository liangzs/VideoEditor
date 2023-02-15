package com.ijoysoft.mediasdk.module.mediacodec;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.ffmpeg.FFmpegCmd;
import com.ijoysoft.mediasdk.module.ffmpeg.SingleCmdListener;

/**
 * 需要错误回调！
 */
public class FfmpegBackgroundService extends Service {
    private static final String TAG = "FfmpegBackgroundService";

    private Messenger clientMessenger = null;

    public FfmpegBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "FfmpegBackgroundService--onBind");
        // TODO: Return the communication channel to the service.
        return serviceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "FfmpegBackgroundService--onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "FfmpegBackgroundService--onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    // serviceMessenger是Service自身的Messenger，其内部指向了ServiceHandler的实例
    // 客户端可以通过IBinder构建Service端的Messenger，从而向Service发送消息，
    // 并由ServiceHandler接收并处理来自于客户端的消息
    private Messenger serviceMessenger = new Messenger(new ServiceHandler());
    private Bundle data;

    // MyService用ServiceHandler接收并处理来自于客户端的消息
    // FfmpegTaskType.getType记得改这个
    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "FfmpegBackgroundService收到客户端指令: " + FfmpegTaskType.getType(msg.what).getName());
            FFmpegCmd.setErrorCallBack(new FFmpegCmd.ErrorCallback() {
                @Override
                public void callBack() {
                    sendCrash2Client();
                    FFmpegCmd.getInstance().shutDownNow();
                }
            });
            switch (FfmpegTaskType.getType(msg.what)) {
                case TERMINAL_TASK:
                    clientMessenger = msg.replyTo;
                    Log.i(TAG, "terminal_task..");
                    FFmpegCmd.getInstance().shutDownNow();
                    if (clientMessenger != null) {
                        Log.i(TAG, "FfmpegBackgroundService向客户端回信终止");
                        Message msgToClient = Message.obtain();
                        msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_TERMINAL;
                        // 可以通过Bundle发送跨进程的信息
                        try {
                            clientMessenger.send(msgToClient);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            Log.e(TAG, "FfmpegBackgroundService向客户端发送信息失败: " + e.getMessage());
                        }
                    }
                    break;
                case EXTRACT_AUDIO:
                    data = msg.getData();
                    if (data != null) {
                        String[] strs = data.getStringArray(FFmpegBGConstant.COMMAND);
                        String taskId = (String) data.get(FFmpegBGConstant.TASK_ID);
                        extractMp3(strs[0], strs[1], strs[2], taskId);
                    }
                    // 通过Message的replyTo获取到客户端自身的Messenger，
                    // Service可以通过它向客户端发送消息
                    clientMessenger = msg.replyTo;
                    break;
                case COMMON_TASK:
                    clientMessenger = msg.replyTo;
                    data = msg.getData();
                    if (data != null) {
                        String[] strs = data.getStringArray(FFmpegBGConstant.COMMAND);
                        final String taskId = (String) data.get(FFmpegBGConstant.TASK_ID);
                        FFmpegCmd.getInstance().execute(strs, new SingleCmdListener() {
                            @Override
                            public void next() {
                                send2Client(taskId);
                            }

                            @Override
                            public void onInnerFinish() {

                            }
                        });
                    }
                    break;
                case PROGRESS_TASK:
                    clientMessenger = msg.replyTo;
                    data = msg.getData();
                    int duration = msg.arg1;
                    if (data != null) {
                        String[] strs = data.getStringArray(FFmpegBGConstant.COMMAND);
                        final String taskId = (String) data.get(FFmpegBGConstant.TASK_ID);
                        FFmpegCmd.getInstance().executeProgress(strs, new SingleCmdListener() {
                            @Override
                            public void next() {
                                send2Client(taskId);
                            }

                            @Override
                            public void onInnerFinish() {

                            }

                            @Override
                            public void onProgress(int position) {
                                sendProgress2Client(position);
                            }
                        }, duration);
                    }
                    break;
                //停止任务并杀死进程
                case TERMINAL_AND_KILL:
                    LogUtils.e(TAG, "FfmpegCutService 服务关闭");
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    break;
                default:
                    clientMessenger = msg.replyTo;
                    break;
            }
        }
    }

    /**
     * 发送执行完毕消息
     */
    private void send2Client(String taskId) {
        if (clientMessenger != null) {
            Log.i(TAG, "FfmpegBackgroundService向客户端回信");
            Message msgToClient = Message.obtain();
            msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_FINISHI;
            Bundle bundle = new Bundle();
            bundle.putString(FFmpegBGConstant.TASK_ID, taskId);
            msgToClient.setData(bundle);
            // 可以通过Bundle发送跨进程的信息
            try {
                clientMessenger.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, "FfmpegBackgroundService向客户端发送信息失败: " + e.getMessage());
            }
        }
    }

    private void sendProgress2Client(int position) {
        LogUtils.i(TAG, "FfmpegBackgroundService 进度:" + position);
        // 可以通过Bundle发送跨进程的信息
        if (clientMessenger != null) {
            try {
                Message msgToClient = Message.obtain();
                msgToClient.what = FFmpegBGConstant.SEND_MESSEGE_PROGRESS;
                msgToClient.arg1 = position;
                clientMessenger.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "FfmpegBackgroundService向客户端发送信息失败: " + e.getMessage());
            }
        }
    }

    /**
     * 发送执行崩溃消息
     */
    private void sendCrash2Client() {
        Log.i(TAG, "FfmpegBackgroundService发生异常");
        Message msgToClient = Message.obtain();
        msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_CRASH;
        // 可以通过Bundle发送跨进程的信息
        try {
            clientMessenger.send(msgToClient);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "FfmpegBackgroundService向客户端发送信息失败: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "FfmpegBackgroundService--ondestory");
        clientMessenger = null;
        FFmpegCmd.getInstance().shutDownNow();
        super.onDestroy();
    }

    /**
     * 音频抽取
     *
     * @param inputPath
     * @param outputPath
     * @param changePath
     */
    private void extractMp3(String inputPath, String outputPath, final String changePath, final String taskId) {
        String[] commands = new String[]{"ffmpeg", "-i", inputPath, "-c:a", "libmp3lame", "-vn", "-y", outputPath};
        StringBuilder sb = new StringBuilder();
        for (String str : commands) {
            sb.append(str).append(" ");
        }
        FFmpegCmd.getInstance().execute(commands, new SingleCmdListener() {
            @Override
            public void next() {

            }

            @Override
            public void onInnerFinish() {
                FileUtils.changePrivateFileName(changePath);
                send2Client(taskId);
            }
        });
    }

}
