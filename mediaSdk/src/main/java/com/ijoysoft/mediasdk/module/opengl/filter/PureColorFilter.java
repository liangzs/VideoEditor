package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

public class PureColorFilter extends AFilter {
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private int mColorLocation;
    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 vPosition;\n" +
            "attribute vec2 vCoord;\n" +
            "uniform mat4 vMatrix; \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position =  vMatrix*vPosition;\n" +
            "    textureCoordinate = vCoord.xy;\n" +
            "}";


    public static final String NO_FILTER_FRAGMENT_SHADER = "" +
            "#version 100\n" +
            " precision mediump float;\n" +
            " \n" +
            " varying vec2 textureCoordinate;\n" +
            " uniform sampler2D vTexture;\n" +
            " uniform vec4 vColor;\n" +
            "void main()\n" +
            "{\n" +
            "     gl_FragColor = vColor;\n" +
            "}";


    public PureColorFilter() {
        super();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(NO_FILTER_VERTEX_SHADER,
                NO_FILTER_FRAGMENT_SHADER);
        // 得到处理到片段着色器的vPosition成员
        mColorLocation = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    //顶点坐标
    protected float pos[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };

    float[] coord = new float[]{
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    @Override
    public void onSizeChanged(int width, int height) {
    }

    @Override
    protected void onDraw() {
        onUseProgram();
//        onSetExpandData();
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition, 2, GLES20.GL_FLOAT, false, 0, mVerBuffer);
        // 设置颜色
        GLES20.glUniform4fv(mColorLocation, 1, rgba, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mHPosition);
    }

    private void pureColorDraw() {
        GLES20.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void setIsPureColor(float[] rgba) {
        this.rgba = rgba;
    }

}
