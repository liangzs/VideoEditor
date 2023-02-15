package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveAction;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutBack;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutQuart;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Shake3TimesAt30Degrees;

import java.util.ArrayList;

/**
 * @author hayring
 * @date 2021/12/30  19:27
 */
public class Common6ThemeManager extends BaseTimeThemeManager {


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }

    /**
     * @param index
     * @param duration
     * @return if (index%9 == 0) {
     * return 3400;
     * }
     * return 800;
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        currentDuration = 800;
        switch (index) {
            case 0:
                currentDuration = 3400;
                IMoveAction scaleRotateAction = new EaseOutQuart(false, true, true);
                list.add(new AnimationBuilder(getDuration(currentDuration, 56, 85), width, height, true)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setStartConor(-45)
                        .setEndConor(0)
                        .setStartX(0.5f)
                        .setStartY(-0.5f)
                        .setScale(1.4f, 1f)
                        .setMoveAction(new EaseOutBack(true, false, false))
                        .setScaleAction(scaleRotateAction)
                        .setRotateAction(scaleRotateAction)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 29, 85), width, height, true)
                        .setScaleAction(new EaseInQuart(false, true, false))
                        .setRatioBlurRange(0f, 1.5f, false)
                        .setScale(1f, 1.4f)
                        .build());
                break;

            case 1:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setRotateAction(new EaseOutQuart(false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, -90, 0)
                        .setMoveBlurRange(1f, 0f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false))
                        .setCoordinate(0f, 0f, 0f, 0.8f)
                        .setMoveBlurRange(0f, 5f)
                        .build());
                break;

            case 2:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false))
                        .setCoordinate(0f, -0.8f, 0f, 0f)
                        .setMoveBlurRange(5f, 0f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setRotateAction(new EaseInQuart(false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, -90)
                        .setMoveBlurRange(0f, 5f)
                        .build());
                break;

            case 3:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setRotateAction(new EaseOutQuart(false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 90, 0)
                        .setMoveBlurRange(1f, 0f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setScale(1f, 0.4f)
                        .setRatioBlurRange(0f, 1.5f, true)
                        .build());
                break;

            case 4:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseOutQuart(false, true, false))
                        .setScale(1.4f, 1f)
                        .setRatioBlurRange(1.5f, 0f, true)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setRotateAction(new EaseInQuart(false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, -90)
                        .setMoveBlurRange(0f, 5f)
                        .build());
                break;
            case 5:
            case 7:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setMoveBlurRange(5f, 0f)
                        .setStartConor(-10)
                        .setEndConor(0)
                        .setStartX(-0.5f)
                        .setEndX(0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setMoveBlurRange(0f, 5f)
                        .setStartConor(0)
                        .setEndConor(10)
                        .setStartX(0.3f)
                        .setEndX(-0.5f)
                        .build());
                break;

            case 6:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseOutQuart(true, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setMoveBlurRange(5f, 0f)
                        .setStartConor(10)
                        .setEndConor(0)
                        .setStartX(0.5f)
                        .setEndX(-0.3f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseInQuart(true, false, false))
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setMoveBlurRange(0f, 5f)
                        .setStartConor(0)
                        .setEndConor(-10)
                        .setStartX(-0.3f)
                        .setEndX(0.5f)
                        .build());
                break;
            case 8:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 5)
                        .setRatioBlurRange(1.5f, 0f, true)
                        .setScale(1.3f, 0.7f).setRotateAction(new Shake3TimesAt30Degrees(false, false, true)).build());

                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setScaleAction(new EaseInQuart(false, true, false))
                        .setScale(0.7f, 1.3f)
                        .setRatioBlurRange(0f, 1.5f, false)
                        .build());
                break;

            default:
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseOutQuart(false, true, false))
                        .setScale(1.4f, 1f)
                        .setRatioBlurRange(1.5f, 0f, true)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder(getDuration(currentDuration, 10, 20), width, height, true)
                        .setMoveAction(new EaseInQuart(false, true, false))
                        .setRatioBlurRange(0f, 1.5f, false)
                        .setScale(1f, 1.4f)
                        .build());
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 9;
    }


    @Override
    public boolean isTextureInside() {
        return true;
    }

    @Override
    protected boolean blurBackground() {
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }


}
