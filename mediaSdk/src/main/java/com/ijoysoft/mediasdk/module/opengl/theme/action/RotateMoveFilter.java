package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

/**
 * 如果着色器有两个纹理的话，那么在绑定纹理的时候，从0开始，然后+1
 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
 */
public class RotateMoveFilter extends TransitionFilter {


    public static final String VERTEX_SHADER = "" + "attribute vec4 vPosition;\n" + "attribute vec2 vCoord;\n"
            + "uniform mat4 vMatrix; \n" + "varying vec2 textureCoordinate;\n" + " \n" + "void main()\n" + "{\n"
            + "    gl_Position =  vMatrix*vPosition;\n" + "    textureCoordinate = vCoord.xy;\n" + "}";

    private int mPrograssLocation;
    private int mDirecctionLocation;
    private MoveFilter moveFilter;
    private float progress;

    public RotateMoveFilter(TransitionType transitionType, MoveFilter moveFilter) {
        super(transitionType, VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.transition_move));

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        mDirecctionLocation = GLES20.glGetUniformLocation(mProgram, "direction");
        progress = 0;
    }


    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform2fv(mDirecctionLocation, 1, moveFilter.data, 0);
        if (progress <= 1.2) {
            GLES20.glUniform1f(mPrograssLocation, progress);
        }
        progress += 0.04;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public enum MoveFilter {
        XAXIS(0, new float[]{-1.0f, 0.0f}),
        YAXIS(1, new float[]{1.0f, 0.0f}),
        ZAXIS(2, new float[]{0.0f, -1.0f});
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
        GLES20.glUniform1f(mPrograssLocation, progress);
    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
}
