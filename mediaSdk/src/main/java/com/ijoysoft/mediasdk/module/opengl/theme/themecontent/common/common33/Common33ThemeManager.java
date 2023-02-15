package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common33;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager;

import java.util.Arrays;
import java.util.List;

public class Common33ThemeManager extends Common15ThemeManager {
    //片段切换循环使用
    private GifOriginFilter gifOriginFilter;

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
    }


    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        //1+2片段
        IParticleRender render = new ParticleBuilder(ParticleType.SCALE_LOOP, widgetMipmaps).setParticleCount(40)
                .setPointSize(3, 20)
                .setMinPointSize(2)
                .setLoadAllParticleInFirstFrame(true)
                .setILocationAction(new Common33Paticle())
                .build();
        renders.add(new TimeThemeRenderContainer(Arrays.asList(new ThemeWidgetInfo(1600, 4800)), render));
        return true;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 4800;
    }

    /**
     * 一共个12个action，那么久三个一组进行分类
     *
     * @param mediaDrawer
     * @param mediaItem
     * @param index
     */
    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        if (gifOriginFilter == null) {
            gifOriginFilter = new GifOriginFilter();
        }
        index = index % 6;
        switch (index) {
            case 0:
                if (gifOriginFilter.getTextureId() != -1) {
                    gifOriginFilter.onDestroy();
                    gifOriginFilter.resetData();
                }
                gifOriginFilter.onCreate();
                gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
                break;
            case 2:
                if (gifOriginFilter.getTextureId() != -1) {
                    gifOriginFilter.onDestroy();
                    gifOriginFilter.resetData();
                }
                gifOriginFilter.onCreate();
                gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
                break;
        }
        super.drawPrepare(mediaDrawer, mediaItem, index);

    }

    @Override
    protected void onGifSizeChanged(int index, int width, int height) {
        if (gifOriginFilter == null) {
            return;
        }
        switch (index) {
            case 0:
                gifOriginFilter.adjustScaling(width, height, 0f, 0, 1.2f, 1.2f);
                break;
            case 2:
                float centerY = width < height ? 0.8f : width == height ? 0.6f : 0.5f;
                float scale = width < height ? 1.3f : width == height ? 1.5f : 1.5f;
                gifOriginFilter.adjustScaling(width, height, 0f, centerY, scale, scale);
                break;
        }
    }

    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
        if (gifOriginFilter != null) {
            gifOriginFilter.draw();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gifOriginFilter != null) {
            gifOriginFilter.onDestroy();
            gifOriginFilter = null;
        }
    }
}
