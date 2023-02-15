package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImage3x3TextureSamplingFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;


public class GPUImageToonFilter extends GPUImage3x3TextureSamplingFilter {
    private static final String TOON_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
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
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "uniform highp float intensity;\n" +
            "uniform highp float threshold;\n" +
            "uniform float strength;\n" +
            "uniform highp float quantizationLevels;\n" +
            "\n" +
            "const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "\n" +
            "float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n" +
            "float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n" +
            "float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n" +
            "float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n" +
            "float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n" +
            "float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n" +
            "float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n" +
            "float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n" +
            "float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
            "float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
            "\n" +
            "float mag = length(vec2(h, v));\n" +
            "\n" +
            "vec3 posterizedImageColor = floor((textureColor.rgb * quantizationLevels) + 0.5) / quantizationLevels;\n" +
            "\n" +
            "float thresholdTest = 1.0 - step(threshold, mag);\n" +
            "\n" +
            "gl_FragColor = vec4(mix(textureColor.rgb,posterizedImageColor * thresholdTest,strength), textureColor.a);\n" +
            "}\n";

    private float mThreshold;
    private int mThresholdLocation;
    private float mQuantizationLevels;
    private int mQuantizationLevelsLocation;

    public GPUImageToonFilter(MagicFilterType magicFilterType) {
        this(magicFilterType, 0.2F, 10.0f);
    }

    private GPUImageToonFilter(MagicFilterType magicFilterType, float threshold, float quantizationLevels) {
        super(magicFilterType, TOON_FRAGMENT_SHADER);
        mThreshold = threshold;
        mQuantizationLevels = quantizationLevels;
    }

    @Override
    public void onInit() {
        super.onInit();
        mThresholdLocation = GLES20.glGetUniformLocation(getProgram(), "threshold");
        mQuantizationLevelsLocation = GLES20.glGetUniformLocation(getProgram(), "quantizationLevels");
    }

    @Override
    public void onInitialized() {
        setFloat(mThresholdLocation, mThreshold);
        setQuantizationLevels(mQuantizationLevels);
    }


    /**
     * The levels of quantization for the posterization of colors within the scene, with a default of 10.0.
     *
     * @param quantizationLevels default 10.0
     */
    private void setQuantizationLevels(final float quantizationLevels) {
        mQuantizationLevels = quantizationLevels;
        setFloat(mQuantizationLevelsLocation, quantizationLevels);
    }
}