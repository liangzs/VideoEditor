package com.ijoysoft.mediasdk.module.opengl.filter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.WaterMarkType;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理涂鸦层的显示
 */
public class DoodleGroupFilter extends AFilter {
    private static final String TAG = "DoodleGroupFilter";
    private List<WaterMarkFilter> mFilters;
    private int showWidth = 0, showHeight = 0;
    private boolean isShow;
    private List<DoodleItem> doodleItemList;
    // 创建离屏buffer
    private int fTextureSize = 2;
    private int[] fFrame = new int[1];
    //    private int[] fRender = new int[1];
    private int[] fTexture = new int[fTextureSize];
    private int textureIndex = 0;
    private int offsetX, offsetY;

    public DoodleGroupFilter() {
        super();
        mFilters = new ArrayList<>();
        doodleItemList = new ArrayList<>();
    }

    @Override
    protected void initBuffer() {

    }

    public void clearAll() {
        mFilters.clear();
    }


    /**
     * 这么做的目的是gif的gifdecoder复用
     */
    private void updateFilter() {
        List<WaterMarkFilter> tempList = new ArrayList<>();
        //check add
        for (int i = 0; i < doodleItemList.size(); i++) {
            DoodleItem doodleItem = doodleItemList.get(i);
            WaterMarkFilter waterMarkFilter = new WaterMarkFilter();
            waterMarkFilter.setPosition(0, 0, showWidth, showHeight, 0);
            waterMarkFilter.setDurationInterval(doodleItem.getDurationInterval());
            waterMarkFilter.onCreate();
            //如果是贴图或者gif则根据坐标进行定位
            if (doodleItem.getWaterMarkType() == WaterMarkType.PICTURE) {
                synScalePosition(waterMarkFilter, doodleItem);
                if (doodleItem.getGif()) {
                    waterMarkFilter.initGif(doodleItem.getPath(), doodleItem.getResourceId());
                } else {
                    Bitmap bitmap;
                    if (doodleItem.getResourceId() != 0 && doodleItem.getResourceId() != -1) {
                        bitmap = BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), doodleItem.getResourceId());
                    } else {
                        bitmap = BitmapUtil.getSmallBitmapByWH(doodleItem.getPath(), 0,
                                ConstantMediaSize.localBitmapWidth, ConstantMediaSize.localBitmapHeight);
                    }
                    if (bitmap == null) {
                        continue;
                    }
                    Bitmap temp = bitmap;
                    if (Math.abs(doodleItem.getAngle()) > 3) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(doodleItem.getAngle());
                        temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap.recycle();
                    }
                    if (temp != null) {
                        waterMarkFilter.setWaterMarkTexure(temp);
                        temp.recycle();
                    }
                }
            }

            if (doodleItem.getWaterMarkType() != WaterMarkType.PICTURE) {
                waterMarkFilter.setWaterMarkTexure(doodleItem.getPath());
            }
            waterMarkFilter.setSize(showWidth, showHeight);
            tempList.add(waterMarkFilter);
        }
        for (int i = 0; i < mFilters.size(); i++) {
            mFilters.get(i).onDestroy();
        }
        mFilters.clear();
        mFilters = tempList;
    }

    /**
     * 旋转角度α，矩形高度h，宽度w
     * width = w*cosα + h*sinα
     * height = h*cosα + w*sinα
     *
     * @param waterMarkFilter
     * @param doodleItem
     */
    private void synScalePosition(WaterMarkFilter waterMarkFilter, DoodleItem doodleItem) {
        LogUtils.i(TAG, doodleItem.toString());
//        float scale = showWidth == 0 ? ConstantMediaSize.showViewWidth * 1f / doodleItem.getBaseW() : showWidth * 1f / doodleItem.getBaseW();
//        float tranx = doodleItem.getTranX() * showWidth / doodleItem.getBaseW();
//        float trany = doodleItem.getTranY() * showHeight / doodleItem.getBaseH();
        float minScale = 1f; //Math.min(showHeight / doodleItem.getBaseH(), showWidth / doodleItem.getBaseW());
        //图片宽高
        float w = doodleItem.getWidth() * minScale;
        float h = doodleItem.getHeight() * minScale;

//        if (doodleItem.getAngle() != 0) {
//            //重新计算宽高
//            w = (int) (doodleItem.getWidth() * Math.abs(Math.cos(doodleItem.getAngle())) + doodleItem.getHeight() * Math.abs(Math.sin(doodleItem.getAngle())));
//            h = (int) (doodleItem.getHeight() * Math.abs(Math.cos(doodleItem.getAngle())) + doodleItem.getWidth() * Math.abs(Math.sin(doodleItem.getAngle())));
//        }
        float centerX = (doodleItem.getTranX() + 0.5f * w) / doodleItem.getBaseW();
        float centerY = (doodleItem.getTranY() + 0.5f * h) / doodleItem.getBaseH();
        float tranx = showWidth * centerX - w * 0.5f;
        float trany = showHeight * centerY - h * 0.5f;
        LogUtils.v(TAG, "getWidth:" + doodleItem.getWidth() + ",getHeight:" + doodleItem.getHeight() + ",tranx:" + tranx + ",trany:" + trany);
//        if (doodleItem.getAngle() != 0) {
//            tranx = tranx * doodleItem.getWidth() / w;
//            trany = trany * doodleItem.getHeight() / h;
//        }
        LogUtils.v(TAG, "w:" + w + ",h:" + h + ",tranx:" + tranx + ",trany:" + trany + ",doodleItem.getWidth() / w;" + doodleItem.getWidth() / w);
        waterMarkFilter.setPosition(tranx, trany, w, h, doodleItem.getAngle());
    }


    @Override
    public int getOutputTexture() {
        if (!isShow) {
            return getTextureId();
        }
        return mFilters.size() == 0 ? getTextureId() : fTexture[(textureIndex - 1) % 2];
    }

    @Override
    protected void onCreate() {
    }

    @Override
    public void onSizeChanged(int width, int height) {

    }


    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height) {
        this.showWidth = width;
        this.showHeight = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        createFrameBuffer();
        updateFilter();
    }


    // 创建FrameBuffer
    private boolean createFrameBuffer() {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        genTextures();
        unBindFrame();
        return false;
    }

    /**
     * 双Texture,一个输入一个输出,循环往复
     * GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
     * //                        GLES20.GL_RENDERBUFFER, fRender[0]);
     * <p>
     * //                // 深度缓冲
     * //                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, fRender[0]);
     * //                // 为深度缓冲区分配存储空间
     * //                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,
     * //                        GLES20.GL_DEPTH_COMPONENT16,
     * //                        width, height);
     * //                // 将深度缓冲区和纹理（颜色缓冲区）附加到帧缓冲区对象。
     * //                GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
     * //                        GLES20.GL_RENDERBUFFER, fRender[0]);
     */
    public void drawGroup(int durationPosition) {
        textureIndex = 0;
        isShow = false;
        for (WaterMarkFilter filter : mFilters) {
            if (filter.getDurationInterval().isInRange(durationPosition)) {
                isShow = true;
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fFrame[0]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                        fTexture[textureIndex % 2], 0);
                GLES20.glViewport(0, 0, showWidth, showHeight);
                if (textureIndex == 0) {
                    filter.setTextureId(getTextureId());
                } else {
                    filter.setTextureId(fTexture[(textureIndex - 1) % 2]);
                }
                filter.draw();
                unBindFrame();
                textureIndex++;
            }
        }
    }

    // 生成Textures
    private void genTextures() {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        if (fTexture[0] == -1 || fTexture[1] == -1) {
//            deleteFrameBuffer();
            GLES20.glGenTextures(fTextureSize, fTexture, 0);
        }
        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, showWidth, showHeight, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
    }

    // 取消绑定Texture
    private void unBindFrame() {
//        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void deleteFrameBuffer() {
//        GLES20.glDeleteRenderbuffers(1, fRender, 0);
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    /**
     * 一次性初始化
     */
    public void setDoodleItemList(List<DoodleItem> list, boolean render) {
        if (list == null) {
            return;
        }
        this.doodleItemList = list;
        if (render) {
            updateFilter();
        }
    }


    /**
     * @param index
     */
    public void updateDoodle(int index, DoodleItem doodleItem) {
        doodleItemList.set(index, doodleItem);
        mFilters.get(index).setWaterMarkTexure(doodleItem.getPath());
        mFilters.get(index).setDurationInterval(doodleItemList.get(index).getDurationInterval());
        mFilters.get(index).create();
        mFilters.get(index).setSize(showWidth, showHeight);
    }

    /**
     * 移除贴图
     *
     * @param index
     */
    public void removeDoodle(int index) {
        doodleItemList.remove(index);
        mFilters.remove(index).onDestroy();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mFilters.size(); i++) {
            mFilters.get(i).onDestroy();
        }
        deleteFrameBuffer();
    }
}
