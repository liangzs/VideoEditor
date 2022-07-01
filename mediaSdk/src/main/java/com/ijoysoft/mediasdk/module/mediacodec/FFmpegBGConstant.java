package com.ijoysoft.mediasdk.module.mediacodec;

public class FFmpegBGConstant {
    /*service*/
    //服务端收到消息处理类型
    public static final int RECEIVE_MESSAGE_EXTRACT_MUSIC = 1;//音频裁剪
    public static final int RECEIVE_MESSAGE_CUT_AUDIO = 2;//音频裁剪

    /*client*/
    /*客户端收到消息处理类型*/
    public static final int SEND_MESSAGE_FINISHI = 1;
    public static final String SEND_MESSAGE_FINISHI_KEY = "result";
}
