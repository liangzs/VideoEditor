package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.entity.DoodleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理涂鸦层的显示
 */
public class DoodleGroupFilter extends AFilter {
    private List<WaterMarkFilter> mFilters;
    private int width = 0, height = 0;
    private boolean isShow;
    private List<DoodleItem> doodleItemList;
    // 创建离屏buffer
    private int fTextureSize = 2;
    private int[] fFrame = new int[1];
    // private int[] fRender = new int[1];
    private int[] fTexture = new int[fTextureSize];
    private int textureIndex = 0;

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

    private void updateFilter() {
        for (int i = 0; i < mFilters.size(); i++) {
            mFilters.get(i).onDestroy();
        }
        clearAll();
        for (int i = 0; i < doodleItemList.size(); i++) {
            // 添加水印
            WaterMarkFilter waterMarkFilter = new WaterMarkFilter();
            waterMarkFilter.setPosition(0, 0, width, height);
            waterMarkFilter.setDurationInterval(doodleItemList.get(i).getDurationInterval());
            waterMarkFilter.onCreate();
            waterMarkFilter.setWaterMarkTexure(doodleItemList.get(i).getPath());
            waterMarkFilter.setSize(width, height);
            mFilters.add(waterMarkFilter);
        }

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
        deleteFrameBuffer();
        this.width = width;
        this.height = height;
        updateFilter();
        createFrameBuffer();
    }

    // 创建FrameBuffer
    private boolean createFrameBuffer() {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // 创建离屏纹理
        genTextures();
        // GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fFrame[0]);
        // GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, fRender[0]);
        // GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
        // GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
        // fTexture[0], 0);
        // GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER,
        // fRender[0]);
        unBindFrame();
        return false;
    }

    /**
     * 双Texture,一个输入一个输出,循环往复
     */
    public void drawGroup(int durationPosition) {
        textureIndex = 0;
        isShow = false;
        for (AFilter filter : mFilters) {
            if (filter.getDurationInterval().isInRange(durationPosition)) {
                isShow = true;
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fFrame[0]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                        fTexture[textureIndex % 2], 0);
                // GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                // GLES20.GL_RENDERBUFFER, fRender[0]);
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
        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
    }

    // 取消绑定Texture
    private void unBindFrame() {
        // GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void deleteFrameBuffer() {
        // GLES20.glDeleteRenderbuffers(1, fRender, 0);
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    /**
     * 一次性初始化
     */
    public void setDoodleItemList(List<DoodleItem> list) {
        if (list == null) {
            return;
        }
        this.doodleItemList = list;
        updateFilter();
    }

    /**
     * 添加涂鸦
     * 单独更新，记得放入glsurfaceview线程
     * 不知道为啥，添加完涂鸦之后，明明是在quetEvent中执行
     * watermarkFilter的create和changesize，但是一直没有效果
     * 可能的原有是activity重新走了resume办法，然后glsureface进行了休眠，glsurfaceview本身也
     * 走了changeSize方法
     *
     * @param doodleItem
     */
    public void addDoodle(DoodleItem doodleItem) {
        doodleItemList.add(doodleItem);
        // 添加水印
        WaterMarkFilter waterMarkFilter = new WaterMarkFilter();
        waterMarkFilter.setPosition(0, 0, width, height);
        waterMarkFilter.setDurationInterval(doodleItem.getDurationInterval());
    }

    /**
     * @param index
     */
    public void updateDoodle(int index, DoodleItem doodleItem) {
        doodleItemList.set(index, doodleItem);
        mFilters.get(index).setWaterMarkTexure(doodleItem.getPath());
        mFilters.get(index).setDurationInterval(doodleItemList.get(index).getDurationInterval());
        mFilters.get(index).create();
        mFilters.get(index).setSize(width, height);
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
