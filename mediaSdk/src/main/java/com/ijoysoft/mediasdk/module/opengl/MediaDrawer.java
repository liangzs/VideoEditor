package com.ijoysoft.mediasdk.module.opengl;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.DoodleGroupFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.NoFilter;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.List;

/**
 * 涂鸦层控制类,便宜控制类
 * mediaDrawer做最后的输出设置（文字、贴图、水印），以及视频比例的偏移量
 */
public class MediaDrawer implements IRender {
    private static final String TAG = "MediaDrawer";
    /**
     * 涂鸦、水印绘制
     */
    private final DoodleGroupFilter mDoodleFilter;
    /**
     * 控件的长宽
     */
    private int viewWidth;
    private int viewHeight;
    private int offsetX, offsetY;

    private int durationPostion;
    private boolean isPreviewRotate;
    /**
     * 显示的滤镜
     */
    private AFilter mShow;

    public MediaDrawer() {
        mDoodleFilter = new DoodleGroupFilter();
        mShow = new NoFilter();
    }

    @Override
    public void onSurfaceCreated() {
        mDoodleFilter.create();
        mShow.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height) {
        viewWidth = width;
        viewHeight = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        mDoodleFilter.setSize(viewWidth, viewHeight);
    }

    /**
     * 把滤镜从主渲染剔除，移到单元的渲染里面。
     * 图片层的滤镜转移到imageplayer里面，视频层的滤镜由videoDrawer接管
     */
    @Override
    public void onDrawFrame() {
        // EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[2]);
        // mMatrixShow.draw();
        // EasyGlUtils.unBindFrameBuffer();
        // mDoodleFilter.setTextureId(fTexture[2]);
        mDoodleFilter.drawGroup(durationPostion);
        // LogUtils.i(TAG, "onDrawFrame-->mDoodleFilter.getTexture:" + mDoodleFilter.getTextureId());
        mShow.setTextureId(mDoodleFilter.getOutputTexture());
        // LogUtils.i(TAG, "onDrawFrame-->mShow.getTexture:" + mShow.getTextureId());
        GLES20.glViewport(isPreviewRotate ? 0 : offsetX, isPreviewRotate ? 0 : offsetY, viewWidth, viewHeight);
        mShow.draw();
    }

    public void checkGlError(String s) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(s + ": glError " + error);
        }
    }

    public void preViewRotate(boolean flag) {
        isPreviewRotate = flag;
    }

    /**
     * 设置外置帧源,从befilter开始绘制
     * 如果是视频，那么就创建一个TexureSurface，并绑定texture的纹理id 和surface
     * surfaceTexture = new SurfaceTexture(texture[0]);
     * mPreFilter.create();
     * mPreFilter.setTextureId(texture[0]);
     * 然后通过 surfaceTexture.updateTexImage();通知视频有帧过去，mPreFilter承载视频的帧，然后放在fbo中进行绘画，然后
     * surfaceTexture.updateTexImage();
     * EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
     * GLES20.glViewport(0, 0, viewWidth, viewHeight);
     * mPreFilter.draw();
     * EasyGlUtils.unBindFrameBuffer();那么就会把prefilter给绘画在fbo中，
     * 然后需要从fbo中读取缓存的帧的数据，那么久直接用这个fTexture[0]就可以使用这个fbo中的帧了
     * <p>
     * 如果对图片进行操作的话，那么需要把图片的纹理id传到prefilter中，但是不走surfaceTexture中的updateTexImage过程，
     * 是由imageplaer主动去推动render的渲染
     */
    public void setInputTexture(int textureId) {
        mDoodleFilter.setTextureId(textureId);
    }

    public void setCurrentDuration(int duration) {
        this.durationPostion = duration;
    }

    /**
     * 视频存储的时候，把最后的那个纹理传入给编码器
     *
     * @return
     */
    public int getShowTexureId() {
        if (mShow != null) {
            return mShow.getTextureId();
        }
        return 0;
    }

    /**
     * 添加涂鸦成,根据不一样时间长度，显示不同时长的涂鸦
     */
    public void addDoodle(DoodleItem doodleItem) {
        mDoodleFilter.addDoodle(doodleItem);
    }

    public void setDoodle(List<DoodleItem> list) {
        LogUtils.i(TAG, "setDoodle");
        mDoodleFilter.setDoodleItemList(list);
    }

    /**
     * 根据索引位置去更新贴图
     */
    public void updateDoodle(int index, DoodleItem doodleItem) {
        mDoodleFilter.updateDoodle(index, doodleItem);
    }

    public void  onDestroy() {
        if (mShow != null) {
            mShow.onDestroy();
        }
        if (mDoodleFilter != null) {
            mDoodleFilter.onDestroy();
        }
    }

}
