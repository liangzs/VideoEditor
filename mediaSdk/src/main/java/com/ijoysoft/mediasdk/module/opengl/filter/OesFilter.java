package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * Description: 加载默认的滤镜的filter
 */
public class OesFilter extends AFilter{

    public OesFilter() {
        super();
    }

    @Override
    public void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/oes_base_vertex.sh"),OpenGlUtils.uRes("shader/oes_base_fragment.sh"));
        float[] coord = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
        };
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    @Override
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType());
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,getTextureId());
        GLES20.glUniform1i(mHTexture,getTextureType());
//        //配置边缘过渡参数
//        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    @Override
    public void onSizeChanged(int width, int height) {

    }

    /**
     * 背景默认为黑色
     */
    @Override
    protected void onClear() {
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setMatrix(float[] matrix) {
        LogUtils.v("OesFilter","matrix:"+matrix);
        super.setMatrix(matrix);
    }
}
