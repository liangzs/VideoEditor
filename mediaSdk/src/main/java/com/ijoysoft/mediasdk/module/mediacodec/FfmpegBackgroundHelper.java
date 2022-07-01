package com.ijoysoft.mediasdk.module.mediacodec;

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


import java.util.Arrays;

import static android.content.Context.BIND_AUTO_CREATE;

public class FfmpegBackgroundHelper {
    private static final String TAG = "FfmpegBackgroundService";

    private static class SingleTonHoler {
        private static FfmpegBackgroundHelper INSTANCE = new FfmpegBackgroundHelper();
    }

    public static FfmpegBackgroundHelper getInstance() {
        return FfmpegBackgroundHelper.SingleTonHoler.INSTANCE;
    }

    private boolean isFinish = true;
    private boolean isBound = false;


    //serviceMessenger表示的是Service端的Messenger，其内部指向了MyService的ServiceHandler实例
    //可以用serviceMessenger向MyService发送消息
    private Messenger serviceMessenger = null;

    //clientMessenger是客户端自身的Messenger，内部指向了ClientHandler的实例
    //MyService可以通过Message的replyTo得到clientMessenger，从而MyService可以向客户端发送消息，
    //并由ClientHandler接收并处理来自于Service的消息
    private Messenger clientMessenger = new Messenger(new ClientHandler());

    //客户端用ClientHandler接收并处理来自于Service的消息
    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "ClientHandler -> handleMessage");
            if (msg.what == FFmpegBGConstant.SEND_MESSAGE_FINISHI) {
                Bundle data = msg.getData();
                if (data != null) {
                    isFinish = data.getBoolean(FFmpegBGConstant.SEND_MESSAGE_FINISHI_KEY, false);
                    Log.i(TAG, "客户端收到Service的消息isFinish: " + isFinish);
                }
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //客户端与Service建立连接
            Log.i(TAG, "客户端 onServiceConnected");
            //我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
            serviceMessenger = new Messenger(binder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //客户端与Service失去连接
            serviceMessenger = null;
            isBound = false;
            Log.i(TAG, "客户端 onServiceDisconnected");
        }
    };

    public void start(Context context) {
        Intent intent = new Intent(context, FfmpegBackgroundService.class);
        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public void stopService(Context context) {
        context.unbindService(conn);
    }

    private void sendCommands(int what, String[] params) {
        isFinish = false;
        Message msg = Message.obtain();
        msg.what = what;
        //此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        //msg.obj = "你好，MyService，我是客户端";
        Bundle data = new Bundle();
        data.putCharSequenceArray("commands", params);
        msg.setData(data);

        //需要将Message的replyTo设置为客户端的clientMessenger，
        //以便Service可以通过它向客户端发送消息
        msg.replyTo = clientMessenger;
        try {
            Log.i(TAG, "客户端向service发送信息+" + Arrays.toString(params));
            if (serviceMessenger!=null){
                serviceMessenger.send(msg);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i(TAG, "客户端向service发送消息失败: " + e.getMessage());
        }
    }

    /**
     * 抽取视频中的音频
     *
     * @param params
     */
    public void extractAudioMp3(String[] params) {
        sendCommands(FFmpegBGConstant.RECEIVE_MESSAGE_EXTRACT_MUSIC, params);
    }

    /**
     * 视频做裁剪时，保留逻辑裁剪的音频文件
     */
    public void trimAudio(String inputPath, String outputPath, String startTime, String endTime) {
        sendCommands(FFmpegBGConstant.RECEIVE_MESSAGE_CUT_AUDIO, new String[]{inputPath, outputPath, startTime, endTime});
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
