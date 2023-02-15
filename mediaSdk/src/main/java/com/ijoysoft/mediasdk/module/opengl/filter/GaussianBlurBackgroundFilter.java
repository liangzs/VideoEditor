package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.Locale;

/**
 * Locale.ENGLISH String.foramt要使用这个，不然对于欧盟的数字格式不正确
 */
public class GaussianBlurBackgroundFilter extends AFilter {

    private float texelWidthOffset;
    private float texelHeightOffset;
    private int blurRadius;
    private double sigma;
    private boolean scale;
    private int widthOffsetLocation;
    private int heightOffsetLocation;
    private int surfaceHeight, surfaceWidth;

    private float[] cube;

    public GaussianBlurBackgroundFilter(int blurRadius, double sigma) {
        super();
        texelWidthOffset = texelHeightOffset = 0;
        scale = false;
        this.blurRadius = blurRadius;
        this.sigma = sigma;
    }


    @Override
    protected void onCreate() {
        createProgram(generateCustomizedGaussianBlurVertexShader(blurRadius, sigma),
                generateCustomizedGaussianBlurFragmentShader(blurRadius, sigma));
    }


    @Override
    public void onSizeChanged(int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;
//        if (!scale) {
//            surfaceWidth = width;
//            surfaceHeight = height;
//        } else {
//            surfaceWidth = width / 4;
//            surfaceHeight = height / 4;
//        }
    }


    @Override
    protected void onDrawArraysPre() {
        widthOffsetLocation = GLES20.glGetUniformLocation(mProgram, "texelWidthOffset");
        heightOffsetLocation = GLES20.glGetUniformLocation(mProgram, "texelHeightOffset");
        GLES20.glUniform1f(widthOffsetLocation, texelWidthOffset / surfaceWidth);
        GLES20.glUniform1f(heightOffsetLocation, texelHeightOffset / surfaceHeight);
    }

    @Override
    protected void onDrawArraysAfter() {
        super.onDrawArraysAfter();
    }

    public GaussianBlurBackgroundFilter setTexelHeightOffset(float texelHeightOffset) {
        this.texelHeightOffset = texelHeightOffset;
        return this;
    }

    public GaussianBlurBackgroundFilter setTexelWidthOffset(float texelWidthOffset) {
        this.texelWidthOffset = texelWidthOffset;
        return this;
    }


    public GaussianBlurBackgroundFilter setScale(boolean scale) {
        this.scale = scale;
        return this;
    }

    private static String generateCustomizedGaussianBlurVertexShader(int blurRadius, double sigma) {
        if (blurRadius < 1) {
            return "";
        }
        // First, generate the normal Gaussian weights for a given sigma
        double[] standardGaussianWeights = new double[blurRadius + 2];
        double sumOfWeights = 0.0;
        for (int currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            standardGaussianWeights[currentGaussianWeightIndex] = (1.0 / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2.0)))
                    * Math.exp(-Math.pow(currentGaussianWeightIndex, 2.0) / (2.0 * Math.pow(sigma, 2.0)));

            if (currentGaussianWeightIndex == 0) {
                sumOfWeights += standardGaussianWeights[currentGaussianWeightIndex];
            } else {
                sumOfWeights += 2.0 * standardGaussianWeights[currentGaussianWeightIndex];
            }
        }

        // Next, normalize these weights to prevent the clipping of the Gaussian curve at the end of the discrete samples from reducing luminance
        for (int currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            standardGaussianWeights[currentGaussianWeightIndex] = standardGaussianWeights[currentGaussianWeightIndex] / sumOfWeights;
        }

        // From these weights we calculate the offsets to read interpolated values from
        int numberOfOptimizedOffsets = Math.min(blurRadius / 2 + (blurRadius % 2), 7);
        double[] optimizedGaussianOffsets = new double[numberOfOptimizedOffsets];

        for (int currentOptimizedOffset = 0; currentOptimizedOffset < numberOfOptimizedOffsets; currentOptimizedOffset++) {
            double firstWeight = standardGaussianWeights[currentOptimizedOffset * 2 + 1];
            double secondWeight = standardGaussianWeights[currentOptimizedOffset * 2 + 2];

            double optimizedWeight = firstWeight + secondWeight;

            optimizedGaussianOffsets[currentOptimizedOffset] = (firstWeight * (currentOptimizedOffset * 2 + 1) + secondWeight * (currentOptimizedOffset * 2 + 2)) / optimizedWeight;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                        String.format(Locale.ENGLISH, "attribute vec4 vPosition;\n" +
                                        "attribute vec4 vCoord;\n" +
                                        "uniform mat4 vMatrix;\n" +
                                        "uniform float texelWidthOffset;\n" +
                                        "uniform float texelHeightOffset;\n" +
                                        "varying vec2 blurCoordinates[%d];\n" +
                                        "void main(){\n" +
                                        "\tgl_Position = vMatrix*vPosition;\n" +
                                        "\tvec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n",
                                1 + (numberOfOptimizedOffsets * 2)
                        ))
                .append("\tblurCoordinates[0] = vCoord.xy;\n");
        for (int currentOptimizedOffset = 0; currentOptimizedOffset < numberOfOptimizedOffsets; currentOptimizedOffset++) {
            stringBuilder.append(String.format(Locale.ENGLISH,
                    "\tblurCoordinates[%d] = vCoord.xy + singleStepOffset * %f;\n" +
                            "\tblurCoordinates[%d] = vCoord.xy - singleStepOffset * %f;\n",
                    (currentOptimizedOffset * 2) + 1,
                    optimizedGaussianOffsets[currentOptimizedOffset],
                    (currentOptimizedOffset * 2) + 2,
                    optimizedGaussianOffsets[currentOptimizedOffset]
            ));
        }
        stringBuilder.append("}\n");
//        LogUtils.v("", "blur:vertex-" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private static String generateCustomizedGaussianBlurFragmentShader(int blurRadius, double sigma) {
        if (blurRadius < 1) {
            return "";
        }

        // First, generate the normal Gaussian weights for a given sigma
        double[] standardGaussianWeights = new double[blurRadius + 2];
        double sumOfWeights = 0.0;
        for (int currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            standardGaussianWeights[currentGaussianWeightIndex] = (1.0 / Math.sqrt(2.0 * Math.PI
                    * Math.pow(sigma, 2.0))) * Math.exp(-Math.pow(currentGaussianWeightIndex, 2.0) / (2.0 * Math.pow(sigma, 2.0)));

            if (currentGaussianWeightIndex == 0) {
                sumOfWeights += standardGaussianWeights[currentGaussianWeightIndex];
            } else {
                sumOfWeights += 2.0 * standardGaussianWeights[currentGaussianWeightIndex];
            }
        }

        // Next, normalize these weights to prevent the clipping of the Gaussian curve at the end of the discrete samples from reducing luminance
        for (int currentGaussianWeightIndex = 0; currentGaussianWeightIndex < blurRadius + 1; currentGaussianWeightIndex++) {
            standardGaussianWeights[currentGaussianWeightIndex] = standardGaussianWeights[currentGaussianWeightIndex] / sumOfWeights;
        }

        // From these weights we calculate the offsets to read interpolated values from
        int numberOfOptimizedOffsets = Math.min(blurRadius / 2 + (blurRadius % 2), 7);
        int trueNumberOfOptimizedOffsets = blurRadius / 2 + (blurRadius % 2);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("#version 100\n" +
                        "precision highp float;\n")
                .append("uniform sampler2D vTexture;\n"
                        + "uniform highp float texelWidthOffset;\n"
                        + "uniform highp float texelHeightOffset;\n")
                .append(String.format(Locale.ENGLISH,
                                "varying highp vec2 blurCoordinates[%d];\n",
                                1 + (numberOfOptimizedOffsets * 2)
                        )
                )
                .append("vec3 hsv2rgb(vec3 c) {\n" +
                        "    const vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n" +
                        "    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n" +
                        "    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n" +
                        "}\n" +
                        "\n" +
                        "vec3 rgb2hsv(vec3 c) {\n" +
                        "    const vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n" +
                        "    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n" +
                        "    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n" +
                        "\n" +
                        "    float d = q.x - min(q.w, q.y);\n" +
                        "    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + 0.001)), d / (q.x + 0.001), q.x);\n" +
                        "}\n")
                .append("void main(){\n")
                .append("\tlowp vec4 sum = vec4(0.0);\n")
                .append(String.format(Locale.ENGLISH,
                                "\tsum += texture2D(vTexture, blurCoordinates[0]) * %f;\n",
                                standardGaussianWeights[0]
                        )
                );

        for (int currentBlurCoordinateIndex = 0; currentBlurCoordinateIndex < numberOfOptimizedOffsets; currentBlurCoordinateIndex++) {
            double firstWeight = standardGaussianWeights[currentBlurCoordinateIndex * 2 + 1];
            double secondWeight = standardGaussianWeights[currentBlurCoordinateIndex * 2 + 2];
            double optimizedWeight = firstWeight + secondWeight;
            stringBuilder.append(String.format(Locale.ENGLISH,
                            "\tsum += texture2D(vTexture, blurCoordinates[%d]) * %f;\n" +
                                    "\tsum += texture2D(vTexture, blurCoordinates[%d]) * %f;\n",
                            (currentBlurCoordinateIndex * 2) + 1, optimizedWeight,
                            (currentBlurCoordinateIndex * 2) + 2, optimizedWeight
                    )
            );
        }

        // If the number of required samples exceeds the amount we can pass in via varyings, we have to do dependent texture reads in the fragment shader
        if (trueNumberOfOptimizedOffsets > numberOfOptimizedOffsets) {
            stringBuilder.append("\thighp vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n");
            for (int currentOverlowTextureRead = numberOfOptimizedOffsets; currentOverlowTextureRead < trueNumberOfOptimizedOffsets; currentOverlowTextureRead++) {
                double firstWeight = standardGaussianWeights[currentOverlowTextureRead * 2 + 1];
                double secondWeight = standardGaussianWeights[currentOverlowTextureRead * 2 + 2];
                double optimizedWeight = firstWeight + secondWeight;
                double optimizedOffset = (firstWeight * (currentOverlowTextureRead * 2 + 1) + secondWeight * (currentOverlowTextureRead * 2 + 2)) / optimizedWeight;
                stringBuilder.append(String.format(Locale.ENGLISH,
                                "\tsum += texture2D(vTexture, blurCoordinates[0] + singleStepOffset * %f) * %f;\n" +
                                        "\tsum += texture2D(vTexture, blurCoordinates[0] - singleStepOffset * %f) * %f;\n",
                                optimizedOffset, optimizedWeight,
                                optimizedOffset, optimizedWeight
                        )
                );
            }
        }
        stringBuilder.append("vec3 rgb = rgb2hsv(sum.rgb);\n" +
                "    vec3 hsv = rgb-vec3(0.0, 0.0, 0.05);\n" +
                "    gl_FragColor = vec4(hsv2rgb(hsv), 1.0);\n").append("}\n");
//        stringBuilder.append("\tgl_FragColor = sum;\n")
//        LogUtils.v("", "blur:fagment" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static GaussianBlurBackgroundFilter initWithBlurRadiusInPixels(int blurRadiusInPixels) {
        // 7.0 is the limit for blur size for hardcoded varying offsets
        int calculatedSampleRadius = 0;
        if (blurRadiusInPixels >= 1) { // Avoid a divide-by-zero error here{
            // Calculate the number of pixels to sample from by setting a bottom limit for the contribution of the outermost pixel
            double minimumWeightToFindEdgeOfSamplingArea = 1.0 / 256.0;
            calculatedSampleRadius = (int) Math.floor(Math.sqrt(-2.0 * Math.pow(blurRadiusInPixels, 2.0) * Math.log(minimumWeightToFindEdgeOfSamplingArea *
                    Math.sqrt(2.0 * Math.PI * Math.pow(blurRadiusInPixels, 2.0)))));
            calculatedSampleRadius += calculatedSampleRadius % 2; // There's nothing to gain from handling odd radius sizes, due to the optimizations I use
        }
        return new GaussianBlurBackgroundFilter(calculatedSampleRadius, blurRadiusInPixels);
    }


    /**
     * 旋转操作,跟imageOriginfiter的旋转有所不同，根据imageOriginfiter对比重新做的旋转坐标     *
     *
     * @param rotation
     */
    public void setRotation(int rotation) {
        rotation = rotation < 0 ? rotation + 360 : rotation;
        float[] coord = getCoord();
        switch (rotation) {
            case ROT_0:
                coord = new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 0.0f,
                        1.0f, 1.0f,
                };
                break;
            case ROT_90://根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
                coord = new float[]{
                        1, 0,
                        0, 0,
                        1, 1,
                        0, 1
                };
                break;
            case ROT_180:
                coord = new float[]{
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                };
                break;
            case ROT_270:
                coord = new float[]{
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 0.0f
                };
                break;

        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    /**
     * 等比缩放图片展示，只显示窗口区域
     *
     * @param width
     * @param height
     */
    public void adjustImageScalingStretch(int mediaW, int mediaH, int rotation, int width, int height) {
        int framewidth = mediaW;
        int frameheight = mediaH;
        if ((rotation == 90 || rotation == 270)) {
            framewidth = mediaH;
            frameheight = mediaW;
        }
        float ratio1 = (1f * width) / (1f * framewidth);
        float ratio2 = (1f * height) / (1f * frameheight);
        float ratioMax = Math.max(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = (1f * width) / (1f * imageWidthNew);
        float ratioHeight = (1f * height) / (1f * imageHeightNew);
        // 根据拉伸比例还原顶点

        cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
//        setRotation(0);
    }

    /**
     * 只清理程序，因为纹理不是它的，所以不必要清理
     */
    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
    }

    /**
     * 等比缩放图片展示，只显示窗口区域
     *
     * @param width
     * @param height
     */
    public void adjustImageScalingCrop(int mediaW, int mediaH, int rotation, int width, int height) {
        int originW = mediaW;
        int originH = mediaH;
        if ((rotation == 90 || rotation == 270)) {
            originW = mediaH;
            originH = mediaW;
        }
        float ratio1 = (1f * width) / (1f * originW);
        float ratio2 = (1f * height) / (1f * originH);
        float ratioMax = Math.max(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(originW * ratioMax);
        int imageHeightNew = Math.round(originH * ratioMax);
        //频繁打印的日志放至-verbose级别中
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = (1f * width) / (1f * imageWidthNew);
        float ratioHeight = (1f * height) / (1f * imageHeightNew);
        // 根据拉伸比例还原顶点
        cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);

        setRotation(0);
    }

    public float[] getCube() {
        return cube;
    }
}
