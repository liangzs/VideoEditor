package com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

/**
 * 卡通filter父类
 * Created by Administrator on 2016/12/8.
 */

public class GPUImage3x3TextureSamplingFilter extends GPUImageFilter {
    public static final String THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "\n" +
            "uniform highp float texelWidth; \n" +
            "uniform highp float texelHeight; \n" +
            "\n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate;\n" +
            "\n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 topLeftTextureCoordinate;\n" +
            "varying vec2 topRightTextureCoordinate;\n" +
            "\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            "varying vec2 bottomLeftTextureCoordinate;\n" +
            "varying vec2 bottomRightTextureCoordinate;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "\n" +
            "    vec2 widthStep = vec2(texelWidth, 0.0);\n" +
            "    vec2 heightStep = vec2(0.0, texelHeight);\n" +
            "    vec2 widthHeightStep = vec2(texelWidth, texelHeight);\n" +
            "    vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);\n" +
            "\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n" +
            "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n" +
            "\n" +
            "    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n" +
            "    topLeftTextureCoordinate = inputTextureCoordinate.xy - widthHeightStep;\n" +
            "    topRightTextureCoordinate = inputTextureCoordinate.xy + widthNegativeHeightStep;\n" +
            "\n" +
            "    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n" +
            "    bottomLeftTextureCoordinate = inputTextureCoordinate.xy - widthNegativeHeightStep;\n" +
            "    bottomRightTextureCoordinate = inputTextureCoordinate.xy + widthHeightStep;\n" +
            "}";

    private int mUniformTexelWidthLocation;
    private int mUniformTexelHeightLocation;

    private boolean mHasOverriddenImageSizeFactor = false;
    private float mTexelWidth;
    private float mTexelHeight;
    private float mLineSize = 1.0f;

    public GPUImage3x3TextureSamplingFilter(MagicFilterType magicFilterType) {
        this(magicFilterType, NO_FILTER_VERTEX_SHADER);
    }

    public GPUImage3x3TextureSamplingFilter(MagicFilterType magicFilterType, final String fragmentShader) {
        super(magicFilterType, THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER, fragmentShader);
    }

    @Override
    public void onInit() {
        super.onInit();
        mUniformTexelWidthLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidth");
        mUniformTexelHeightLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeight");
        if (mTexelWidth != 0) {
            updateTexelValues();
        }
    }


    @Override
    public void onDisplaySizeChanged(int width, int height) {
        super.onDisplaySizeChanged(width, height);
        if (!mHasOverriddenImageSizeFactor) {
            setLineSize(mLineSize);
        }
    }

    public void setTexelWidth(final float texelWidth) {
        mHasOverriddenImageSizeFactor = true;
        mTexelWidth = texelWidth;
        setFloat(mUniformTexelWidthLocation, texelWidth);
    }

    public void setTexelHeight(final float texelHeight) {
        mHasOverriddenImageSizeFactor = true;
        mTexelHeight = texelHeight;
        setFloat(mUniformTexelHeightLocation, texelHeight);
    }

    public void setLineSize(final float size) {
        mLineSize = size;
        mTexelWidth = size / mOutputWidth;
        mTexelHeight = size / mOutputHeight;
        updateTexelValues();
    }

    private void updateTexelValues() {
        setFloat(mUniformTexelWidthLocation, mTexelWidth);
        setFloat(mUniformTexelHeightLocation, mTexelHeight);
    }
}