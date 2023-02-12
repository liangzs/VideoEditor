package com.qiusuo.videoeditor.module.select;

import android.content.Context;

import com.qiusuo.videoeditor.common.bean.LocalMedia;
import com.qiusuo.videoeditor.module.select.interfaces.OnCallbackListener;

import java.util.ArrayList;

/**
 * @author：luck
 * @date：2021/5/19 9:36 AM
 * @describe：CompressEngine Please use {@link CompressFileEngine}
 */
@Deprecated
public interface CompressEngine {
    /**
     * Custom compression engine
     * <p>
     * Users can implement this interface, and then access their own compression framework to plug
     * the compressed path into the {@link LocalMedia} object;
     *
     * </p>
     *
     * <p>
     * 1、LocalMedia media = new LocalMedia();
     * media.setCompressed(true);
     * media.setCompressPath("Your compressed path");
     * </p>
     * <p>
     * 2、listener.onCall( "you result" );
     * </p>
     *
     * @param context
     * @param list
     * @param listener
     */
    void onStartCompress(Context context, ArrayList<LocalMedia> list, OnCallbackListener<ArrayList<LocalMedia>> listener);
}
