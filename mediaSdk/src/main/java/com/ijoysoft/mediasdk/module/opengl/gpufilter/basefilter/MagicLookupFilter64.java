package com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;


public class MagicLookupFilter64 extends GPUImageFilter {

    public static final String LOOKUP_FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2;\n" +
            " uniform float strength;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     mediump float blueColor = textureColor.b * 15.0;\n" +
            "     \n" +
            "     mediump vec2 quad1;\n" +
            "     quad1.y = floor(floor(blueColor) / 4.0);\n" +
            "     quad1.x = floor(blueColor) - (quad1.y * 4.0);\n" +
            "     \n" +
            "     mediump vec2 quad2;\n" +
            "     quad2.y = floor(ceil(blueColor) / 4.0);\n" +
            "     quad2.x = ceil(blueColor) - (quad2.y * 4.0);\n" +
            "     \n" +
            "     highp vec2 texPos1;\n" +
            "     texPos1.x = (quad1.x * 0.25) + ((15.0/64.0) * textureColor.r);\n" +
            "     texPos1.y = (quad1.y * 0.25) + ((15.0/64.0) * textureColor.g);\n" +
            "     \n" +
            "     highp vec2 texPos2;\n" +
            "     texPos2.x = (quad2.x * 0.25) + ((15.0/64.0) * textureColor.r);\n" +
            "     texPos2.y = (quad2.y * 0.25) + ((15.0/64.0) * textureColor.g);\n" +
            "     \n" +
            "     lowp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n" +
            "     lowp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n" +
            "     \n" +
            "     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n" +
            "     gl_FragColor = mix(textureColor,vec4(newColor.rgb, textureColor.w),strength);\n" +
            " }";

    protected Object table;

    public MagicLookupFilter64(MagicFilterType magicFilterType, Object table) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, LOOKUP_FRAGMENT_SHADER);
        this.table = table;
    }

    public int mLookupTextureUniform;
    public int mLookupSourceTexture = OpenGlUtils.NO_TEXTURE;

    @Override
    protected void onInit() {
        super.onInit();
        mLookupTextureUniform = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
    }

    @Override
    protected void onInitialized() {
        runOnDraw(new Runnable() {
            public void run() {
                mLookupSourceTexture = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), table);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int[] texture = new int[]{mLookupSourceTexture};
        GLES20.glDeleteTextures(1, texture, 0);
        mLookupSourceTexture = -1;
    }

    @Override
    protected void onDrawArraysAfter() {
        if (mLookupSourceTexture != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    @Override
    protected void onDrawArraysPre() {
        if (mLookupSourceTexture != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLookupSourceTexture);
            GLES20.glUniform1i(mLookupTextureUniform, 3);
        }
    }

    public String getDownloadPath() {
        return null;
    }

//    @Override
//    public String getFilterName() {
//        if (thumbDrawableId == R.drawable.filter_thumb_movie1) {
//            return GpuFilterFactory.NAME_LOOKUP;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie1) {
//            return GpuFilterFactory.NAME_LOOKUP1;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie2) {
//            return GpuFilterFactory.NAME_LOOKUP2;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie3) {
//            return GpuFilterFactory.NAME_LOOKUP3;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie4) {
//            return GpuFilterFactory.NAME_LOOKUP4;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie5) {
//            return GpuFilterFactory.NAME_LOOKUP5;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie6) {
//            return GpuFilterFactory.NAME_LOOKUP6;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie7) {
//            return GpuFilterFactory.NAME_LOOKUP7;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie8) {
//            return GpuFilterFactory.NAME_LOOKUP8;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie9) {
//            return GpuFilterFactory.NAME_LOOKUP9;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie10) {
//            return GpuFilterFactory.NAME_LOOKUP10;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie11) {
//            return GpuFilterFactory.NAME_LOOKUP11;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie12) {
//            return GpuFilterFactory.NAME_LOOKUP12;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie13) {
//            return GpuFilterFactory.NAME_LOOKUP13;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_movie14) {
//            return GpuFilterFactory.NAME_LOOKUP14;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food1) {
//            return GpuFilterFactory.NAME_LOOKUP15;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food2) {
//            return GpuFilterFactory.NAME_LOOKUP16;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food3) {
//            return GpuFilterFactory.NAME_LOOKUP17;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food4) {
//            return GpuFilterFactory.NAME_LOOKUP18;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food5) {
//            return GpuFilterFactory.NAME_LOOKUP19;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food6) {
//            return GpuFilterFactory.NAME_LOOKUP20;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food7) {
//            return GpuFilterFactory.NAME_LOOKUP21;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food8) {
//            return GpuFilterFactory.NAME_LOOKUP22;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food9) {
//            return GpuFilterFactory.NAME_LOOKUP23;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food10) {
//            return GpuFilterFactory.NAME_LOOKUP24;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food11) {
//            return GpuFilterFactory.NAME_LOOKUP25;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food12) {
//            return GpuFilterFactory.NAME_LOOKUP26;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food13) {
//            return GpuFilterFactory.NAME_LOOKUP27;
//        } else if (thumbDrawableId == R.drawable.filter_thumb_food14) {
//            return GpuFilterFactory.NAME_LOOKUP28;
//        } else {
//            return GpuFilterFactory.NAME_LOOKUP29;
//        }
//    }
}
