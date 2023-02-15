package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;

/**
 * 模拟光照缩小
 */
public class FlashZoomFilter extends AFilter {
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

    //+vec4(color, 1.0);
    public static final String NO_FILTER_FRAGMENT_SHADER = "#version 100\n" +
            " precision mediump float;\n" +
            " varying vec2 textureCoordinate;\n" +
            " uniform sampler2D vTexture;\n" +
            " uniform vec4 vColor;\n" +
            " uniform vec2 iResolution;\n" +
            " uniform float vProgress;\n" +
            " float circ(vec2 p,float progress) {\n" +
            "\tfloat r = length(p);\n" +
            "    r = sqrt(r);\n" +
            "    return abs(4.*r * fract(1. + progress));\n" +
            "}\n" +
            "void main()\n" +
            "{\n" +
            "\tvec2 uv = (textureCoordinate - .5 * iResolution.xy) / iResolution.y;\n" +
            "   \tvec3 color = vec3(.1);\n" +
            "    float rz = abs(circ(uv,vProgress));   \n" +
            "    color = vec3(.2) / rz;   \n" +
            "gl_FragColor =texture2D(vTexture, textureCoordinate)+vec4(color, 1.0);\n" +
            "}";

    private int width, height;
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private int mColorLocation;
    private int progressLocation;
    private int iResolutionLocation;
    private float progress;
    private BaseEvaluate evaluate;

    public FlashZoomFilter() {
        super();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(NO_FILTER_VERTEX_SHADER,
                NO_FILTER_FRAGMENT_SHADER);
        // 得到处理到片段着色器的vPosition成员
        mColorLocation = GLES20.glGetUniformLocation(mProgram, "vColor");
        progressLocation = GLES20.glGetUniformLocation(mProgram, "vProgress");
        iResolutionLocation = GLES20.glGetUniformLocation(mProgram, "iResolution");
        float[] OM = MatrixUtils.getOriginalMatrix();
        MatrixUtils.flip(OM, false, true);//矩阵上下翻转
        setMatrix(OM);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    //顶点坐标
    protected float pos[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };


    @Override
    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform4fv(mColorLocation, 1, rgba, 0);
        if (evaluate != null) {
            GLES20.glUniform1f(progressLocation, evaluate.getCustomValue());
//            LogUtils.v("", "evaluate.getCustomValue():" + evaluate.getCustomValue());
        }
        GLES20.glUniform2f(iResolutionLocation, width, height);
    }

    @Override
    public void draw() {
        if (evaluate != null) {
            evaluate.draw();
        }
        super.draw();
    }

    private void pureColorDraw() {
        GLES20.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void setIsPureColor(float[] rgba) {
        this.rgba = rgba;
    }

    /**
     * 进度值
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setProgressEvaluate(BaseEvaluate evaluate) {
        this.evaluate = evaluate;
    }

    public void reset() {
        if (evaluate != null) {
            evaluate.reset();
        }
    }
}
