package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;

/**
 * 上滑动下来,X轴转动
 * 目前移动是匀速移动、匀速转动,后面可自定义控制加速等等
 */
public class OutEvaluate extends BaseEvaluate {
    /**
     * 根据悬停的类型，还有悬停时间，计算出的帧集合
     * 可能是存储角度，也有可能是存储偏移量
     */


    public OutEvaluate(int duration, int oriConor, int destConor, float start, float end) {
    }


    @Override
    public float[] draw() {
        transitionOM = MatrixUtils.getOriginalMatrix();
        return transitionOM;
    }

    @Override
    public float[] prepare() {
        transitionOM = MatrixUtils.getOriginalMatrix();
        return transitionOM;
    }


    @Override
    public void seek(int duration) {

    }

}
