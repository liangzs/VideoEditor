package com.ijoysoft.mediasdk.module.opengl.theme.action;

public abstract class IMoveActionWrapper implements IMoveAction {
    @Override
    public boolean isRotate() {
        return false;
    }

    @Override
    public boolean isScale() {
        return false;
    }

    @Override
    public boolean isTranslate() {
        return false;
    }

    @Override
    public boolean isSkew() {
        return false;
    }
}
