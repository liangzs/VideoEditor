package com.qiusuo.videoeditor.ui.widgegt.guide.listener;

import android.view.View;

import com.qiusuo.videoeditor.ui.widgegt.guide.core.Controller;

/**
 * Created by hubert on 2018/2/12.
 * <p>
 * 用于引导层布局初始化
 */

public interface OnLayoutInflatedListener {

    /**
     * @param controller {@link Controller}
     */
    void onLayoutInflated(View view, Controller controller);
}
