package com.ijoysoft.mediasdk.module.opengl.filter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;
import com.ijoysoft.mediasdk.module.playControl.GlideGif;
import com.ijoysoft.mediasdk.module.playControl.GlideGifDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.glGetUniformLocation;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * gif转场，对外参数是设置颜色
 */


public class GifTransitionFilter extends AFilter {
    private final String VERTEX = "#version 100\n" +
            "attribute vec4 vPosition;\n" +
            "attribute vec2 vCoord;\n" +
            "uniform mat4 vMatrix;\n" +
            "uniform float alpha;\n" +
            "varying float vAlpha;\n" +
            "uniform vec3 color;\n" +
            "varying vec3 vColor;\n" +
            "varying vec2 textureCoordinate;\n" +
            "void main(){\n" +
            "    gl_Position = vMatrix*vPosition;\n" +
            "    textureCoordinate = vCoord;\n" +
            "    vAlpha=alpha;\n" +
            "    vColor=color;\n" +
            "}";


    private final String FRAGMENT = "#version 100\n" +
            "precision mediump float; \n" +
            "uniform sampler2D vTexture;\n" +
            "varying float vAlpha;\n" +
            "varying vec3 vColor;\n" +
            "varying vec2 textureCoordinate;\n" +
            "void main()                    \n" +
            "{\n" +
            "   gl_FragColor =vec4(vColor, 1.0) *texture2D(vTexture, textureCoordinate)*vAlpha;\n" +
            "}\n";
    private List<Bitmap> listBitmap;
    private int frameIndex;
    private int colorLocation;

    public GifTransitionFilter() {
    }

    private float[] rgb = new float[]{1.0f, 1.0f, 1.0f};

    @Override
    public void draw() {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        int frameIndex = getFrameIndex();
        if (!ObjectUtils.isEmpty(listBitmap)) {
            setTextureId(OpenGlUtils.loadTexture(listBitmap.get(frameIndex), getTextureId()));
            if (getTextureId() != NO_TEXTURE && getTextureId() != 0) {
                super.draw();
            }
            setFrameIndex(frameIndex + 1);
        }
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(VERTEX, FRAGMENT);
//        mFilter.create();
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
        colorLocation = glGetUniformLocation(mProgram, "color");
    }

    public void initGif(String path, int resourceId) {
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
//                GifDecoder mGifDecoder = new GifDecoder();
//                InputStream inputStream = null;
//                if (resourceId != 0) {
//                    inputStream = MediaSdk.getInstance().getResouce().openRawResource(resourceId);
//                } else {
//                    try {
//                        inputStream = new FileInputStream(path);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                //这里面的方法会对inpustream进行close
//                mGifDecoder.read(inputStream);
//                List<Bitmap> bitmaps = new ArrayList<>();
//                for (int i = 0; i < mGifDecoder.getFrameCount(); i++) {
//                    Bitmap b = mGifDecoder.getFrame(i);
//                    bitmaps.add(b);
//                    listBitmap = bitmaps;
//                }
                GlideGif gif;
                if (resourceId != 0) {
                    gif = GlideGifDecoder.Companion.getGif(MediaSdk.getInstance().getContext(), resourceId);
                } else {
                    gif = GlideGifDecoder.Companion.getGif(MediaSdk.getInstance().getContext(), path);
                }
                listBitmap = gif.getBitmaps();
            }
        });
    }

    @Override
    public void onSizeChanged(int width, int height) {
//        mFilter.setSize(width, height);// 注释该行代码，则进行全屏平铺拉伸
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        setFloatVec3(colorLocation, rgb);
    }


    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
//        mFilter.onDestroy();
        if (!ObjectUtils.isEmpty(listBitmap)) {
            listBitmap.clear();
        }

    }


    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        if (listBitmap != null && listBitmap.size() > 0) {
            this.frameIndex = frameIndex % listBitmap.size();
        }
    }


    public void resetData() {
        frameIndex = 0;
    }

    public void setColor(float[] color) {
        this.rgb = color;
    }

}
