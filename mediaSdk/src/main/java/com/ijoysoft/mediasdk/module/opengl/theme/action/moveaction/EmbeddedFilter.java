package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;

/**
 * @author hayring
 * @date 2022/1/5  14:18
 */
public abstract class EmbeddedFilter extends AFilter {


    /**
     * 是否销毁
     */
    boolean destroied;

    /**
     * 宽度
     */
    private float width;

    /**
     * 高度
     */
    private float height;


    /**
     * 宽高
     * 参数在程序中的位置
     */
    private int mResolutionLocation;



    /**
     * 进度
     * 参数在程序中的位置
     */
    private int mProgressLocation;

    /**
     * 进度
     */
    protected float progress;

    /**
     * 缩放
     * 参数在程序中的位置
     */
    private int mScaleLocation;

    /**
     * x轴缩放
     */
    private float scaleX = 1.0f;


    /**
     * y轴缩放
     */
    private float scaleY = 1.0f;


    /**
     * 缩放基数
     */
    private float baseScale = 1.0f;


    /**
     * 顶点着色器
     */
    protected String mVertexShader;

    /**
     * 片元着色器
     */
    protected String mFragmentShader;


    protected static final String DEFAULT_VERTEX_PATH = "shader/embedded_filter_vertex.glsl";

    @Override
    public void onCreate() {
        createProgramByAssetsFile(mVertexShader, mFragmentShader);
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
        mProgressLocation = GLES20.glGetUniformLocation(mProgram, "u_Progress");
        mResolutionLocation = GLES20.glGetUniformLocation(mProgram, "u_Resolution");
        mScaleLocation = GLES20.glGetUniformLocation(mProgram, "uScale");
        progress = 0;
    }

    /**
     * 设置其他扩展数据
     */
    @Override
    protected void onSetExpandData() {
    }


    /**
     * 绘制已加载的纹理
     * @param textureId opengl内的纹理id
     */
    public void drawTexture(int textureId) {
        setTextureId(textureId);
        draw();
    }

    /**
     * 启用顶点坐标和纹理坐标进行绘制
     */
    @Override
    protected void onDraw() {
        //GLES20.glClearColor(1, 1, 1, 1.0f);
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVerBuffer);
//        GLES20.glEnableVertexAttribArray(mHCoord);
//        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mHPosition);
//        GLES20.glDisableVertexAttribArray(mHCoord);
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        if (destroied) {
            return;
        }
        destroied = true;
        GLES20.glDeleteProgram(mProgram);
        setTextureId(-1);
    }


    /**
     * 绘制前设置数据
     */
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(mProgressLocation, progress);
        GLES20.glUniform2f(mResolutionLocation, width, height);
        GLES20.glUniform2f(mScaleLocation, scaleX * baseScale, scaleY * baseScale);
    }



    public void setProgress(float progress) {
        this.progress = progress;
    }



    public void setBaseScale(float baseScale) {
        this.baseScale = baseScale;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }


    @Override
    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
    }


    @Override
    protected void onDrawArraysAfter() {
        super.onDrawArraysAfter();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    }







    public void updateProgress(float p) {
        this.progress = p > 1 ? 1 : p;
    }




    public void changeRatio() {

    }
}
