package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * shuter 展示时移动，区别于不动的百叶窗
 */
public class ShadeShutterMoveTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private static final String FRAGMENT = FRAGMENT_HEAD +
            "void main() {\n" +
            "\n" +
            "    float w = 15.0;\n" +
            "    float R = 2.0 * progress - 1.0;\n" +
            "    vec4 texColor1 = texture2D(vTexture, textureCoordinate);\n" +
            "    float a = 1.0-smoothstep(0.0, 1.0, textureCoordinate.x - R);\n" +
            "    float aa = step(0.0, fract(textureCoordinate.x * w) - a);\n" +
            "    texColor1.a = aa;\n" +
            "    vec4 secondColor = texture2D(vTexture1, textureCoordinate);\n" +
            "    gl_FragColor= mix(texColor1,secondColor, aa);" +
            "}";


    public ShadeShutterMoveTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        LogUtils.i("FRAGMENT", FRAGMENT);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
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
