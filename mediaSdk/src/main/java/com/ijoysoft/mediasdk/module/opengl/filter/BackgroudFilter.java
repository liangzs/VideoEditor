package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;
import android.util.Log;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 背景滤镜，可以是纯色， 或者当前纹理的模糊
 */
public class BackgroudFilter extends AFilter {
    public static final int ROT_0 = 0;
    public static final int ROT_90 = 90;
    public static final int ROT_180 = 180;
    public static final int ROT_270 = 270;
    //水平，垂直方向
    private int isVertical = 0;
    //    private int isVerticalLocation;
    private int resolutionLocation;
    private float[] resolutions = new float[]{0, 0};

    public BackgroudFilter() {
        super();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.readShaderFromRawResource(R.raw.gpu_round_blur));
        resolutionLocation = GLES20.glGetUniformLocation(mProgram, "iResolution");

    }


    @Override
    public void onSizeChanged(int width, int height) {
        resolutions = new float[]{ConstantMediaSize.currentScreenWidth, ConstantMediaSize.currentScreenHeight};
    }

    @Override
    protected void onDrawArraysPre() {
//        GLES20.glUniform1i(isVerticalLocation, isVertical);
        GLES20.glUniform2fv(resolutionLocation, 1, resolutions, 0);
    }

    /**
     * 模糊x或者y
     */
    public void setIsVertical(int verticalFlag) {
        isVertical = verticalFlag;
    }

    /**
     * 旋转操作
     *
     * @param rotation
     */
    public void setRotation(int rotation) {
        float[] coord = getCoord();
        switch (rotation) {
            case ROT_0:
//                coord = new float[]{
//                        0.0f, 0.0f,
//                        0.0f, 1.0f,
//                        1.0f, 0.0f,
//                        1.0f, 1.0f,
//                };
                coord = new float[]{
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                };


                break;
            case ROT_90://根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
//                coord = new float[]{
//                        1.0f, 1.0f,
//                        0.0f, 1.0f,
//                        0.0f, 0.0f,
//                        1.0f, 0.0f
//                };
                coord = new float[]{
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f
                };

                break;
            case ROT_180:
//                coord = new float[]{
//                        1.0f, 1.0f,
//                        1.0f, 0.0f,
//                        0.0f, 1.0f,
//                        0.0f, 0.0f,
//                };
                coord = new float[]{
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                };
                break;
            case ROT_270:
                coord = new float[]{
                        0.0f, 0.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f
                };
                break;
        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }


}
