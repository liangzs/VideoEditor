package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * y转动号形状
 */
public class RotateAxisYTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;

    private static final String FRAGMENT = FRAGMENT_HEAD +
            "#define PI 3.1415926 \n" +
            "vec2 transform(vec2 texCoord,float theta,float zOffset)\n" +
            "{\n" +
            "    vec2 res = texCoord - 0.5;   \n" +
            "    res.x = res.x / cos(theta);\n" +
            "    res.y = res.y / (1.0 - res.x * sin(theta));\n" +
            "    res.x = res.x  / (1.0 - res.x * sin(theta));\n" +
            "    res = res * (1.0 + zOffset);    \n" +
            "    res = res + 0.5;    \n" +
            "    return res;\n" +
            "}" +
            "void main() {\n" +
            "   float zOffset = 0.2 - abs(0.4*progress - 0.2);\n" +
            "    vec2 texCoordAfterTransform = transform(textureCoordinate, progress*PI, zOffset);\n" +
            "    vec4 resColor = vec4(progress,0.0,0.0,1.0);\n" +
            "    vec4 texColor1 = texture2D(vTexture1, texCoordAfterTransform);\n" +
            "    vec4 texColor2 = texture2D(vTexture, vec2(1.0 - texCoordAfterTransform.x, texCoordAfterTransform.y));\n" +
            "    if (progress <= 0.5){\n" +
            "        resColor = texColor1;}\n" +
            "    else{\n" +
            "        resColor = texColor2;}\n" +
            "    gl_FragColor = resColor;" +
            "}";


    public RotateAxisYTransitionFilter(TransitionType transitionType) {
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
