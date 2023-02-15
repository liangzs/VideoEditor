package com.ijoysoft.mediasdk.module.opengl.theme.action;

public interface IMoveAction {

    float[] action(float duration, float start, float end);

    boolean isTranslate();

    boolean isRotate();

    boolean isScale();

    boolean isSkew();


}
