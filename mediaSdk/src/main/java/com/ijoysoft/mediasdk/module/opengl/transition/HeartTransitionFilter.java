package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;

/**
 * 如果着色器有两个纹理的话，那么在绑定纹理的时候，从0开始，然后+1
 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
 * <p>
 * textureCoordinate.x/1.7 这里的意思是，固定了9:16的比例，反归一化成比例x和y成1:1比例
 * inHeart(1.0 - p, vec2(0.5, 0.4), progress)) 这里是原来的坐标值，现在换成vec2(0.7,0.4)
 */
public class HeartTransitionFilter extends TransitionFilter {
    private int mPrograssLocation;
    private int mCoordxLocation;
    private int mCenterxLocation;
    private float mCoordx;
    private float mCenterx;
    private static final String FRAGMENT = "precision mediump float;\n" +
            "varying vec2 textureCoordinate;\n" +
            "uniform float progress;\n" +
            "uniform float coordx;\n" +
            "uniform float centerx;\n" +
            "uniform sampler2D vTexture;\n" +
            "uniform sampler2D vTexture1;\n" +
            " float inHeart (vec2 p, vec2 center, float size) {\n" +
            "        if (size==0.0) return 0.0;\n" +
            "        vec2 o = (p-center)/(1.6*size);\n" +
            "        float a = o.x*o.x+o.y*o.y-0.3;\n" +
            "        return step(a*a*a, o.x*o.x*o.y*o.y*o.y);\n" +
            "    }\n" +
            "void main(){\n" +
            " vec2 p=vec2(textureCoordinate.x/coordx,textureCoordinate.y);\n" +
            "    gl_FragColor= mix(texture2D(vTexture1, textureCoordinate), texture2D(vTexture, textureCoordinate)," +
            " inHeart(1.0 - p, vec2(centerx, 0.4), progress));\n" +
            "}";


    public HeartTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        mCoordxLocation = GLES20.glGetUniformLocation(mProgram, "coordx");
        mCenterxLocation = GLES20.glGetUniformLocation(mProgram, "centerx");
        progress = 0;
        changeRatio();
    }


    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(mPrograssLocation, progress);
        progress += 0.04;
//        LogUtils.i(TAG, "onDrawArraysPre->" + progress);
        GLES20.glUniform1f(mCoordxLocation, mCoordx);
        GLES20.glUniform1f(mCenterxLocation, mCenterx);
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
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void changeRatio() {
        switch (ConstantMediaSize.ratioType) {
            case _16_9:
            case _4_3:
                mCoordx = 0.56f;
                mCenterx = 0.1f;
                break;
            case _9_16:
            case _3_4:
                mCoordx = 1.7f;
                mCenterx = 0.7f;
                break;
            case _1_1:
                mCoordx = 1;
                mCenterx = 0.5f;
                break;
            default:
                mCoordx = 1;
                mCenterx = 0.5f;
                break;
        }
    }
}
