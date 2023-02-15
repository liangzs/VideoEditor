package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 情侣
 */
public class MagicBrannanFilter extends GPUImageFilter {
    private int[] inputTextureHandles = {-1, -1, -1, -1, -1};
    private int[] inputTextureUniformLocations = {-1, -1, -1, -1, -1};
    private int mGLStrengthLocation;

    public MagicBrannanFilter(MagicFilterType magicFilterType) {
        super(magicFilterType,NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.mediasdk_filter_brannan));
    }

    protected void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
        for (int i = 0; i < inputTextureHandles.length; i++)
            inputTextureHandles[i] = -1;
    }

    protected void onDrawArraysAfter() {
        for (int i = 0; i < inputTextureHandles.length
                && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawArraysPre() {
        for (int i = 0; i < inputTextureHandles.length
                && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
            GLES20.glUniform1i(inputTextureUniformLocations[i], (i + 3));
        }
    }

    protected void onInit() {
        super.onInit();
        for (int i = 0; i < inputTextureUniformLocations.length; i++)
            inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture" + (2 + i));
        mGLStrengthLocation = GLES20.glGetUniformLocation(mGLProgId,"strength");
    }

    protected void onInitialized() {
        super.onInitialized();
        setFloat(mGLStrengthLocation, 1.0f);
        runOnDraw(new Runnable() {
            public void run() {
                inputTextureHandles[0] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), "filter/brannan_process.png");
                inputTextureHandles[1] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), "filter/brannan_blowout.webp");
                inputTextureHandles[2] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), "filter/brannan_contrast.webp");
                inputTextureHandles[3] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), "filter/brannan_luma.webp");
                inputTextureHandles[4] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), "filter/brannan_screen.webp");
            }
        });
    }
}
