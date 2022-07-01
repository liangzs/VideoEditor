package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;
import android.util.Log;

@SuppressWarnings("unused")
public class WaterMarkFilter extends NoFilter {
    /**
     * 水印的放置位置和宽高
     */
    private int x, y, w, h;
    /**
     * 水印图片的bitmap
     */
    private ImageOriginFilter mFilter;
    // private OrientationNoFilter mFilter;

    public WaterMarkFilter() {
        // mFilter = new OrientationNoFilter();
        mFilter = new ImageOriginFilter();
    }

    @Override
    public void draw() {
        // glPushMatrix();
        super.draw();
        GLES20.glViewport(x, y, w, h);// 如果x,y,不为0的话，以及w，h和showHeight,showWidth不一致的话，所以在mFilter.draw()后重新设置mFilter的viewport
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        // GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // //设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        mFilter.draw();
        // GLES20.glDisable(GLES20.GL_BLEND);
        // glPopMatrix();

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mFilter.create();
        // float[] OM = MatrixUtils.getOriginalMatrix();
        // MatrixUtils.flip(OM, false, true);//矩阵上下翻转
        // mFilter.setMatrix(OM);
        float[] coord = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, };

        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        this.w = width;
        this.h = height;
        mFilter.setSize(width, height);// 注释该行代码，则进行全屏平铺拉伸
    }

    public void setPosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

    public void setWaterMarkTexure(String path) {
        mFilter.setBitmapPath(path, 0);
        mFilter.initTexture();
        Log.i("test", "涂鸦纹理：" + mFilter.getTextureId());
        // if (bitmap != null) {
        // GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
        // mFilter.setTextureId(OpenGlUtils.loadTexture(bitmap, NO_TEXTURE, true));
        // }
    }

    public int getWaterMarkTexure() {
        return mFilter.getTextureId();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFilter.onDestroy();
    }
}
