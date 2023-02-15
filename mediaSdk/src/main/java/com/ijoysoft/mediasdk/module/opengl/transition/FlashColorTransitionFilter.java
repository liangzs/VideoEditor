package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 如果着色器有两个纹理的话，那么在绑定纹理的时候，从0开始，然后+1
 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
 */
public class FlashColorTransitionFilter extends TransitionFilter {
    private int mPrograssLocation;
    private int colorLocation;
    private float[] rgb = new float[]{1.0f, 1.0f, 1.0f};

    public FlashColorTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.transition_flash_color));
        if (transitionType == TransitionType.FLASH_WHITE) {
            rgb = new float[]{1.0f, 1.0f, 1.0f};
        } else if (transitionType == TransitionType.FLASH_BLACK) {
            rgb = new float[]{0.0f, 0.0f, 0.0f};
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        colorLocation = GLES20.glGetUniformLocation(mProgram, "color");
        progress = 0;
    }


    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(mPrograssLocation, progress);
        GLES20.glUniform3fv(colorLocation, 1, rgb, 0);
        progress += 0.02;
    }

    @Override
    public void seekTo(int duration) {
        float d = (float) duration / 1000.0f;
        if (d >= 2.0f) {
            progress = 1.0f;
        } else {
            progress = d / 2;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
}
