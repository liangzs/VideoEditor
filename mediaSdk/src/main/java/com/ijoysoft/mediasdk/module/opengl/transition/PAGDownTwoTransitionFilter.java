package com.ijoysoft.mediasdk.module.opengl.transition;

import android.graphics.Matrix;
import android.opengl.GLES20;

import org.libpag.PAGFile;
import org.libpag.PAGImage;

/**
 * 采用pag资源进行转场操作,为了主元素切换简便，可以挂靠一个fade的opengl的转场
 * 存在两个主体内容
 */
public class PAGDownTwoTransitionFilter extends PAGDownloadFilter {


    public PAGDownTwoTransitionFilter(TransitionType transitionType) {
        super(transitionType);
    }

    @Override
    protected void onDraw() {
        super.onDraw();
    }

    @Override
    public void pagTransitionPrepare() {
        Matrix matrix;
        if (transitionType.isInside) {//内部显示
            matrix = insideCenter();
        } else {//crop铺满模式
            matrix = cropCenter();
        }
        mPAGFile.setMatrix(matrix);
        Matrix inverMatrix = new Matrix();
        matrix.invert(inverMatrix);
        //特殊主题
        if (dealSpecialPag(inverMatrix)) {
            return;
        }

        PAGImage pagImage = PAGImage.FromTexture(getPreviewTextureId(), GLES20.GL_TEXTURE_2D, width, height, false);
        if (pagImage != null) {
            pagImage.setMatrix(inverMatrix);
            mPAGFile.replaceImage(0, pagImage);
        }
        pagImage = PAGImage.FromTexture(getTextureId(), GLES20.GL_TEXTURE_2D, width, height, false);
        if (pagImage != null) {
            pagImage.setMatrix(inverMatrix);
//            pagImage.setMatrix(pag2CurrentViewMatrix(mPAGFile, width, height));
            mPAGFile.replaceImage(1, pagImage);
        }
    }


    /**
     * 如果pag素材的宽高比例对不上的话，那么就做裁剪动作
     * 通过matrix取最大的缩放倍数
     *
     * @return
     */
    private Matrix pagtransitionMaxtrix(PAGFile pagImage, int viewWidth, int viewHeight) {
        int width = pagImage.width();
        int height = pagImage.height();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / (float) viewWidth, (float) height / (float) viewHeight);
        return matrix;
    }

    @Override
    public void pagTransitionDraw() {
        super.pagTransitionDraw();
    }
}
