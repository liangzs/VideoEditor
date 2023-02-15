package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2;

import android.opengl.GLES20;
import android.util.Log;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 灰色和原色渐变
 */
public class MixGrayFilter extends AFilter {
    private int intensityLocation = 0;
    private long startTime = 0;


    public void onDestroy() {
        super.onDestroy();
    }

    //改变混合值的线性大小
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.readShaderFromRawResource(R.raw.theme_two_mixgray));
        intensityLocation = GLES20.glGetUniformLocation(mProgram, "intensity");

        float[] OM = MatrixUtils.getOriginalMatrix();
        MatrixUtils.flip(OM, false, true);//矩阵上下翻转
        setMatrix(OM);

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
    public void onSizeChanged(int width, int height) {
        // 显示区背景
    }


    public void setIntensityValue(float value) {
        Log.i("setIntensityValue:", "value:" + value);
        GLES20.glUniform1f(intensityLocation, value);
    }

    public void adjustImageScaling(int outputWidth, int outputHeight, int framewidth, int frameheight) {
        float ratio1 = outputWidth*1f / framewidth*1f;
        float ratio2 = outputHeight*1f / frameheight*1f;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = outputWidth*1f / imageWidthNew*1f;
        float ratioHeight = outputHeight*1f / imageHeightNew*1f;
        // 根据拉伸比例还原顶点
        float[] cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
    }


}
