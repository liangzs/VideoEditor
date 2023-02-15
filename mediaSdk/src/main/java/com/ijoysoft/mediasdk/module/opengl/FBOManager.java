package com.ijoysoft.mediasdk.module.opengl;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 管控Fbo的分配
 * FBO&texure
 * imageplayer    fTextureSize = 4;
 * 		1.imageBackgroundFilter   fTextureSize = 1;
 * 		2.SlideGpuFilterGroup     fTextureSize = 1;
 *
 * MediaDrawer    fTextureSize = 3;
 * 		1.VideoBackgroundFilter  fTextureSize = 2;
 * 		2.ImageBackgroundFilter  fTextureSize = 1;
 * 		3.SlideGpuFilterGroup    fTextureSize = 1;
 * 		4.DoodleGroupFilter      fTextureSize = 2;
 *
 * 	因为fbo较多，在创建和销毁过程中，其绑定的纹理
 * 	有可能会被以下三个抢占，导致纹理混淆，内容丢失（涂鸦消失等...）
 * 	1、滤镜：由纹理的会加载多个1-2，有转场时两个需要显示则2-4，每个滤镜组进行切换是会把
 * 当前滤镜回收，然后创建新的
 * 2、图片元素：1-3 图片切换的时候需要进行切换
 * 3、涂鸦： >1   有多少个涂鸦就有多少个纹理，所以每次涂鸦都只能获取零碎的纹理id
 *
 */
public class FBOManager {
    private static class SingleTonHoler {
        private static FBOManager INSTANCE = new FBOManager();
    }

    public static FBOManager getInstance() {
        return SingleTonHoler.INSTANCE;
    }

    private static final String TAG = "FBOManager";

    private static final int EXTAND_NUM = 5;
    // 两张纹理
    private int fTextureSize = 4;
    private int[] fTexture = new int[fTextureSize];
    private int width, height;
    private List<Integer> listUsing = new ArrayList<>();// 已经分配出去的列表（包含了上次事务列表，所以当上次事务被清理的时候，同时也要处理移除当前列表）
    private LinkedList<Integer> listAvalible = new LinkedList<>();// 可用fbo列表
    private List<Integer> listLast = new ArrayList<>();// 上一次事务列表

    private FBOManager() {
    }

    /**
     * 在renderThread进行调用
     * @param width
     * @param height
     */
    public void createFBo(int width, int height) {
        deleteFrameBuffer();
        // 创建离屏渲染
        this.width = width;
        this.height = height;
        // 创建离屏纹理
        generateTextures(width, height, fTextureSize, fTexture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        for (int i = 0; i < fTextureSize; i++) {
            listAvalible.add(fTexture[i]);
        }
        LogUtils.i(TAG, "createFBo:listAvalible.size()" + listAvalible.size() + ","+Arrays.toString(listAvalible.toArray()));
    }

    public void onDestroy() {
        deleteFrameBuffer();
    }

    // 生成Textures
    private void generateTextures(int width, int height, int fTextureSize, int[] fTexture) {
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

    private void deleteFrameBuffer() {
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        listAvalible.clear();
        listUsing.clear();
        listLast.clear();
    }

    /**
     * 清理上次所有的fbo缓存。两个预览页面切换
     * @param fTextureSize
     * @return
     */
    public int[] fboTransactionClearTop(int fTextureSize) {
        // 情况上次事务占用保留
        if (!listLast.isEmpty()) {
            listAvalible.addAll(listLast);
            listLast.clear();
        }

        if (!listUsing.isEmpty()) {
            listAvalible.addAll(listUsing);
            listUsing.clear();
        }
        if (fTextureSize > listAvalible.size()) {
            LogUtils.i(TAG, "fboTransactionClearTop:-extandTexture");
            extandTexture();
            return fboTransactionClearTop(fTextureSize);
        }
        int[] result = new int[fTextureSize];
        for (int i = 0; i < fTextureSize; i++) {
            result[i] = listAvalible.pop();
        }
        for (int i = 0; i < result.length; i++) {
            listUsing.add(result[i]);
        }
        LogUtils.i(TAG, "fboTransactionClearTop:-listAvalible" + Arrays.toString(listAvalible.toArray()));
        LogUtils.i(TAG, "fboTransactionClearTop:-listUsing" + Arrays.toString(listUsing.toArray()));
        return result;
    }

    /**
     *普通事务的规则如下：
     * 如果当前事务已经走完，然后启动下一个了新的事务，那么当前的事务可以清理重新利用
     * @param fTextureSize
     */
    public int[] fboTransaction(int fTextureSize) {
        if (fTextureSize > listAvalible.size()) {
            extandTexture();
            return fboTransaction(fTextureSize);
        }
        int[] result = new int[fTextureSize];
        for (int i = 0; i < fTextureSize; i++) {
            result[i] = listAvalible.pop();
        }
        // 记录在using
        swithStatus(result, true);
        return result;
    }

    /**
     *普通事务的规则如下：
     * 如果当前事务已经走完，然后启动下一个了新的事务，那么当前的事务可以清理重新利用
     * @param fTextureSize
     */
    public int[] fboTransactionNoRecyle(int fTextureSize) {
        if (fTextureSize > listAvalible.size()) {
            extandTexture();
            LogUtils.i(TAG, "fboTransactionNoRecyle:-extandTexture");
            return fboTransactionNoRecyle(fTextureSize);
        }
        int[] result = new int[fTextureSize];
        for (int i = 0; i < fTextureSize; i++) {
            result[i] = listAvalible.pop();
        }
        LogUtils.i(TAG, "fboTransactionNoRecyle:-listAvalible" + Arrays.toString(listAvalible.toArray()));
        // 记录在using
        swithStatus(result, false);
        return result;
    }

    /**
     * 清除状态，切换数据
     */
    private void swithStatus(int[] result, boolean isRecycle) {
        if (!isRecycle) {
            for (int i = 0; i < result.length; i++) {
                listUsing.add(result[i]);
            }
            LogUtils.i(TAG, "swithStatus:-listUsing" + Arrays.toString(listUsing.toArray()));
            return;
        }

        // 情况上次事务占用保留
        if (!listLast.isEmpty()) {
            listAvalible.addAll(listLast);
        }
        if (!listUsing.isEmpty()) {
            listLast.clear();
            listLast.addAll(listUsing);
        }
        listUsing.clear();
        for (int i = 0; i < result.length; i++) {
            listUsing.add(result[i]);
        }
    }

    /**
     * 数据扩展
     */
    private void extandTexture() {
        fTextureSize += EXTAND_NUM;
        int[] newArray = new int[EXTAND_NUM];
        generateTextures(width, height, EXTAND_NUM, newArray);
        int[] temp = new int[fTextureSize];
        System.arraycopy(fTexture, 0, temp, 0, fTexture.length);
        System.arraycopy(newArray, 0, temp, temp.length - EXTAND_NUM, EXTAND_NUM);
        // 扩展后赋值
        fTexture = temp;
        for (int i = 0; i < EXTAND_NUM; i++) {
            listAvalible.add(newArray[i]);
        }
        LogUtils.i(TAG, "extandTexture:-listAvalible" + Arrays.toString(listAvalible.toArray()));
    }

    /**
     * 固定分配imageplayer的FBO
     */
    public int[] getFBOforImageplayer(int width, int height) {
        boolean isInit = false;
        for (int i : fTexture) {
            if (i == 0 || i == -1) {
                isInit = true;
            }
        }
        if (isInit) {
            deleteFrameBuffer();
            generateTextures(width, height, fTextureSize, fTexture);
        }
        return fTexture;
    }


}
