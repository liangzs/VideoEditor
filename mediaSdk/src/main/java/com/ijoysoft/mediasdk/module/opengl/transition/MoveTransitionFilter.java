package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 如果着色器有两个纹理的话，那么在绑定纹理的时候，从0开始，然后+1
 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
 */
public class MoveTransitionFilter extends TransitionFilter {
    private int mPrograssLocation;
    private int mDirecctionLocation;
    private MoveFilter moveFilter;
    private float progress;
    public MoveTransitionFilter(TransitionType transitionType, MoveFilter moveFilter) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.transition_move));
        this.moveFilter = moveFilter;

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        mDirecctionLocation = GLES20.glGetUniformLocation(mProgram, "direction");
        LogUtils.i(TAG, "onCreate");
        progress = 0;
    }


    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform2fv(mDirecctionLocation, 1, moveFilter.data, 0);
        if (progress <= 1.2) {
            GLES20.glUniform1f(mPrograssLocation, progress);
        }
        LogUtils.i(TAG, "onDrawArraysPre:" + progress);
        progress += 0.04;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public enum MoveFilter {
        LEFT(0, new float[]{-1.0f, 0.0f}),
        RIGHT(1, new float[]{1.0f, 0.0f}),
        TOP(2, new float[]{0.0f, -1.0f}),
        BOTTOM(3, new float[]{0.0f, 1.0f}),
        RIGHT_TOP(4, new float[]{1.0f, 1.0f}),
        RIGHT_BOTTOM(5, new float[]{1.0f, -1.0f}),
        LEFT_BOTTOM(6, new float[]{-1.0f, -1.0f}),
        LEFT_TOP(7, new float[]{-1.0f, 1.0f});

        private int vChangeType;
        private float[] data;

        MoveFilter(int vChangeType, float[] data) {
            this.vChangeType = vChangeType;
            this.data = data;
        }

        public int getType() {
            return vChangeType;
        }

        public float[] data() {
            return data;
        }

    }

    /**
     * progress 0到1是执行动画的过程
     * 按照一秒25帧，那么1s的变化过程是0到0.5
     * 1s--0.5
     * 2s--1
     */
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
