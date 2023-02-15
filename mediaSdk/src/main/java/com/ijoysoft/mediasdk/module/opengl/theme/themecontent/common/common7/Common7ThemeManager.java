package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common7;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.Firework;
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmbeddedFilterEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3Times;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3TimesEasingOutExpo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake7Times;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3TimesAt30Degrees;
import com.ijoysoft.mediasdk.module.opengl.filter.RedBlueFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 20212/1/06  9:56
 */
public class Common7ThemeManager extends BaseTimeThemeManager {


    /**
     * @param widgetMipmaps
     * @return
     */
    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {

        IParticleRender render = new ParticleBuilder(ParticleType.CENTER_EXPAND, new ArrayList<>(widgetMipmaps.subList(0, 4))).setParticleCount(32)
                .setPointSize(3, 20)
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .setSelfRotate(true).setScale(false).build();
        render.onSurfaceCreated();

        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 440, 0, 400, 840), render));

        IParticleRender render2 = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(new ArrayList<>(widgetMipmaps.subList(0, 4)))
                .setParticleCount(16)
                .setParticleSizeRatio(0.03f)
                .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .build();
        render2.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(Arrays.asList(
                new ThemeWidgetInfo(0, 440, 0, 1240, 1680),
                new ThemeWidgetInfo(0, 680, 0, 2960, 3640)
        ), render2));

//        Firework firework = new Firework();
//        firework.setDuration(680);
//        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 680, 0, 2960, 3640), firework));


        IParticleRender render3 = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(new ArrayList<>(widgetMipmaps.subList(4, 7)))
                .setParticleCount(16)
                .setParticleSizeRatio(0.015f)
                .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .build();
        render3.onSurfaceCreated();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 440, 0, 4040, 4480), render3));

        IParticleRender render4 = new ParticleBuilder(ParticleType.CENTER_EXPAND, new ArrayList<>(widgetMipmaps.subList(6, 7))).setParticleCount(32)
                .setPointSize(3, 20).setSelfRotate(true).setScale(false)
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .setExpandIn(true).build();
        render4.onSurfaceCreated();

        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 440, 0, 4880, 5320), render4));

        IParticleRender render5 = new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                .setListBitmaps(new ArrayList<>(widgetMipmaps.subList(4, 5)))
                .setParticleCount(32)
                .setParticleSizeDelta(10f)
                .setSizeFreeze(true)
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .build();
        render5.onSurfaceCreated();

        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 440, 0, 5720, 6160), render5));

        IParticleRender render6 = new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                .setListBitmaps(new ArrayList<>(widgetMipmaps.subList(4, 6)))
                .setParticleCount(16)
                .setParticleSizeDelta(5f)
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .build();
        render6.onSurfaceCreated();

        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 440, 0, 6560, 7000), render6));

        IParticleRender render7 = new ParticleBuilder(ParticleType.CENTER_EXPAND, new ArrayList<>(widgetMipmaps.subList(6, 7))).setParticleCount(32)
                .setPointSize(3, 20).setSelfRotate(true).setScale(false)
                .setLoadAllParticleInFirstFrame(true)
                .setSizeFloatingStart(0.4f)
                .setExpandIn(true).build();
        render7.onSurfaceCreated();

        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 440, 0, 8760, 9200), render7));


        return true;
    }

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }

    @Override
    protected int computeThemeCycleTime() {
        return 9200;
    }

    /**
     * index %=11;
     * switch (index) {
     * case 2:
     * return 1960;
     * case 7:
     * case 8:
     * case 9:
     * return 440;
     * case 10:
     * return 880;
     * default:
     * return 840;
     * }
     *
     * @param index
     * @param duration
     * @return
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        currentDuration = 840;
        switch (index) {
            case 0:
                //840, 840
                IMoveAction scaleRotateAction = new EaseOutQuart(false, true, false);
                list.add(new AnimationBuilder(getDuration(currentDuration, 5, 21), width, height, true)
                        .setScale(0.7f, 1f)
                        .setScaleAction(scaleRotateAction)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 16, 21), width, height, true)
                        .build());
                break;


            case 1:
                //840,1680
            case 4:
                //840, 5320
                list.add(new AnimationBuilder(getDuration(duration, 5, 21), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 30)
                        .setRotateAction(new Shake7Times(false, false, true))
                        .setRotaCenter(3f, 0f)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0f)
                        .build());

                list.add(new AnimationBuilder(getDuration(duration, 16, 21), width, height, true).build());
                break;

            case 2:
                //1960, 3640
                RedBlueFilter redBlueFilter = new RedBlueFilter();
                redBlueFilter.onCreate();
                redBlueFilter.onSizeChanged(width, height);

                EmbeddedFilterEvaluate evaluate1 = new EmbeddedFilterEvaluate(new AnimationBuilder(getDuration(duration, 5, 49), width, height, true));
                evaluate1.setEmbeddedFilter(redBlueFilter);
                list.add(evaluate1);
                list.add(new AnimationBuilder(getDuration(duration, 7, 49), width, height, true).setScale(1f, 1f).build());

                EmbeddedFilterEvaluate evaluate2 = new EmbeddedFilterEvaluate(new AnimationBuilder(getDuration(duration, 5, 49), width, height, true));
                evaluate2.setEmbeddedFilter(redBlueFilter);
                list.add(evaluate2);
                list.add(new AnimationBuilder(getDuration(duration, 7, 49), width, height, true).setScale(1f, 1f).build());

                EmbeddedFilterEvaluate evaluate3 = new EmbeddedFilterEvaluate(new AnimationBuilder(getDuration(duration, 5, 49), width, height, true));
                evaluate3.setEmbeddedFilter(redBlueFilter);
                list.add(evaluate3);
                list.add(new AnimationBuilder(getDuration(duration, 20, 49), width, height, true).setScale(1f, 1f).build());

                break;

            case 3:
                //840,4480
                list.add(new AnimationBuilder(getDuration(duration, 5, 21), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 30)
                        .setRotateAction(new Shake7Times(false, false, true))
                        .setRotaCenter(-3f, 0f)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0f)
                        .build());
                list.add(new AnimationBuilder(getDuration(duration, 16, 21), width, height, true).build());
                break;


            case 5:
                //840, 6160
                list.add(new AnimationBuilder(getDuration(duration, 5, 21), width, height, true).setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0f)
                        .setScale(1.3f, 0.7f).setMoveAction(new Shake3TimesAt30Degrees(false, false, true)).build());
                list.add(new AnimationBuilder(getDuration(duration, 16, 21), width, height, true)
                        .build());

                break;

            case 6:
                //840, 7000
                list.add(new AnimationBuilder((int) duration, width, height, true)
                        .setScale(1.3f, 1.3f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 80)
                        .setRotateAction(new Shake3TimesEasingOutExpo(false, false, true, false))
                        .build());
                break;


            case 7:
                //440. 7440
                list.add(new AnimationBuilder((int) duration, width, height, true).setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setScale(1.3f, 0.7f)
                        .setMoveAction(new Shake3TimesEasingOutExpo(false, false, true, false)).build());
                break;
            case 8:
                //440, 7880
                list.add(new AnimationBuilder((int) duration, width, height, true).setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setScale(1.3f, 0.7f)
                        .setRotaCenter(0f, -0.5f)
                        .setMoveAction(new Shake3TimesEasingOutExpo(false, false, true, false)).build());
                break;

            case 9:
                //440, 8320
                list.add(new AnimationBuilder((int) duration, width, height, true)
                        .setStartY(0)
                        .setEndY(0.5f)
                        .setScale(1.3f, 0.7f)
                        .setMoveAction(new Shake3TimesEasingOutExpo(true, false, false, false))
                        .build());
                break;
            case 10:
                //880, 9200
                list.add(new AnimationBuilder(getDuration(duration, 11, 22), width, height, true)
                        .setStartX(0)
                        .setEndX(0.5f)
                        .setScale(1.3f, 0.7f)
                        .setMoveAction(new Shake3TimesEasingOutExpo(true, false, false, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0f)
                        .build());
                list.add(new AnimationBuilder(getDuration(duration, 11, 22), width, height, true)
                        .setScale(0.7f, 0.7f)
                        .build());


                break;


            default:
                list.add(new AnimationBuilder(getDuration(duration, 10, 20), width, height, true)
                        .setMoveAction(new EaseOutQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0f)
                        .setScale(1.4f, 1f)
                        .build());
                list.add(new AnimationBuilder(getDuration(duration, 10, 20), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0f, 5f)
                        .setScale(1f, 1.4f)
                        .build());
                break;
        }
        return list;
    }
//
//  TODO 有bug
//
//    @Override
//    protected void createExtraWithBitmap(int index, MediaItem mediaItem) {
//        super.createExtraWithBitmap(index, mediaItem);
//        List<BaseEvaluate> evaluateList = map.get(index);
//        switch (index) {
//            case 0: {
//                List<Bitmap> list = mediaItem.getMimapBitmaps();
//                TimeThemeEvaluate evaluate = (TimeThemeEvaluate) evaluateList.get(2);
//
//                IRender render = new ParticleBuilder(ParticleType.CENTER_EXPAND, list).setParticleCount(25)
//                        .setPointSize(3, 20).setSelfRotate(true).setScale(true).build();
//                render.onSurfaceCreated();
//                evaluate.setExtraRender(Collections.singletonList(
//                        render
//                ));
//            }break;
//            case 1: {
//                List<Bitmap> list = mediaItem.getMimapBitmaps();
//                TimeThemeEvaluate evaluate = (TimeThemeEvaluate) evaluateList.get(1);
//
//                IRender render = new ParticleBuilder(ParticleType.CENTER_EXPAND, list).setParticleCount(25)
//                        .setPointSize(3, 20).setSelfRotate(true).setScale(true).build();
//                render.onSurfaceCreated();
//                evaluate.setExtraRender(Collections.singletonList(
//                        render
//                ));
//            }
//
//
//
//                break;
//
//            default: break;
//        }
//    }

    @Override
    protected int createActions() {
        return 11;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }


    @Override
    public boolean isTextureInside() {
        return true;
    }


    @Override
    protected boolean blurBackground() {
        return true;
    }
}
