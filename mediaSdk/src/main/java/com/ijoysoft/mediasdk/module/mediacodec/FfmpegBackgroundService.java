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
import com.ijoysoft.mediasdk.module.ffmpeg.SingleCmdListener;
import com.ijoysoft.mediasdk.module.ffmpeg.VideoEditManager;

import java.util.Arrays;

public class FfmpegBackgroundService extends Service {
    private static final String TAG = "FfmpegBackgroundService";

    private Messenger clientMessenger = null;

    public FfmpegBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return serviceMessenger.getBinder();
    }

    // serviceMessenger是Service自身的Messenger，其内部指向了ServiceHandler的实例
    // 客户端可以通过IBinder构建Service端的Messenger，从而向Service发送消息，
    // 并由ServiceHandler接收并处理来自于客户端的消息
    private Messenger serviceMessenger = new Messenger(new ServiceHandler());
    private Bundle data;

    // MyService用ServiceHandler接收并处理来自于客户端的消息
    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case FFmpegBGConstant.RECEIVE_MESSAGE_EXTRACT_MUSIC:
                data = msg.getData();
                if (data != null) {
                    String[] strs = data.getStringArray("commands");
                    Log.i(TAG, "FfmpegBackgroundService收到客户端如下信息: " + Arrays.toString(strs));
                    extractMp3(strs[0], strs[1], strs[2]);
                }
                // 通过Message的replyTo获取到客户端自身的Messenger，
                // Service可以通过它向客户端发送消息
                clientMessenger = msg.replyTo;
                break;
            case FFmpegBGConstant.RECEIVE_MESSAGE_CUT_AUDIO:
                data = msg.getData();
                if (data != null) {
                    String[] strs = data.getStringArray("commands");
                    Log.i(TAG, "FfmpegBackgroundService收到客户端如下信息: " + Arrays.toString(strs));
                    VideoEditManager.cutAudio(strs[0], strs[1], strs[2], strs[3], new SingleCmdListener() {
                        @Override
                        public void next() {
                            send2Client();
                        }
                    }, true);
                }
                clientMessenger = msg.replyTo;
                break;
            default:
                clientMessenger = msg.replyTo;
                break;
            }
        }
    }

    private void send2Client() {
        if (clientMessenger != null) {
            Log.i(TAG, "FfmpegBackgroundService向客户端回信");
            Message msgToClient = Message.obtain();
            msgToClient.what = FFmpegBGConstant.SEND_MESSAGE_FINISHI;
            // 可以通过Bundle发送跨进程的信息
            Bundle bundle = new Bundle();
            bundle.putBoolean(FFmpegBGConstant.SEND_MESSAGE_FINISHI_KEY, true);
            msgToClient.setData(bundle);
            try {
                clientMessenger.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, "FfmpegBackgroundService向客户端发送信息失败: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        clientMessenger = null;
        super.onDestroy();
    }

    /**
     * 音频抽取
     *
     * @param inputPath
     * @param outputPath
     * @param changePath
     */
    private void extractMp3(String inputPath, String outputPath, final String changePath) {
        VideoEditManager.extractAudioMp3(inputPath, outputPath, new SingleCmdListener() {
            @Override
            public void next() {
                FileUtils.changePrivateFileName(changePath);
                send2Client();
                // 做视频裁剪的时候，音频抽取刚完成，然后马上做视频裁剪的音频抽取，但是读取不到、、FileUtils.changePrivateFileName(changePath)这个文件，所以让当前线程睡眠几秒
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

}
