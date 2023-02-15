package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 网格三角形过渡
 */
public class ShadeMultiTriangleTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private int ratioLocation;
    private float ratio;
    private static final String FRAGMENT = FRAGMENT_HEAD +
            "uniform float hwRatio;\n" +
            "void main() {\n" +
            "   float w=6.0;\n" +
            "   float R = 2.0 * progress - 1.0;\n" +
            "    vec4 texColor1 = texture2D(vTexture1, textureCoordinate);\n" +
            "    vec4 texColor2 = texture2D(vTexture, textureCoordinate);\n" +
            "    float f1 =abs(fract(textureCoordinate.x * w)) * 2.0 - 1.0 - R;\n" +
            "    float f2 =abs(fract(textureCoordinate.y * w )) * 2.0 - 1.0 + R;\n" +
            "    float a = step(0.0,f2 - f1);\n" +
            "    gl_FragColor = mix(texColor1,texColor2, a);" +
            "}";



    public ShadeMultiTriangleTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        ratio = 1.0f;
        LogUtils.i("FRAGMENT", FRAGMENT);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        ratioLocation = GLES20.glGetUniformLocation(mProgram, "hwRatio");
        progress = 0;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        progress += 0.04;
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v(TAG, "onDrawArraysPre<-" + progress);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        if (width != 0) {
            ratio = (float) height / (float) width;
        }
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
        GLES20.glUniform1f(ratioLocation, ratio);
    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
}
