package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common13;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustom;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustomNoOffset;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutPow;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Easings;

import java.util.ArrayList;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Common13ThemeManager extends BaseTimeThemeManager {

    public Common13ThemeManager() {
    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }


    @Override
    protected boolean blurBackground() {
        return true;
    }

    @Override
    public boolean isTextureInside() {
        return true;
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     * //左右移动
     * case 0:
     * case 4:
     * return 600;
     * case 1:
     * return 400;
     * case 2:
     * case 3:
     * case 9:
     * case 17:
     * return 1200;
     * case 10:
     * case 11:
     * return 2000;
     * default:
     * return 600;
     * }
     *
     * @param index
     * @param duration
     * @return
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
//        index = 17;
        currentDuration = 600;
        switch (index % createActions()) {
            case 0:
                //左旋转进
                list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                        .setMoveAction(new EaseOutPow(true, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 90, 0)
                        .setCoordinate(-0.3f, 0, 0f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                break;
            case 1:
                currentDuration = 400;
                //右旋转进
                list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                        .setMoveAction(new EaseOutCubic(true, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 90, 0)
                        .setCoordinate(0.3f, 0, 0f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                break;
            case 2:
                currentDuration = 1200;
                //左旋转进，下模糊移动出
                list.add(new AnimationBuilder((int) (currentDuration * 0.3f), width, height, true)
                        .setMoveAction(new EaseOutPow(true, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 90, 0)
                        .setCoordinate(-0.3f, 0, 0f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.7f), width, height, true)
                        .setMoveAction(new EaseInMultiple(5, true, false, true))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 20)
                        .setCoordinate(0, 0, 0, 2f).build());
                break;
            case 3://动感模糊进,左旋转出
                currentDuration = 1200;
                list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                        .setMoveAction(new EaseOutMultiple(5, true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 20, 0)
                        .setCoordinate(0, -2f, 0, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                        .setMoveAction(new EaseInCubic(false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, -90).build());
                break;
            case 4:
                //跟随旋转进，缩小出场
                list.add(new AnimationBuilder((int) (currentDuration * 0.6f), width, height, true)
                        .setScale(1.6f, 1.6f)
                        .setMoveAction(new EaseInCubic(false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 90, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.4), width, height, true)
                        .setRatioBlurRange(0, 6, true)
                        .setScaleAction(new EaseInCubic())
                        .setScale(1.6f, 1.0f)
                        .build());
                break;
            case 5:
                //径向模糊缩小，右转
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(6, 0, true)
                        .setScale(1.7f, 1.0f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 90)
                        .build());
                break;
            case 6:
                //缩小，悬停转动
                list.add(new AnimationBuilder((int) (currentDuration * 0.8), width, height, true)
                        .setScale(1.2f, 1.0f)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpring(x)))
                        .build());
                list.add(new AnimationBuilder((int) (duration - currentDuration * 0.8), width, height, true).build());
                break;
            case 7:
            case 8:
                //缩小，微无规则散动，再缩小
                list.addAll(EvaluatorHelper.zoomOut2MoveFree2ZoomOut(currentDuration, duration, width, height));
                break;
            case 9:
                currentDuration = 1200;
                //缩小弹簧进
                list.addAll(EvaluatorHelper.zoomOutSpring2ZoomOut(currentDuration, duration, width, height));
                break;
            case 10:
                currentDuration = 2000;
                //缩小弹动，旋转放大，缩小微转回正
                list.add(new AnimationBuilder((int) (currentDuration * 0.3), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setIsSprintY(true)
                        .setMoveAction(new EaseCustomNoOffset(x -> Easings.easeSpringSmoothDelay(x)))
                        .setScale(1.6f, 1.1f)
                        .build());
                list.add(new AnimationBuilder((int) (duration - currentDuration * 0.6), width, height, true)
                        .setScaleAction(new EaseInCubic(true, true, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 2f)
                        .setCoordinate(0, 0, 0, -0.01f)
                        .setScale(1.1f, 1.3f)
                        .build());
                list.add(new AnimationBuilder((int) (currentDuration * 0.3F), width, height, true)
                        .setScaleAction(new EaseInCubic(true, true, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2, 1f)
                        .setScale(1.3f, 0.8f)
                        .build());
            case 11:
                currentDuration = 2000;
                //靠近左上角缩放，移动旋转，然后大旋转出去
                list.add(new AnimationBuilder((int) (currentDuration * 0.3f), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 2, 2)
                        .setScale(1.4f, 1.0f, -0.8f, -0.9f)
                        .setCoordinate(0.2f, 0, 0, 0)
                        .setScaleAction(new EaseOutCubic())
                        .build());
                list.add(new AnimationBuilder((int) (duration - currentDuration * 0.6f), width, height, true)
                        .setMoveAction(new EaseCustom(x -> Easings.easeInOutSine(x)).setRotation(true).setTranslate(true))
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 2, -2)
                        .setCoordinate(0, 0, 0.1f, 0)
                        .build());
                list.add(new AnimationBuilder((int) (currentDuration * 0.3f), width, height, true)
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, -2, -90)
                        .setRotateAction(new EaseInCubic())
                        .build());

            case 12:
                //中心点偏移左的缩小，放大微移
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(3, 0, true)
                        .setScale(1.7f, 1.0f, -0.7f, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseInCubic())
                        .setScale(1.0f, 1.7f, -0.7f, 0)
                        .setCoordinate(0, 0, 0.1f, 0)
                        .build());
            case 13:
                //中心点偏移右的缩小，放大微移
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(3, 0, true)
                        .setScale(1.7f, 1.0f, 0.7f, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseInCubic())
                        .setScale(1.0f, 1.7f, 0.7f, 0)
                        .setCoordinate(0, 0, -0.1f, 0)
                        .build());

            case 14:
                //缩小，快到慢转，转动出场
                list.add(new AnimationBuilder((int) (currentDuration * 0.3), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(3, 0, true)
                        .setScale(1.7f, 1.0f)
                        .build());
                list.add(new AnimationBuilder((int) (duration - currentDuration * 0.6), width, height, true)
                        .setRotateAction(new EaseCustom(x -> (float) Easings.easeOutSine(x)))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 8)
                        .build());
                list.add(new AnimationBuilder((int) (currentDuration * 0.3), width, height, true)
                        .setRotateAction(new EaseInCubic())
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 8, -90)
                        .build());
                break;
            case 15:
                //右进来，缩小
                list.add(new AnimationBuilder((int) (currentDuration * 0.6), width, height, true)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 0)
                        .setScale(1.2f, 1.2f)
                        .setCoordinate(1.5f, 0, 0, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.4), width, height, true)
                        .setMoveAction(new EaseInCubic(false, true, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5)
                        .setScale(1.2f, 0.6f)
                        .build());
                break;
            case 16:
                //左进来，缩小
                list.add(new AnimationBuilder((int) (currentDuration * 0.6), width, height, true)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 0)
                        .setScale(1.2f, 1.2f)
                        .setCoordinate(-1.5f, 0, 0, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.4), width, height, true)
                        .setMoveAction(new EaseInCubic(false, true, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 5)
                        .setScale(1.2f, 0.6f)
                        .build());
            case 17:
                currentDuration = 1200;
                //缩小弹簧进
                list.addAll(EvaluatorHelper.zoomOutSpring2ZoomOut(currentDuration, duration, width, height));
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 18;
    }


}
