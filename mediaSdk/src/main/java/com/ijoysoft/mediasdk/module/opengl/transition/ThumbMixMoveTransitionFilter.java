package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.Random;

/**
 * 缩略混合移动
 */
public class ThumbMixMoveTransitionFilter extends TransitionFilter {
    private int mPrograssLocation;
    private static final String FRAGMENT = "precision mediump float;\n" +
            "varying vec2 textureCoordinate;\n" +
            "uniform float progress;\n" +
            "uniform sampler2D vTexture;\n" +
            "uniform sampler2D vTexture1;\n" +
            "#define PI 3.14159265358979323\n" +
            "#define POW2(X) X*X\n" +
            "#define POW3(X) X*X*X\n" +
            "uniform int endx; // = 2\n" +
            "uniform int endy; // = -1\n" +
            "\n" +
            "float Rand(vec2 v) {\n" +
            "  return fract(sin(dot(v.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
            "}\n" +
            "vec2 Rotate(vec2 v, float a) {\n" +
            "  mat2 rm = mat2(cos(a), -sin(a),\n" +
            "                 sin(a), cos(a));\n" +
            "  return rm*v;\n" +
            "}\n" +
            "float CosInterpolation(float x) {\n" +
            "  return -cos(x*PI)/2.+.5;\n" +
            "}\n" +
            "void main() {\n" +
            "  vec2 p = textureCoordinate.xy / vec2(1.0).xy - .5;\n" +
            "  vec2 rp = p;\n" +
            "  float rpr = (progress*2.-1.);\n" +
            "  float z = -(rpr*rpr*2.) + 3.;\n" +
            "  float az = abs(z);\n" +
            "  rp *= az;\n" +
            "  rp += mix(vec2(.5, .5), vec2(float(endx) + .5, float(endy) + .5), POW2(CosInterpolation(progress)));\n" +
            "  vec2 mrp = mod(rp, 1.);\n" +
            "  vec2 crp = rp;\n" +
            "  bool onEnd = int(floor(crp.x))==endx&&int(floor(crp.y))==endy;\n" +
            "  if(!onEnd) {\n" +
            "    float ang = float(int(Rand(crp)*4.))*.5*PI;\n" +
            "    mrp = vec2(.5) + Rotate(mrp-vec2(.5), ang);\n" +
            "  }\n" +
            "  if(onEnd || Rand(floor(crp))>.5) {\n" +
            "    gl_FragColor= texture2D(vTexture, mrp);\n" +
            "  } else {\n" +
            "    gl_FragColor=texture2D(vTexture1, mrp);\n" +
            "  }\n" +
            "}";


    private int directionXLocation;
    private int directionYLocation;

    private int directionXValue;
    private int directionYValue;

    public ThumbMixMoveTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        directionXValue = 1;
        directionYValue = 1;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        directionXLocation = GLES20.glGetUniformLocation(mProgram, "endx");
        directionYLocation = GLES20.glGetUniformLocation(mProgram, "endy");
        progress = 0;
    }

    /**
     * 这里采用超出值域的，以便三角形能走完，故设最大值为2
     */
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1i(directionXLocation, directionXValue);
        GLES20.glUniform1i(directionYLocation, directionYValue);
        LogUtils.i(TAG, "onDrawArraysPre->" + progress + "x:" + directionXValue + ",y:" + directionYValue);
        if (progress > 1) {
            progress = 1.0f;
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        progress += 0.04;

    }

    @Override
    public void seekTo(int duration) {
        float d = (float) duration / 1000.0f;
        if (d >= 1) {
            progress = 1;
        } else {
            progress = d / 2;
        }
        LogUtils.i(TAG, "seekTo:" + progress + ",origin:" + duration);
        GLES20.glUniform1f(mPrograssLocation, progress);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void resetProperty() {
        Random random = new Random();
        int value = random.nextInt(2);
        directionXValue = value > 0 ? 1 : -1;
        value = random.nextInt(2);
        directionYValue = value > 0 ? 1 : -1;
    }
}
