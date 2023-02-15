package com.ijoysoft.mediasdk.module.opengl.filter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;

import androidx.core.graphics.PathUtils;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.global.ThreadPoolManager;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

@SuppressWarnings("unused")
public class WaterMarkFilter extends NoFilter {
    private static final String TAG = "WaterMarkFilter";
    //贴图基线，按照其基线对比进行缩放,即两个展示区域的showViewWidth进行对比
    private int x, y, w, h;
    private int angle;
    /**
     * 水印图片的bitmap
     */
    private ImageOriginFilter mFilter;

    private boolean isGif;

    private List<Bitmap> listBitmap;
    private int frameIndex;
    private Object lock = new Object();
    private boolean isTransition;

    public WaterMarkFilter() {
        mFilter = new ImageOriginFilter();
    }

    @Override
    public void draw() {
        // glPushMatrix();
        super.draw();
        GLES20.glViewport(x, y, w, h);// 如果x,y,不为0的话，以及w，h和showHeight,showWidth不一致的话，所以在mFilter.draw()后重新设置mFilter的viewport
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        if (isGif) {
            int frameIndex = getFrameIndex();
            LogUtils.i(TAG, "frameindex:" + frameIndex);
            if (!ObjectUtils.isEmpty(listBitmap)) {
                mFilter.setTextureId(OpenGlUtils.loadTexture(listBitmap.get(frameIndex), mFilter.getTextureId()));
            }
            LogUtils.i(TAG, "getTextureId:" + mFilter.getTextureId());
            if (mFilter.getTextureId() != NO_TEXTURE && mFilter.getTextureId() != 0) {
                mFilter.draw();
            }
            setFrameIndex(frameIndex + 1);
        } else {
            mFilter.draw();
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mFilter.create();
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};

        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    public void initGif(String path, int resourceId) {
        isGif = true;
        ThreadPoolMaxThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                GifDecoder mGifDecoder = new GifDecoder();
                InputStream inputStream = null;
                if (resourceId != 0) {
                    inputStream = MediaSdk.getInstance().getResouce().openRawResource(resourceId);
                } else {
                    try {
                        inputStream = new FileInputStream(path);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                //这里面的方法会对inpustream进行close
                mGifDecoder.read(inputStream);
                List<Bitmap> bitmaps = new ArrayList<>();
                for (int i = 0; i < mGifDecoder.getFrameCount(); i++) {
                    Bitmap b = mGifDecoder.getFrame(i);
                    if (b == null) {
                        continue;
                    }
                    if (Math.abs(angle) > 3) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(angle);
                        Bitmap temp = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                        bitmaps.add(temp);
                        b.recycle();
                    } else {
                        bitmaps.add(b);
                    }
                    listBitmap = bitmaps;
                }
            }
        });
    }

    @Override
    public void onSizeChanged(int width, int height) {
        mFilter.setSize(width, height);// 注释该行代码，则进行全屏平铺拉伸
    }

    /**
     * 设置贴纸坐标
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param angle
     */
    public void setPosition(float x, float y, float width, float height, float angle) {
        this.x = (int) x;
        this.y = (int) y;
        //如果angle不为0的话，则需要计算旋转后的宽高
        this.w = (int) width;
        this.h = (int) height;
        this.angle = (int) angle;
//        LogUtils.v("WaterMarkFilter","x:"+x+","+"y:"+y+","+"width:"+width+","+"height:"+height+","+"angle:"+angle);
    }

    public void setWaterMarkTexure(String path) {
        mFilter.setBitmapPath(path, 0);
        mFilter.initTexture();
    }

    public void setWaterMarkTexure(Bitmap bitmap) {
        mFilter.setBackgroundTexture(bitmap);
    }


    public int getWaterMarkTexure() {
        return mFilter.getTextureId();
    }

    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        mFilter.onDestroy();
        if (!ObjectUtils.isEmpty(listBitmap)) {
            listBitmap.clear();
        }
//        final List<Bitmap> baks = listBitmap;
//        if (!ObjectUtils.isEmpty(listBitmap)) {
//            for (Bitmap bitmap : baks) {
//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }
//            }
//        }

    }


    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        if (listBitmap != null && listBitmap.size() > 0) {
            this.frameIndex = frameIndex % listBitmap.size();
        }
    }


    public void resetGif() {
        frameIndex = 0;
    }

    public boolean isTransition() {
        return isTransition;
    }

    public void setTransition(boolean transition) {
        isTransition = transition;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
