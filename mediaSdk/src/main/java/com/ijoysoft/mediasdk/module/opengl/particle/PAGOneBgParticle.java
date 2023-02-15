package com.ijoysoft.mediasdk.module.opengl.particle;

import android.graphics.Matrix;
import android.opengl.GLES20;

import androidx.annotation.Keep;

import org.libpag.PAGImage;

/**
 * 采用pag资源进行转场操作,为了主元素切换简便，可以挂靠一个fade的opengl的转场
 * 存在两个主体内容
 */
public class PAGOneBgParticle extends PAGNoBgParticle {
    private Matrix inverMatrix;

    public PAGOneBgParticle(){

    }

    @Keep
    public PAGOneBgParticle(boolean online, String path) {
        super(online, path);
    }

    @Override
    public void pagFilePrepare() {
        android.graphics.Matrix matrix = cropCenter();
        mPAGFile.setMatrix(matrix);
        inverMatrix = new Matrix();
        matrix.invert(inverMatrix);
    }

    public void updateBgTeture(int texture) {
        //底色背景纹理映射
        PAGImage pagImage = PAGImage.FromTexture(texture, GLES20.GL_TEXTURE_2D, viewWidth, viewHeight, false);
        if (pagImage != null) {
            pagImage.setMatrix(inverMatrix);
            mPAGFile.replaceImage(0, pagImage);
        }
    }
}
