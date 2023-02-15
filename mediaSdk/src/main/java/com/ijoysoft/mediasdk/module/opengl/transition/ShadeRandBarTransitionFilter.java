package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 随机线条形状
 */
public class ShadeRandBarTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;

    private static final String FRAGMENT = FRAGMENT_HEAD +
            "float randomW(float pos) {\n" +
            "    return fract(cos(pos) * 10.0);\n" +
            "}" +
            "void main() {\n" +
            "float w=20.0;" +
            "vec4 texColor1 = texture2D(vTexture, textureCoordinate);\n" +
            "vec4 texColor2 = texture2D(vTexture1, textureCoordinate);\n" +
            "float alpha = 1.0;" +
            "for (float i = 0.0; i < 20.0; i++) {\n" +
            "        float pos = randomW(i);\n" +
            "        float y0 = w * abs(textureCoordinate.x - pos) + 1.0;\n" +
            "        float y1 = w * abs(textureCoordinate.x - pos) - pos * w;\n" +
            "        alpha = min(clamp(y0 + progress * (-1.0 - pos * w), 0.0, 1.0),alpha);\n" +
            "    }" +
            "gl_FragColor = mix(texColor2,texColor1 , alpha);" +
            "}";


    public ShadeRandBarTransitionFilter(TransitionType transitionType) {
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
