package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common5;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmbeddedFilterEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuad;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3TimesAt30Degrees;
import com.ijoysoft.mediasdk.module.opengl.transition.QuarterCylinderOutFilter;

import java.util.ArrayList;

/**
 * @author hayring
 * @date 2022/1/13  11:37
 */
public class Common5ThemeManagerTemplate extends BaseTimeThemeManager {


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected int computeThemeCycleTime() {
        return 8900;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }


    @Override
    public boolean isTextureInside() {
        return true;
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return if (index%15 == 0) {
     * return 1920;
     * }
     * return 480;
     * }
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        currentDuration = 480;
        switch (index) {
            case 0:
                currentDuration = 1920;
                list.add(new AnimationBuilder(getDuration(currentDuration, 25, 48), width, height, true).setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 1f, 0)
                        .setScale(1.3f, 0.9f).setMoveAction(new Shake3TimesAt30Degrees(false, false, true)).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }

                list.add(new AnimationBuilder(getDuration(currentDuration, 23, 48), width, height, true)
                        .setScaleAction(new EaseInQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 1f)
                        .setScale(0.9f, 1.3f)
                        .build());
                break;
            case 2:
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveAction(new EaseOutQuart(false, true, true))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .setScale(1f, 0.8f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-15)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 5f)
                        .setScale(0.8f, 0.4f)
                        .build());
                break;

            case 4:
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveAction(new EaseOutQuart(false, true, true))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .setScale(1.3f, 0.9f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-90)
                        .setEndConor(-15)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, true))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 5f)
                        .setScale(0.9f, 1.3f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-15)
                        .setEndConor(0)
                        .build());
                break;

            case 6:
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-10)
                        .setEndConor(0)
                        .setScale(0.85f, 0.85f)
                        .setStartX(-0.5f)
                        .setEndX(0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(0)
                        .setScale(0.85f, 0.85f)
                        .setEndConor(10)
                        .setStartX(0.3f)
                        .setEndX(-0.5f)
                        .build());
                break;

            case 7:
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(10)
                        .setEndConor(0)
                        .setScale(0.85f, 0.85f)
                        .setStartX(0.5f)
                        .setEndX(-0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(0)
                        .setScale(0.85f, 0.85f)
                        .setEndConor(-10)
                        .setStartX(-0.3f)
                        .setEndX(0.5f)
                        .build());
                break;


            case 3:
            case 5:
            case 9:
                list.add(new AnimationBuilder(getDuration(duration, 8, 12), width, height, true)
                        .setScaleAction(new EaseOutQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .setScale(1.3f, 0.9f)
                        .build());

//                list.add(new AnimationBuilder(160, width, height, true)
//                        .setRotationType(AnimateInfo.ROTATION.Y_ANXIS)
//                        .setStartConor(0)
//                        .setEndConor(90)
//                        .setEndX(-1.1f)
//                        .build());
                EmbeddedFilterEvaluate evaluate = new EmbeddedFilterEvaluate(new AnimationBuilder(getDuration(duration, 4, 12), width, height, true).setScale(0.7f, 1.0f));
                QuarterCylinderOutFilter quarterCylinderOutFilter = new QuarterCylinderOutFilter();
                evaluate.setEmbeddedFilter(quarterCylinderOutFilter);
                quarterCylinderOutFilter.onCreate();
                evaluate.getEmbeddedFilter().onSizeChanged(width, height);
                list.add(evaluate);
//                list.add(new AnimationBuilder(160, width, height, true).setScale(0.7f, 1.0f).build());
                break;

            case 10:
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveAction(new EaseOutQuart(false, true, true))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .setScale(1.3f, 0.9f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(90)
                        .setEndConor(-15)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, true))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5f)
                        .setScale(0.85f, 0.85f)
                        .setCoordinate(0f, 0f, 2f, 0f)
                        .build());
                break;

            case 11:
                list.add(
                        new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0)
                                .setMoveAction(new EaseOutQuart(true, false, false, false))
                                .setCoordinate(0.5f, 0f, 0f, 0f)
                                .build()
                );
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(
                        new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5f)
                                .setSkewAction(new EaseInQuad(false, false, false, true))
                                .setSkew(0, 0, 0, -15f)
                                .setCoordinate(0f, 0f, 0.5f, 0f)
                                .build()
                );

            case 12:
                list.add(
                        new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0)
                                .setSkewAction(new EaseInQuad(false, false, false, true))
                                .setSkew(0, -15, 0, 0f)
                                .setCoordinate(-0.5f, 0f, 0f, 0f)
                                .build()
                );
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(
                        new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                                .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5f)
                                .setMoveAction(new EaseOutQuart(true, false, false, false))
                                .setCoordinate(0f, 0f, -0.5f, 0f)
                                .build()
                );
            case 13:
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .setScale(0.6f, 0.9f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 5f)
                        .setScale(0.9f, 0.6f)
                        .build());
                break;
            case 14:
                IMoveAction scaleRotateAction = new EaseOutQuart(false, true, true);
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5f, 0)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-45)
                        .setEndConor(0)
                        .setScale(0.85f, 0.85f)
                        .setScaleAction(scaleRotateAction)
                        .setRotateAction(scaleRotateAction)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 5f)
                        .setScaleAction(new EaseInQuart(false, true, false))
                        .setScale(0.9f, 1.3f)
                        .build());
                break;
            default:
//                list.add(new AnimationBuilder(period, width, height, true).
//                        setCoordinate(-0.6f, 0, 0, 0).setScale(1.2f, 1.2f).build());
                list.add(new AnimationBuilder(getDuration(currentDuration, 8, 12), width, height, true)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 5f, 0)
                        .setMoveAction(new EaseOutQuart(false, true, false))
                        .setScale(1.3f, 0.9f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 4, 12), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 5f)
                        .setScale(0.9f, 1.3f)
                        .build());
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 15;
    }


    @Override
    protected boolean blurBackground() {
        return true;
    }


}