package com.ijoysoft.mediasdk.module.opengl.particle;

import androidx.annotation.Keep;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.LogUtils;

import org.libpag.PAGFile;

/**
 * pag粒子系统，挂载一个pag文件，进行循环播放，唯一要做的事情是比例的适配
 */

public class PAGNoBgParticle implements IParticleRender {
    protected PAGFile mPAGFile;
    protected int viewWidth;
    protected int viewHeight;


    public PAGNoBgParticle() {

    }

    @Keep
    public PAGNoBgParticle(boolean online, String path) {
        if (online) {
            mPAGFile = PAGFile.Load(path);
        } else {
            mPAGFile = PAGFile.Load(MediaSdk.getInstance().getResouce().getAssets(), path);
        }
    }

    public PAGNoBgParticle(PAGFile mPAGFile) {
        this.mPAGFile = mPAGFile;
    }

    public void setmPAGFile(PAGFile pagFile) {
        mPAGFile = pagFile;
    }

    /**
     * 居中裁剪
     */
    public void pagFilePrepare() {
        android.graphics.Matrix matrix = cropCenter();
        mPAGFile.setMatrix(matrix);
    }

    @Override
    public void onPlepare(int index) {

    }

    @Override
    public void setMatrix(float[] matrix) {

    }

    @Override
    public void onSurfaceCreated() {

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        this.viewWidth = width;
        this.viewHeight = height;
    }

    @Override
    public void onDestroy() {

    }


    public PAGFile getPAGFile() {
        return mPAGFile;
    }

    public void setPAGFile(PAGFile PAGFile) {
        mPAGFile = PAGFile;
    }

    /**
     * 裁剪居中
     *
     * @return
     */
    public android.graphics.Matrix cropCenter() {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
//        LogUtils.v("PAGNoBgParticle", ":" + mPAGFile.width() + "," + mPAGFile.height());
        int offsetx;
        int offsety;
        float sw = (float) viewWidth / mPAGFile.width();//9
        float sh = (float) viewHeight / mPAGFile.height();//16
        if (sh > sw) {
            offsetx = (int) (mPAGFile.width() * sh - viewWidth);
            offsety = 0;
        } else {
            offsetx = 0;
            offsety = (int) (mPAGFile.height() * sw - viewHeight);
        }
        float cropScale = Math.max(sw, sh);
        matrix.postScale(cropScale, cropScale);
        matrix.postTranslate(-offsetx / 2f, -offsety / 2f);
        //放大后平移居中
//        LogUtils.v("PAGNoBgParticle", ":" + mPAGFile.width() + "," + mPAGFile.height());
        return matrix;
    }


    /**
     * inside居中
     *
     * @return
     */
    public android.graphics.Matrix insideCenter() {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());
        int offsetx;
        int offsety;
        float sw = (float) viewWidth / mPAGFile.width();//9
        float sh = (float) viewHeight / mPAGFile.height();//16
        if (sw > sh) {
            offsetx = (int) (viewWidth - mPAGFile.width() * sh);
            offsety = 0;
        } else {
            offsetx = 0;
            offsety = (int) (viewHeight - mPAGFile.height() * sw);
        }
        float cropScale = Math.min(sw, sh);
        matrix.postScale(cropScale, cropScale);
        matrix.postTranslate(offsetx / 2f, offsety / 2f);
        //放大后平移居中
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());
        return matrix;
    }
}
