package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 四角扩散
 */
public class ShadeFourCornerTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private static final String FRAGMENT = FRAGMENT_HEAD +
            "void main() {\n" +
            " vec2 newCoord = textureCoordinate;\n" +
            " float R=progress*0.5;             \n" +
            "    if (textureCoordinate.x > 0.5 && textureCoordinate.y > 0.5) {\n" +
            "        newCoord -= R;\n" +
            "    }\n" +
            "    else if (textureCoordinate.x > 0.5 && textureCoordinate.y < 0.5) {\n" +
            "        newCoord.x -= R;\n" +
            "        newCoord.y += R;\n" +
            "    }\n" +
            "    else if (textureCoordinate.x < 0.5 && textureCoordinate.y>0.5) {\n" +
            "        newCoord.x += R;\n" +
            "        newCoord.y -= R;\n" +
            "    }\n" +
            "    else if (textureCoordinate.x < 0.5 && textureCoordinate.y < 0.5) {\n" +
            "        newCoord += R;\n" +
            "    }\n" +
            "    vec4 texColor1 = texture2D(vTexture1, newCoord);\n" +
            "    texColor1.a = step(R, abs(fract(textureCoordinate.x) - 0.5)) * step(R, abs(fract(textureCoordinate.y) - 0.5));\n" +
            "    vec4 secondColor = texture2D(vTexture, textureCoordinate);\n" +
            "    gl_FragColor = mix(secondColor, texColor1, texColor1.a);" +
            "}";


    public ShadeFourCornerTransitionFilter(TransitionType transitionType) {
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
