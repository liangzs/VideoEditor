package com.ijoysoft.mediasdk.module.opengl.theme;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;

import java.util.List;

/**
 * 控件挂载到时间戳
 * 创建离屏渲染，控件显示完毕之后作为值传出
 */
public class ThemeWidgetTimeManager extends AFilter {
    private static final String TAG = "ThemeWidgetTimeManager";
    private List<ThemeWidgetInfo> widgets;
    private int showWidth = 0, showHeight = 0;
    private int fTextureSize = 2;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[fTextureSize];
    private int textureIndex = 0;

    public ThemeWidgetTimeManager() {
        super();
    }

    @Override
    protected void initBuffer() {

    }


    @Override
    public int getOutputTexture() {
        return widgets.size() == 0 ? getTextureId() : fTexture[(textureIndex - 1) % 2];
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
        createFrameBuffer();
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
     */
    public void drawGroup(int durationPosition) {
        textureIndex = 0;

    }

    // 生成Textures
    private void genTextures() {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        if (fTexture[0] == -1 || fTexture[1] == -1) {
            deleteFrameBuffer();
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
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteFrameBuffer();
    }
}
