package com.ijoysoft.mediasdk.module.opengl.transition;

import android.graphics.Matrix;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import org.libpag.PAGFile;
import org.libpag.PAGImage;

/**
 * 采用pag资源进行转场操作,为了主元素切换简便，可以挂靠一个fade的opengl的转场
 * 用于下载数据的加载
 */
public class PAGTransitionFilter extends TransitionFilter {
    //数据来源可以是asset，或者线上下载到sd的存储路径
    protected PAGFile mPAGFile;
    protected int mPrograssLocation;
    protected int wholeWidth, wholeHeight;

    public PAGTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.transition_fade));
    }


    @Override
    public void resetProperty() {
        progress = -0.3f;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        progress = -0.3f;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress < 0 ? 0 : progress);
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


    public PAGFile getPAGFile() {
        return mPAGFile;
    }

    public void setPAGFile(PAGFile PAGFile) {
        mPAGFile = PAGFile;
    }


    /**
     * pag转场的切换准备工作,设置内在pagimage的纹理替换
     */
    public void pagTransitionPrepare() {

    }

    /**
     * 补充pag转场的绘画
     */
    public void pagTransitionDraw() {

    }

    /**
     * @param wholeWidth
     * @param wholeHeight
     */
    public void setWholeWh(int wholeWidth, int wholeHeight) {
        this.wholeWidth = wholeWidth;
        this.wholeHeight = wholeHeight;
    }

    /**
     * 裁剪居中
     *
     * @return
     */
    public android.graphics.Matrix cropCenter() {
        LogUtils.i("PAGTransitionFilter", ":" + transitionType);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());
        int offsetx;
        int offsety;
        float sw = (float) width / mPAGFile.width();//9
        float sh = (float) height / mPAGFile.height();//16
        if (sh > sw) {
            offsetx = (int) (mPAGFile.width() * sh - width);
            offsety = 0;
        } else {
            offsetx = 0;
            offsety = (int) (mPAGFile.height() * sw - height);
        }
        float cropScale = Math.max(sw, sh);
        matrix.postScale(cropScale, cropScale);
        matrix.postTranslate(-offsetx / 2f, -offsety / 2f);
        //放大后平移居中
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());
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
        float sw = (float) width / mPAGFile.width();//9
        float sh = (float) height / mPAGFile.height();//16
        if (sw > sh) {
            offsetx = (int) (width - mPAGFile.width() * sh);
            offsety = 0;
        } else {
            offsetx = 0;
            offsety = (int) (height - mPAGFile.height() * sw);
        }
        float cropScale = Math.min(sw, sh);
        matrix.postScale(cropScale, cropScale);
        matrix.postTranslate(offsetx / 2f, offsety / 2f);
        //放大后平移居中
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());
        return matrix;
    }

    /**
     * 目前paper1需要特殊处理，因为pag文件无法调整索引
     */
    protected boolean dealSpecialPag(Matrix inverMatrix) {
        if (transitionType == TransitionType.PAG_INTEREST_PAPER1
                || transitionType == TransitionType.PAG_INTEREST_PAPER2
                || transitionType == TransitionType.PAG_INTEREST_PAPER3
                || transitionType == TransitionType.PAG_INTEREST_PAPER4) {
            PAGImage pagImage = PAGImage.FromTexture(getPreviewTextureId(), GLES20.GL_TEXTURE_2D, width, height, false);
            if (pagImage != null) {
                pagImage.setMatrix(inverMatrix);
                mPAGFile.replaceImage(1, pagImage);
            }

            pagImage = PAGImage.FromTexture(getTextureId(), GLES20.GL_TEXTURE_2D, width, height, false);
            if (pagImage != null) {
                pagImage.setMatrix(inverMatrix);
                mPAGFile.replaceImage(0, pagImage);
            }
            return true;
        }

        return false;
    }

}

