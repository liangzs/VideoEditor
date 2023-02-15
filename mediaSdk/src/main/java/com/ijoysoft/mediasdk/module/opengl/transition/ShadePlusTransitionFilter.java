package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * +号形状
 */
public class ShadePlusTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private int reverseLocation;
    private boolean isReverse;

    private static final String FRAGMENT = FRAGMENT_HEAD +
            "uniform int reverse;\n" +
            "float plus(float w,float x) {\n" +
            "    float y0 = w * abs(x - 0.5) + 1.0;\n" +
            "    float y1 = w * abs(x - 0.5) - w / 2.0;\n" +
            "    float alpha = clamp(y0 + progress * (-1.0 - w / 2.0), 0.0, 1.0);\n" +
            "    return alpha;\n" +
            "}\n" +
            "float plusRe(float w, float x) {\n" +
            "    float y0 = w * abs(x - 0.5) + 1.0;\n" +
            "    float y1 = w * abs(x - 0.5) - w / 2.0;\n" +
            "    float alpha = clamp(y1 + progress * (1.0 + w / 2.0), 0.0, 1.0);\n" +
            "    return alpha;\n" +
            "}\n" +
            "void main() {\n" +
            "float w=20.0;" +
            "vec4 texColor1 = texture2D(vTexture, textureCoordinate);\n" +
            "vec4 texColor2 = texture2D(vTexture1, textureCoordinate);\n" +
            " if(reverse==0){ " +
            "float alpha = min(plus(w, textureCoordinate.x), plus(w, textureCoordinate.y));" +
            "gl_FragColor = mix(texColor1, texColor2, alpha);" +
            "   return;" +
            "} " +
            "float alpha = min(plusRe(w, textureCoordinate.x), plusRe(w, textureCoordinate.y));" +
            "gl_FragColor = mix(texColor2,texColor1 , alpha);" +
            "}";


    public ShadePlusTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        switch (transitionType) {
            case SHADE_PLUS:
                isReverse = false;
                break;
            case SHADE_PLUS_RE:
                isReverse = true;
                break;
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        reverseLocation = GLES20.glGetUniformLocation(mProgram, "reverse");
        progress = 0;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        if (isReverse) {
            GLES20.glUniform1i(reverseLocation, 1);
        } else {
            GLES20.glUniform1i(reverseLocation, 0);
        }
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
