package com.ijoysoft.mediasdk.module.opengl.particle;


import com.ijoysoft.mediasdk.module.playControl.IRender;

public interface IParticleRender extends IRender {
    void onPlepare(int index);

    void setMatrix(float[] matrix);

    /**
     * 比例更改监听器
     */
    interface OnSurfaceChangedListener {
        default void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        }
    }
}
