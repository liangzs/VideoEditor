package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * shuter 展示时移动，区别于不动的百叶窗
 */
public class ShadeMultiDiamondTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private int reverseLocation;
    private int ratioLocation;
    private boolean isReverse;
    private float ratio;
    private static final String FRAGMENT = FRAGMENT_HEAD +
            "uniform int reverse;\n" +
            "uniform float hwRatio;\n" +
            "void main() {\n" +
            "float w = 6.0;\n" +
            "float R = progress;\n" +
            "    if(reverse==1){\n" +
            "      R=1.0-progress;\n" +
            "     }\n" +
            "    float xa = abs(fract(textureCoordinate.x * w-0.5) * 2.0 - 1.0)  -R;\n" +
            "    float ya = abs(fract(textureCoordinate.y * w * hwRatio - 0.5) * 2.0 - 1.0) + R;\n" +
            "    float a = step(0.0, xa-ya);\n" +
            "    vec4 texColor1 = texture2D(vTexture1, textureCoordinate);\n" +
            "    vec4 texColor2 = texture2D(vTexture, textureCoordinate);\n" +
            "    if(reverse==0){\n" +
            "     gl_FragColor= mix(texColor2, texColor1, a);\n" +
            "     }else{\n" +
            "     gl_FragColor= mix(texColor1,texColor2 , a);" +
            "     }\n" +
            "}";


    public ShadeMultiDiamondTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        LogUtils.i("FRAGMENT", FRAGMENT);
        ratio = 1.0f;
        switch (transitionType) {
            case SHADE_MULTI_DIAMOND:
                isReverse = false;
                break;
            case SHADE_MULTI_DIAMOND_RE:
                isReverse = true;
                break;

        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        reverseLocation = GLES20.glGetUniformLocation(mProgram, "reverse");
        ratioLocation = GLES20.glGetUniformLocation(mProgram, "hwRatio");
        progress = 0;
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        if (width != 0) {
            ratio = (float) height / (float) width;
        }
    }

    public static final float TRANSITION_PROGRESS = 1.5f;

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        GLES20.glUniform1f(ratioLocation, ratio);
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
