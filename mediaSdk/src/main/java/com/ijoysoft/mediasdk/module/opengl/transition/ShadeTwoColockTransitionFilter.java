package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 两条线时针
 */
public class ShadeTwoColockTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private int widthLocation;
    private int width;
    private static final String FRAGMENT = FRAGMENT_HEAD +
            "uniform float width;\n" +
            "void main() {\n" +
            "vec2 newUv = floor(textureCoordinate* width) / width;\n" +
            "    vec4 texColor1 = texture2D(vTexture1, textureCoordinate);\n" +
            "    vec4 texColor2 = texture2D(vTexture, textureCoordinate);\n" +
            "    float f1 = acos(dot(vec2(1.0, 0.0), normalize(newUv - 0.5)));\n" +
            "    float f2 = acos(dot(vec2(-1.0, 0.0), normalize(newUv - 0.5)));\n" +
            "    float R = 3.2* progress;\n" +
            "    float uvY = textureCoordinate.y - 0.5;\n" +
            "    float a = 1.0;\n" +
            "    if (uvY < 0.0){\n" +
            "        if (f1 < R) {\n" +
            "            a = 0.0;\n" +
            "        } else{\n" +
            "            a = 1.0;\n" +
            "        }\n" +
            "    }else {\n" +
            "        if (f2 < R){\n" +
            "            a = 0.0;\n" +
            "        } else {\n" +
            "            a = 1.0;\n" +
            "        }\n" +
            "    }\n" +
            "    gl_FragColor = mix (texColor2, texColor1,a);" +
            "}";


    public ShadeTwoColockTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        LogUtils.i("FRAGMENT", FRAGMENT);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        widthLocation = GLES20.glGetUniformLocation(mProgram, "width");
        progress = 0;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        GLES20.glUniform1f(widthLocation, width);
        progress += 0.04;
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v(TAG, "onDrawArraysPre<-" + progress);
    }


    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        this.width = width;
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
