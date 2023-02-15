package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.content.Context;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.util.Arrays;


/**
 * 波纹
 * Created by luotangkang on 2019年1月17日11:26:14
 */
public class MagicWaveFilter extends GPUImageFilter {

    private int[] inputTextureHandles = {-1};
    private int[] inputTextureUniformLocations = {-1};

    public MagicWaveFilter(MagicFilterType magicFilterType) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.photo_edit_wave));
    }

    protected void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, inputTextureHandles, 0);
        Arrays.fill(inputTextureHandles, -1);
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
        inputTextureUniformLocations[0] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
    }

    protected void onInitialized() {
        runOnDraw(new Runnable() {
            public void run() {
                inputTextureHandles[0] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), "filter/refraction.png");
            }
        });
    }
}
