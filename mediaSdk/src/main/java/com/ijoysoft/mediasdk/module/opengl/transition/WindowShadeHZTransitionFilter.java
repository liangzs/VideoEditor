package com.ijoysoft.mediasdk.module.opengl.transition;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * 如果着色器有两个纹理的话，那么在绑定纹理的时候，从0开始，然后+1
 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
 */
public class WindowShadeHZTransitionFilter extends TransitionFilter {

    private int countLocation;
    private int smoothnessLocation;
    private static final float COUNT = 15.0f;
    private static final float SMOOTHNESS = 10.0f;
    private int mPrograssLocation;
    private float progress;

    public WindowShadeHZTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.transition_window_shade_hz));

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        countLocation = GLES20.glGetUniformLocation(mProgram, "count");
        smoothnessLocation = GLES20.glGetUniformLocation(mProgram, "smoothness");
        progress = 0;
    }


    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(mPrograssLocation, progress);
        progress += 0.04;
        LogUtils.i(TAG, "onDrawArraysPre<-" + progress);
        GLES20.glUniform1f(countLocation, COUNT);
        GLES20.glUniform1f(smoothnessLocation, SMOOTHNESS);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void seekTo(int duration) {
        float d = (float) duration / 1000.0f;
        if (d >= 2.0f) {
            progress = 1.0f;
        } else {
            progress = d / 2;
        }
        LogUtils.i(TAG, "seekTo:" + progress + ",origin:" + duration);
        GLES20.glUniform1f(mPrograssLocation, progress);
    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
}
