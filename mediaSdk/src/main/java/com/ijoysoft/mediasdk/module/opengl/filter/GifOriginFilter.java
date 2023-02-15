package com.ijoysoft.mediasdk.module.opengl.filter;

import static android.opengl.GLES20.glGetUniformLocation;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;
import com.ijoysoft.mediasdk.module.playControl.GlideGif;
import com.ijoysoft.mediasdk.module.playControl.GlideGifDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * gif控件，对外参数是设置颜色
 */


public class GifOriginFilter extends AFilter {
    private final String VERTEX = "#version 100\n" +
            "//gif vertex\n" +
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
            "//gif fragment\n" +
            "precision mediump float; \n" +
            "uniform sampler2D vTexture;\n" +
            "varying float vAlpha;\n" +
            "varying vec3 vColor;\n" +
            "varying vec2 textureCoordinate;\n" +
            "void main()                    \n" +
            "{\n" +
            "   gl_FragColor =vec4(vColor, 1.0) *texture2D(vTexture, textureCoordinate)*vAlpha;\n" +
            "}\n";

    private List<GifDecoder.GifFrame> frames;


    /**
     * gif中帧的位置
     */
    private int frameIndex;


    /**
     * 过去的时间
     */
    private int passedMillisecond;

    /**
     * full image width
     */
    protected int width;

    /**
     * full image height
     */
    protected int height;

    private int colorLocation;

    public GifOriginFilter() {
    }

    private float[] rgb = new float[]{1.0f, 1.0f, 1.0f};

    @Override
    public void draw() {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子

        if (!ObjectUtils.isEmpty(frames)) {
            GifDecoder.GifFrame frame = frames.get(frameIndex);
            if (frame.image == null || frame.image.isRecycled()) {
                return;
            }
            setTextureId(OpenGlUtils.loadTexture(frame.image, getTextureId()));
            if (getTextureId() != NO_TEXTURE && getTextureId() != 0) {
//                LogUtils.v(getClass().getSimpleName(), "gif drawing, frame index:" + frameIndex);
                super.draw();
            } else {
                LogUtils.e(getClass().getSimpleName(), "gif draw failed");
            }
            passedMillisecond += ConstantMediaSize.PHTOTO_FPS_TIME;
            while (passedMillisecond >= frame.delay) {
                passedMillisecond -= frame.delay;
                nextFrame();
            }
        }
    }

    @Override
    public void onCreate() {
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
//                frames = mGifDecoder.getFrames();
//                for (int i = 0; i < frames.size(); i++) {
//                    if (frames.get(i).delay == 0) {
//                        frames.get(i).delay = frames.get(i - 1).delay;
//                    }
//                }
                GlideGif gif;
                if (resourceId != 0) {
                    gif = GlideGifDecoder.Companion.getGif(MediaSdk.getInstance().getContext(), resourceId);
                } else {
                    gif = GlideGifDecoder.Companion.getGif(MediaSdk.getInstance().getContext(), path);
                }
                frames = gif.getFrames();
                width = gif.getWidth();
                height = gif.getHeight();
            }
        });
    }

    public void setFrames(List<GifDecoder.GifFrame> frames) {
        this.frames = frames;
        if (frames.isEmpty()) {
            return;
        }
        width = frames.get(0).image.getWidth();
        height = frames.get(0).image.getHeight();
    }


    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        setFloatVec3(colorLocation, rgb);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void nextFrame() {
        frameIndex = (frameIndex + 1) % frames.size();
    }


    public void resetData() {
        if (!ObjectUtils.isEmpty(frames)) {
            frameIndex = 0;
        }
    }

    public void setColor(float[] color) {
        this.rgb = color;
    }


    float[] cube;

    public float[] adjustScaling(int showWidth, int showHeight, float centerX, float centerY, float scale) {
        return adjustScaling(showWidth, showHeight, centerX, centerY, scale, scale);
    }

    /**
     * float ratioWidth = imageWidthNew / outputWidth; 相当于归一化坐标，然后 ratioWidth / 4,
     * ratioHeight / 8是换算到总的归一化坐标
     *
     * @param showWidth  渲染宽度
     * @param showHeight 渲染高度
     * @param centerX    中点x轴相对坐标
     * @param centerY    中点y轴相对坐标
     * @param scalex     x轴缩放的程度（比例的倒数）
     * @param scaley     y轴缩放的程度（比例的倒数）
     */
    public float[] adjustScaling(int showWidth, int showHeight, float centerX, float centerY, float scalex, float scaley) {
        //图片宽高相对渲染宽高缩小的程度
        float ratio1 = (float) showWidth / width;
        float ratio2 = (float) showHeight / height;
        //哪一个轴缩放的程度最小，就说明等比例拉伸后该轴长先达到渲染(宽/高)的长度
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        /**
         * @author hayring将该图片等比拉伸至刚才测量的值，即某一方向上的长度先达到与渲染（长/宽）相等
         */
        int imageWidthNew = Math.round(width * ratioMax);
        int imageHeightNew = Math.round(height * ratioMax);
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v("adjustScaling", "outputWidth:" + (float) showWidth + ",outputHeight:" + (float) showHeight + ",imageWidthNew:"
//                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点
        cube = new float[]{centerX - ratioWidth / scalex, centerY + ratioHeight / scaley, centerX - ratioWidth / scalex, centerY - ratioHeight / scaley,
                centerX + ratioWidth / scalex, centerY + ratioHeight / scaley, centerX + ratioWidth / scalex, centerY - ratioHeight / scaley};
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "gif cube:" + Arrays.toString(cube));
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }


    /**
     * 上下左右靠边缩放，四个方向和四个角落都支持
     *
     * @param orientation T,B,L,R,LT,LB,RT,RB
     * @param coord       自定义x/y的位置 当orientation = L,T,R,B
     */
    public float[] adjustScaling(int showWidth, int showHeight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        float ratio1 = (float) showWidth / width;
        float ratio2 = (float) showHeight / height;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(width * ratioMax);
        int imageHeightNew = Math.round(height * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = 2f * imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点


        cube = pos;
        switch (orientation) {
            case LEFT:
                ratioHeight /= 2f;
                cube = new float[]{-1f, coord + ratioHeight / scale,
                        -1f, coord - ratioHeight / scale,
                        -1f + 2f * ratioWidth / scale, coord + ratioHeight / scale,
                        -1f + 2f * ratioWidth / scale, coord - ratioHeight / scale};
                break;
            case RIGHT:
                ratioHeight /= 2f;
                cube = new float[]{1 - 2f * ratioWidth / scale, coord + ratioHeight / scale,
                        1f - 2f * ratioWidth / scale, coord - ratioHeight / scale,
                        1f, coord + ratioHeight / scale,
                        1f, coord - ratioHeight / scale};
                break;
            case BOTTOM:
                cube = new float[]{coord - ratioWidth / scale, 1f,
                        coord - ratioWidth / scale, 1 - ratioHeight / scale,
                        coord + ratioWidth / scale, 1,
                        coord + ratioWidth / scale, 1 - ratioHeight / scale};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                cube = new float[]{coord - ratioWidth / scale, ratioHeight / scale - 1f,
                        coord - ratioWidth / scale, -1f,
                        coord + ratioWidth / scale, ratioHeight / scale - 1,
                        coord + ratioWidth / scale, -1f};
                break;
            case LEFT_TOP:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{-1.0f, ratioHeight / scale - 1.0f,
                        -1.0f, -1.0f,
                        ratioWidth / scale - 1.0f, ratioHeight / scale - 1.0f,
                        ratioWidth / scale - 1.0f, -1.0f};
                break;
            case LEFT_BOTTOM:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{-1.0f, 1.0f, -1.0f, 1.0f - ratioHeight / scale,
                        ratioWidth / scale - 1.0f, 1.0f, ratioWidth / scale - 1.0f, 1.0f - ratioHeight / scale};
                break;
            case RIGHT_TOP:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{1.0f - ratioWidth / scale, ratioHeight / scale - 1.0f, 1.0f - ratioWidth / scale, -1.0f,
                        1.0f, ratioHeight / scale - 1.0f, 1.0f, -1.0f};
                break;
            case RIGHT_BOTTOM:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{1.0f - ratioWidth / scale, 1.0f, 1.0f - ratioWidth / scale, 1.0f - ratioHeight / scale,
                        1.0f, 1.0f, 1.0f, 1.0f - ratioHeight / scale};
                break;
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }

    /**
     * 针对一些部件，比如锦旗，需要铺满屏幕的。做等比例的高拉伸
     *
     * @param showWidth
     * @param showHeight
     * @return
     */
    public float[] adjustScalingFitX(int showWidth, int showHeight, AnimateInfo.ORIENTATION orientation) {
        float ratio1 = (float) showWidth / width;
        // 居中后图片显示的大小
        float imageHeightNew = height * ratio1;
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioH = imageHeightNew / showHeight;//-1到1所以为2
        // 根据拉伸比例还原顶点
        cube = pos;
        switch (orientation) {
            case BOTTOM:
                //检测是否超越了showHeight的一半长度，如果超过了一半，那么需要以0为坐标进行重新计算了
                //y=1-2*imageHeightNew / showHeight
                ratioH = 1 - 2 * ratioH;
                cube = new float[]{-1f, 1f, -1f, ratioH,
                        1f, 1, 1f, ratioH};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                //y=2*imageHeightNew / showHeight-1
                ratioH = 2 * ratioH - 1;
                cube = new float[]{-1f, ratioH, -1f, -1f,
                        1f, ratioH, +1f, -1f};
                break;
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }

    public float[] adjustScalingWithoutSettingCube(int showWidth, int showHeight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {

        float ratio1 = (float) showWidth / width;
        float ratio2 = (float) showHeight / height;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(width * ratioMax);
        int imageHeightNew = Math.round(height * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = 2f * imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点


        float[] cube = pos;
        switch (orientation) {
            case LEFT:
                ratioHeight /= 2f;
                cube = new float[]{-1f, coord + ratioHeight / scale,
                        -1f, coord - ratioHeight / scale,
                        -1f + 2f * ratioWidth / scale, coord + ratioHeight / scale,
                        -1f + 2f * ratioWidth / scale, coord - ratioHeight / scale};
                break;
            case RIGHT:
                ratioHeight /= 2f;
                cube = new float[]{1 - 2f * ratioWidth / scale, coord + ratioHeight / scale,
                        1f - 2f * ratioWidth / scale, coord - ratioHeight / scale,
                        1f, coord + ratioHeight / scale,
                        1f, coord - ratioHeight / scale};
                break;
            case BOTTOM:
                cube = new float[]{coord - ratioWidth / scale, 1f,
                        coord - ratioWidth / scale, 1 - ratioHeight / scale,
                        coord + ratioWidth / scale, 1,
                        coord + ratioWidth / scale, 1 - ratioHeight / scale};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                cube = new float[]{coord - ratioWidth / scale, ratioHeight / scale - 1f,
                        coord - ratioWidth / scale, -1f,
                        coord + ratioWidth / scale, ratioHeight / scale - 1,
                        coord + ratioWidth / scale, -1f};
                break;
            case LEFT_TOP:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{-1.0f, ratioHeight / scale - 1.0f,
                        -1.0f, -1.0f,
                        ratioWidth / scale - 1.0f, ratioHeight / scale - 1.0f,
                        ratioWidth / scale - 1.0f, -1.0f};
                break;
            case LEFT_BOTTOM:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{-1.0f, 1.0f, -1.0f, 1.0f - ratioHeight / scale,
                        ratioWidth / scale - 1.0f, 1.0f, ratioWidth / scale - 1.0f, 1.0f - ratioHeight / scale};
                break;
            case RIGHT_TOP:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{1.0f - ratioWidth / scale, ratioHeight / scale - 1.0f, 1.0f - ratioWidth / scale, -1.0f,
                        1.0f, ratioHeight / scale - 1.0f, 1.0f, -1.0f};
                break;
            case RIGHT_BOTTOM:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{1.0f - ratioWidth / scale, 1.0f, 1.0f - ratioWidth / scale, 1.0f - ratioHeight / scale,
                        1.0f, 1.0f, 1.0f, 1.0f - ratioHeight / scale};
                break;
        }
        return cube;
    }

    public void adjustScalingByCube(float[] cube) {
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
    }

}
