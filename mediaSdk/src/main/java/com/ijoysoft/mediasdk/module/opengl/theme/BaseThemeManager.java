package com.ijoysoft.mediasdk.module.opengl.theme;

import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

import java.util.List;

public class BaseThemeManager implements ThemeIRender {
    public static float DEFAUT_ZVIEW = -3f;
    public static float ZVIEW_OFFSET = -0.1f;
    protected IAction actionRender;
    protected PureColorFilter pureColorFilter;
    protected float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    //背景颜色
    protected BGInfo mBGInfo;

    @Override
    public void initBackgroundTexture() {
        ratioChange();
    }

    public BaseThemeManager() {
        pureColorFilter = new PureColorFilter();
        setIsPureColor();
    }

    public void setIsPureColor() {
    }

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        drawPrepare(mediaItem, index);
    }

    public void drawPrepare(MediaItem mediaItem, int index) {

    }

    public void setIsPureColor(BGInfo bgInfo) {
        this.mBGInfo = bgInfo;
        if (actionRender instanceof BaseBlurThemeExample) {
            actionRender.setIsPureColor(bgInfo);
        }
    }


    @Override
    public void setActionRender(IAction actionRender) {
        if (this.actionRender != null) {
            this.actionRender.onDestroy();
        }
        this.actionRender = actionRender;
    }

    @Override
    public IAction getNextAction(MediaItem mediaItem, int index) {
        return null;
    }

    @Override
    public IAction getAction() {
        return null;
    }

    @Override
    public void draFrameExtra() {

    }

    @Override
    public void previewAfilter(MediaItem mediaItem) {
        if (actionRender == null) {
            return;
        }
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
        actionRender.drawFramePreview();
    }


    @Override
    public void previewMediaIem() {
        if (actionRender == null) {
            return;
        }
        actionRender.drawFramePreview();
    }

    @Override
    public void setPreAfilter(List<MediaItem> list, int index) {

    }

    @Override
    public void ratioChange() {

    }

    @Override
    public void seekTo(int currentDuration) {
        if (actionRender != null) {
            actionRender.seek(currentDuration);
        }
    }


    @Override
    public void onDestroyFragment() {
        onDestroy();
    }


    @Override
    public void onSurfaceCreated() {
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
    }

    @Override
    public void onDrawFrame() {

    }

    @Override
    public void onDestroy() {

    }


    @Override
    public boolean checkSupportTransition() {
        return false;
    }

    public float getZView() {
        return DEFAUT_ZVIEW;
    }

    @Override
    public void drawVideoFrame(int videoTeture) {
        if (actionRender != null) {
            actionRender.drawVideoFrame(videoTeture);
        }
    }
}
