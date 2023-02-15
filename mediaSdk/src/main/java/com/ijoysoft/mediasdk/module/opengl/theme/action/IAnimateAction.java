package com.ijoysoft.mediasdk.module.opengl.theme.action;

/**
 * 采用统一接口，以防进入进出的动画也有可能是非线性的，所以预留接口，实现非线性的进入停留进出的三个动画
 */
public interface IAnimateAction {

    float[] draw();
    float drawAlpha();

    float[] prepare();

    void seek(int duration);
}
