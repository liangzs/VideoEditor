package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.RedBlueFilter;
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
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuad;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuad;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3Times;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3TimesAt30Degrees;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake7Times;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hayring
 * @date 20212/1/06  9:56
 */
public class Common8ThemeManager extends BaseTimeThemeManager {


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }

    @Override
    protected int computeThemeCycleTime() {
        return 11200;
    }


    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        currentDuration = 2240;
        switch (index) {
            case 0:
                list.add(new AnimationBuilder(getDuration(currentDuration, 37, 56), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-10)
                        .setEndConor(0)
                        .setScale(0.9f, 0.9f)
                        .setStartX(0.5f)
                        .setEndX(-0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 19, 56), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(0)
                        .setScale(0.9f, 0.9f)
                        .setEndConor(10)
                        .setStartX(-0.3f)
                        .setEndX(0.5f)
                        .build());
                break;

            case 1:
                list.add(new AnimationBuilder(getDuration(currentDuration, 37, 56), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(10)
                        .setEndConor(0)
                        .setScale(0.9f, 0.9f)
                        .setStartX(-0.5f)
                        .setEndX(0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 19, 56), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(0)
                        .setScale(0.9f, 0.9f)
                        .setEndConor(-10)
                        .setStartX(0.3f)
                        .setEndX(-0.5f)
                        .build());
                break;
            case 2:
                list.add(new AnimationBuilder(getDuration(currentDuration, 37, 56), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false, false))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, -5, 0)
                        .setScale(0.9f, 0.9f)
                        .setStartX(0.5f)
                        .setEndX(-0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 19, 56), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false, false))
                        .setSkewAction(new EaseInQuad(false, false, false, true))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setSkew(0, 0, 0, 10)
                        .setScale(0.9f, 0.9f)
                        .setStartX(-0.3f)
                        .setEndX(0.5f)
                        .build());
                break;

            case 3:
                list.add(new AnimationBuilder(getDuration(currentDuration, 37, 56), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false, false))
                        .setSkewAction(new EaseOutQuad(false, false, false, true))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setSkew(0, 10, 0, 0)
                        .setScale(0.9f, 0.9f)
                        .setStartX(-0.5f)
                        .setEndX(0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 19, 56), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false, false))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, -5)
                        .setScale(0.9f, 0.9f)
                        .setStartX(0.3f)
                        .setEndX(-0.5f)
                        .build());
                break;

            default:
                list.add(new AnimationBuilder(getDuration(currentDuration, 37, 56), width, height, true)
                        .setMoveAction(new EaseOutQuad(true, true, false, false))
                        .setScale(1.2f, 0.9f)
                        .setStartX(1f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 19, 56), width, height, true)
                        .setMoveAction(new EaseInQuad(true, true, false, false))
                        .setScale(0.9f, 1.2f)
                        .setEndX(-1f)
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
        return 5;
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
