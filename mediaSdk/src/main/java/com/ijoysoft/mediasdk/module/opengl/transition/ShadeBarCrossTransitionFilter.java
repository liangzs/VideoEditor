package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 条形块分阶移动
 */
public class ShadeBarCrossTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private static final String FRAGMENT = FRAGMENT_HEAD +
            "void main() {\n" +
            "float w = 4.0;\n" +
            "    vec2 texCoord1 = textureCoordinate;\n" +
            "    vec2 texCoord2 = textureCoordinate;\n" +
            "    float flag = step(0.0, fract(textureCoordinate.y * w) * 2.0 - 1.0);\n" +
            "    texCoord1.x -= progress;\n" +
            "    texCoord2.x += progress;\n" +
            "    vec4 texColor1 = texture2D(vTexture1, texCoord1);\n" +
            "    texColor1 *= flag * step(progress, textureCoordinate.x);\n" +
            "    vec4 texColor2 = texture2D(vTexture1, texCoord2);\n" +
            "    texColor2 *= (1.0 - flag) * step(progress, 1.0 - textureCoordinate.x);\n" +
            "    vec4 secondColor = texture2D(vTexture, textureCoordinate);\n" +
            "    vec4 resultColor = texColor1 + texColor2;\n" +
            "    gl_FragColor = mix(secondColor, resultColor, resultColor.a); \n" +
            "}";


    public ShadeBarCrossTransitionFilter(TransitionType transitionType) {
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
