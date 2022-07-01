package com.ijoysoft.mediasdk.common.global;

import android.content.Context;
import android.content.res.Resources;

/**
 * 除却一些初始化工作，也用于module调用主壳app的资源
 */
public class MediaSdk {
    private static class InstanceHolder {
        private static MediaSdk INSTANCE = new MediaSdk();
    }

    public static MediaSdk getInstance() {
        return MediaSdk.InstanceHolder.INSTANCE;
    }

    private Context context;

    /**
     * 一些参数的初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        ConstantMediaSize.init(context);
    }

    public Resources getResouce() {
        return context.getResources();
    }

    public Context getContext() {
        return context;
    }
}
