package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.util.Arrays;

/**
 * 咖啡
 */
public class MagicEarlyBirdFilter extends GPUImageFilter {
    private int[] inputTextureHandles = {-1, -1, -1, -1, -1};
    private int[] inputTextureUniformLocations = {-1, -1, -1, -1, -1};

    public MagicEarlyBirdFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_earlybird));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
        Arrays.fill(inputTextureHandles, -1);
    }

    @Override
    protected void onDrawArraysAfter() {
        for (int i = 0; i < inputTextureHandles.length && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    @Override
    protected void onDrawArraysPre() {
        for (int i = 0; i < inputTextureHandles.length && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
            GLES20.glUniform1i(inputTextureUniformLocations[i], (i + 3));
        }
    }

    @Override
    protected void onInit() {
        super.onInit();
        for (int i = 0; i < inputTextureUniformLocations.length; i++) {
            inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture" + (2 + i));
        }
    }

    @Override
    protected void onInitialized() {
        runOnDraw(new Runnable() {
            public void run() {
                inputTextureHandles[0] = OpenGlUtils.loadTexture("filter/earlybirdcurves.png");
                inputTextureHandles[1] = OpenGlUtils.loadTexture("filter/earlybirdoverlaymap_new.png");
                inputTextureHandles[2] = OpenGlUtils.loadTexture("filter/vignettemap_new.webp");
                inputTextureHandles[3] = OpenGlUtils.loadTexture("filter/earlybirdblowout.png");
                inputTextureHandles[4] = OpenGlUtils.loadTexture("filter/earlybirdmap.png");
            }
        });
    }

}
