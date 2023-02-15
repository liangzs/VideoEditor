package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author hayring
 * @date 2022/1/6  17:41
 */
public class Firework extends AFilter implements IRender {

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
     * 颜色
     * 参数在程序中的位置
     */
    private int mColorLocation;



    /**
     * 颜色
     */
    float[] color = new float[]{1f, 1f, 1f};

    /**
     * 中心x轴
     */
    private float centerX = 0f;

    /**
     * 中心y轴
     */
    private float centerY = 0f;


    /**
     * 中心坐标
     * 参数在程序中的位置
     */
    private int mCenterLocation;





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
     *
     */
    int frameIndex = 0;

    /**
     *
     */
    int frameCount;



    /**
     * 顶点着色器
     */
    protected String mVertexShader;

    /**
     * 片元着色器
     */
    protected String mFragmentShader;


    protected static final String VERTEX_PATH = "shader/firework_vertex.glsl";

    protected static final String FRAGMENT_PATH = "shader/firework_fragment.glsl";



    @Override
    public void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes(VERTEX_PATH), OpenGlUtils.uRes(FRAGMENT_PATH));
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
        mProgressLocation = GLES20.glGetUniformLocation(mProgram, "u_Progress");
        mResolutionLocation = GLES20.glGetUniformLocation(mProgram, "u_Resolution");
        mColorLocation = GLES20.glGetUniformLocation(mProgram, "u_BaseColor");
        mCenterLocation = GLES20.glGetUniformLocation(mProgram, "u_Center");
        progress = 0;
    }

    /**
     * 设置其他扩展数据
     */
    @Override
    protected void onSetExpandData() {
    }



    /**
     * 启用顶点坐标和纹理坐标进行绘制
     */
    @Override
    protected void onDraw() {
        frameIndex++;
        progress = ((float) frameIndex) / frameCount;
//        GLES20.glClearColor(1, 1, 1, 0.0f);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVerBuffer);
        GLES20.glEnableVertexAttribArray(mHCoord);
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHCoord);
        if (frameCount == frameIndex) {
            frameIndex = 0;
        }
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
        GLES20.glUniform2f(mCenterLocation, centerX, centerY);
        GLES20.glUniform3f(mColorLocation, color[0], color[1], color[2]);
    }



    public void setProgress(float progress) {
        updateProgress(progress);
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
        this.progress = p > 1 ? 1 : 0;
        frameIndex =(int) Math.ceil(progress * frameCount);
    }




    public void changeRatio() {

    }




    @Override
    public void onSurfaceCreated() {
        onCreate();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        onSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame() {
        draw();
    }


    @Override
    public void draw() {
        onClear();
        onUseProgram();
        onSetExpandData();
        onDrawArraysPre();
        onDraw();
        onDrawArraysAfter();
    }

    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }



    public void setColor(float r, float g, float b) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }


    public void setDuration(int duration) {
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
    }




}
