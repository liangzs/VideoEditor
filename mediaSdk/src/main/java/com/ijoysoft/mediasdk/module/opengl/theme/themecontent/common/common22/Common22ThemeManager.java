package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common22;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeFilterContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuad;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutBounce;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuad;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hayring
 * @date 20212/1/06  9:56
 */
public class Common22ThemeManager extends Common8ThemeManager {


    @Override
    protected int computeThemeCycleTime() {
        return 8960;
    }
    @Override
    protected int createActions() {
        return 4;
    }


    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {

        ThemeWidgetInfo themeWidgetInfo1 = new ThemeWidgetInfo(0, 2240, 0, 0, 2240);
        ThemeWidgetInfo themeWidgetInfo2 = new ThemeWidgetInfo(0, 2240, 0, 4480, 6720);

        IParticleRender render = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(new ArrayList<>(widgetMipmaps.subList(0, 2)))
                .setParticleCount(16)
                .setParticleSizeRatio(0.02f)
                .setRandomAlphaStart(0f)
                .setSizeFloatingStart(0.3f)
                .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                .setLoadAllParticleInFirstFrame(true)
                .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                .build();
        render.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(Arrays.asList(themeWidgetInfo1, themeWidgetInfo2), render));

        IParticleRender render2 = new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                .setListBitmaps(Collections.singletonList(widgetMipmaps.get(2)))
                .setParticleCount(8)
                .setShortLife(0.5f)
                .setParticleSizeDelta(2f)
                .build();
        render2.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(Arrays.asList(themeWidgetInfo1, themeWidgetInfo2), render2));


        ThemeWidgetInfo themeWidgetInfo3 = new ThemeWidgetInfo(0, 2240, 0, 2240, 4480);
        ThemeWidgetInfo themeWidgetInfo4 = new ThemeWidgetInfo(0, 2240, 0, 6720, 8960);

        IParticleRender render3 = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(Collections.singletonList(widgetMipmaps.get(3)))
                .setParticleCount(4)
                .setParticleSizeRatio(0.02f)
                .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                .setStartPositionGenerator(ParticleBuilder.createBottomParticleGenerator())
                .setLoadAllParticleInFirstFrame(true)
                .build();
        render3.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(Arrays.asList(themeWidgetInfo3, themeWidgetInfo4), render3));

        IParticleRender render4 = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(new ArrayList<>(widgetMipmaps.subList(4, 6)))
                .setParticleCount(8)
                .setParticleSizeRatio(0.03f)
                .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                .setStartPositionGenerator(ParticleBuilder.createBottomParticleGenerator())
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.01f)
                .build();
        render4.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(Arrays.asList(themeWidgetInfo3, themeWidgetInfo4), render4));

        return false;
    }

    @Override
    protected void customCreateGifWidget(List<List<GifDecoder.GifFrame>> gifMipmaps, int timeLineStart) {
//        GifOriginFilter gifOriginFilter1 = new GifOriginFilter();
//        gifOriginFilter1.setFrames(gifMipmaps.get(timeLineStart++));
//        gifOriginFilter1.setOnSizeChangedListener(((width1, height1) ->
//                gifOriginFilter1.adjustScaling(width1, height1, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1f)));
//        gifOriginFilter1.onCreate();
//        gifOriginFilter1.onSizeChanged(width, height);
//        renders.add(new TimeThemeFilterContainer(new ThemeWidgetInfo(0, 2240, 0, 0, 2240), gifOriginFilter1));
//
//
//        GifOriginFilter gifOriginFilter2 = new GifOriginFilter();
//        gifOriginFilter2.setFrames(gifMipmaps.get(timeLineStart++));
//        gifOriginFilter2.setOnSizeChangedListener(((width1, height1) ->
//                gifOriginFilter2.adjustScaling(width1, height1, AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1f)));
//        gifOriginFilter2.onCreate();
//        gifOriginFilter2.onSizeChanged(width, height);
//        renders.add(new TimeThemeFilterContainer(new ThemeWidgetInfo(0, 2240, 0, 4480, 6720), gifOriginFilter2));


    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        if (index == 6) {
            return new ThemeWidgetInfo(0, 2240, 0, 2240, 4480);
        }
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
        widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
            if (width1 < height1) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 0.75f);
            } else if (width1 > height1) {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.5f);
            } else {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1f);
            }
            widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
        });
        widgetTimeExample.onSizeChanged(width, height);

    }






}
