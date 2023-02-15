package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common20;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutBounce;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuad;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hayring
 * @date 2022/1/13  17:42
 */
public class Common20ThemeManager extends Common8ThemeManager {

    /**
     * 粒子系统
     */
    IRender render;

    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {

        render = new ParticleBuilder(ParticleType.CENTER_EXPAND, new ArrayList<>(widgetMipmaps.subList(6, 10))).setParticleCount(16)
                .setPointSize(3, 40)
                .setLoadAllParticleInFirstFrame(true)
                .setSelfRotate(true).setScale(false).build();
        render.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(Arrays.asList(
                        new ThemeWidgetInfo(0, 1240, 0, 1000, 2240),
                        new ThemeWidgetInfo(0, 1240, 0, 3240, 4480),
                        new ThemeWidgetInfo(0, 1240, 0, 5480, 6720),
                        new ThemeWidgetInfo(0, 1240, 0, 7720, 8960),
                        new ThemeWidgetInfo(0, 1240, 0, 9960, 11200)
                )
                , render));


        return false;
    }

    protected AFilter bgFilter;


    @Override
    public void onDrawFrame(int currenPostion) {
        bgFilter.draw();
        super.onDrawFrame(currenPostion);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (actionRender != null) {
            actionRender.onDestroy();
        }
        for (Bitmap bitmap : roundBitMapCache.values()) {
            bitmap.recycle();
        }
        roundBitMapCache.clear();
        if (render != null) {
            render.onDestroy();
        }
        if (bgFilter != null) {
            bgFilter.onDestroy();
        }

    }


    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        onFilterSizeChanged(width, height);
        if (render != null) {
            render.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        }
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        float[] cube = actionRender.getPos();
        cube[1] = 0.8f;
        cube[5] = 0.8f;
        actionRender.setVertex(cube);
        initFilter();
    }


    protected void initFilter() {
        if (bgFilter == null) {
            bgFilter = new PureColorFilter();
            bgFilter.create();
        }
        ((PureColorFilter) bgFilter).setIsPureColor(new float[]{1, 1, 1, 1});
    }

    protected void onFilterSizeChanged(int width, int height) {
        bgFilter.onSizeChanged(width, height);
    }

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        switch (index) {
            case 0:
                return new ThemeWidgetInfo(1000, 1240, 0, 0, 2240);
            case 1:
                return new ThemeWidgetInfo(1000, 1240, 0, 2240, 4480);
            case 2:
            case 3:
                return new ThemeWidgetInfo(1000, 1240, 0, 4480, 6720);
            case 4:
                return new ThemeWidgetInfo(1000, 1240, 0, 6720, 8960);
            case 5:
                return new ThemeWidgetInfo(1000, 1240, 0, 8960, 11200);
            default:
                return null;
        }
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
//        float scale = 0.7f;
//        int period = (int) (widgetTimeExample.getWidgetInfo().getTime() / 3);
        switch (index) {
            case 0: {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.5f);
                widgetTimeExample.setEnterAction(new AnimationBuilder(1000)
                        .setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f));
                widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutBounce(true, false, false, false));
            }
            break;
            case 1: {
                widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                    if (width1 < height1) {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f);
                    } else {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 2f);
                    }

                    widgetTimeExample.setEnterAction(new AnimationBuilder(1000)
                            .setCoordinate(0f, Float.MIN_VALUE, 0f, 0f));
                    widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                    widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutQuad(true, false, false, false));
                });
                widgetTimeExample.onSizeChanged(width, height);
            }
            break;
            case 2: {
                widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                    if (width1 < height1) {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 2f);
                    } else {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, 1f);
                    }

                    widgetTimeExample.setEnterAction(new AnimationBuilder(1000)
                            .setCoordinate(0f, Float.MAX_VALUE, 0f, 0f));
                    widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                    widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutQuad(true, false, false, false));
                });
                widgetTimeExample.onSizeChanged(width, height);

            }
            break;
            case 3: {
                widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                    if (width1 < height1) {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 2f);
                    } else {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1f);
                    }

                    widgetTimeExample.setEnterAction(new AnimationBuilder(1000)
                            .setCoordinate(0f, Float.MAX_VALUE, 0f, 0f));
                    widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                    widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutQuad(true, false, false, false));
                });
                widgetTimeExample.onSizeChanged(width, height);
            }
            break;
            case 4: {
                widgetTimeExample.setOnSizeChangedListener((width1, height1) -> {
                    if (width1 < height1) {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f);
                    } else {
                        widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 2f);
                    }

                    widgetTimeExample.setEnterAction(new AnimationBuilder(1000)
                            .setCoordinate(0f, Float.MIN_VALUE, 0f, 0f));
                    widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                    widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutQuad(true, false, false, false));
                });
                widgetTimeExample.onSizeChanged(width, height);
            }
            break;
            case 5: {
                widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, 1.5f);
                widgetTimeExample.setEnterAction(new AnimationBuilder(1000)
                        .setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f));
                widgetTimeExample.fixEnterOutAnimationStartEndAtScreenEdge();
                widgetTimeExample.getEnterAnimation().setMoveAction(new EaseOutBounce(true, false, false, false));
            }
            break;
        }
    }


    Map<Integer, Bitmap> roundBitMapCache = new HashMap<>();

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
//        int key = index % createActions();
//        if (!map.containsKey(key)) {
//            map.put(key, createAnimateEvaluate(key, mediaItem.getDuration()));
//            createExtraWithBitmap(index, mediaItem);
//        }
//        lastEvaluates = currentEvaluates;
//        currentEvaluates = map.get(index % createActions());
//        if (roundBitMapCache.get(index) == null && mediaItem.getBitmap() != null) {
//            roundBitMapCache.put(index, BitmapUtil.getRoundedBitmap(mediaItem.getBitmap(), 0.2f));
//        }
//        actionRender.setBackgroundTexture(roundBitMapCache.get(index));
//        if (isTextureInside()) {
//            actionRender.adjustImageScalingInside(width, height);
//        } else {
//            actionRender.adjustImageScalingCrop(width, height);
//        }
//
//        actionRender.setEvaluates(currentEvaluates);
//        if (lastEvaluates != null) {
//            resetEvaluate(lastEvaluates);
//        }
    }

    public void superDrawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
    }


    @Override
    protected boolean blurBackground() {
        return false;
    }
}
