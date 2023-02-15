package com.ijoysoft.mediasdk.module.mediacodec;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.ffmpeg.FFmpegCmd;
import com.ijoysoft.mediasdk.module.ffmpeg.SingleCmdListener;

import java.util.List;

/**
 * 专注裁剪服务20年
 * 裁剪模块需要进度
 */
public class FfmpegCutService extends Service {
    private static final String TAG = "FfmpegCutService";

    private Messenger clientMessenger = null;

    public FfmpegCutService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i(TAG, "FfmpegCutService--onBind");
        // TODO: Return the communication channel to the service.
        return serviceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "FfmpegCutService--onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG, "FfmpegCutService--onStartCommand");
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
            LogUtils.i(TAG, "FfmpegCutService收到客户端指令: " + FfmpegTaskType.getType(msg.what).getName());
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
                    LogUtils.i(TAG, "terminal_task..");
                    FFmpegCmd.getInstance().shutDownNow();
                    if (clientMessenger != null) {
                        LogUtils.i(TAG, "FfmpegCutService向客户端回信终止");
                        Message msgToClient = Message.obtain();
                        msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_TERMINAL;
                        // 可以通过Bundle发送跨进程的信息
                        try {
                            clientMessenger.send(msgToClient);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            LogUtils.e(TAG, "FfmpegCutService向客户端发送信息失败: " + e.getMessage());
                        }
                    }
                    break;
                case COMMON_TASK:
                    clientMessenger = msg.replyTo;
                    data = msg.getData();
                    int duration = msg.arg1;
                    if (data != null) {
                        String[] strs = data.getStringArray(FFmpegBGConstant.COMMAND);
                        String taskId = data.getString(FFmpegBGConstant.TASK_ID);
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
                case LIST_TASK:// 列表任务
                    clientMessenger = msg.replyTo;
                    data = msg.getData();
                    if (data != null) {
                        ThreadPoolMaxThread.getInstance().execute(() -> {
                            String str = data.getString(FFmpegBGConstant.COMMAND);
                            String taskId = data.getString(FFmpegBGConstant.TASK_ID);
                            List<String[]> commands = new Gson().fromJson(str, new TypeToken<List<String[]>>() {
                            }.getType());
                            FFmpegCmd.getInstance().executeList(commands, new SingleCmdListener() {
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
                            });
                        });


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
            LogUtils.i(TAG, "FfmpegCutService向客户端回信");
            // 可以通过Bundle发送跨进程的信息
            try {
                Message msgToClient = Message.obtain();
                msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_FINISHI;
                Bundle bundle = new Bundle();
                bundle.putString(FFmpegBGConstant.TASK_ID, taskId);
                msgToClient.setData(bundle);
                clientMessenger.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "FfmpegCutService向客户端发送信息失败: " + e.getMessage());
            }
        }
    }

    /**
     * 发送执行崩溃消息
     */
    private void sendCrash2Client() {
        LogUtils.i(TAG, "FfmpegCutService发生异常");
        // 可以通过Bundle发送跨进程的信息
        if (clientMessenger != null) {
            try {
                Message msgToClient = Message.obtain();
                msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_CRASH;
                clientMessenger.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "FfmpegCutService向客户端发送信息失败: " + e.getMessage());
            }
        }

    }

    private void sendProgress2Client(int position) {
        LogUtils.i(TAG, "FfmpegCutService 进度:" + position);
        // 可以通过Bundle发送跨进程的信息
        if (clientMessenger != null) {
            try {
                Message msgToClient = Message.obtain();
                msgToClient.what = FFmpegBGConstant.SEND_MESSEGE_PROGRESS;
                msgToClient.arg1 = position;
                clientMessenger.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "FfmpegCutService向客户端发送信息失败: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "FfmpegCutService--ondestory");
        clientMessenger = null;
        FFmpegCmd.getInstance().shutDownNow();
        super.onDestroy();
    }

}
