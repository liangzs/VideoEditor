package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common12;

import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustom;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Easings;

import java.util.ArrayList;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Common12ThemeManager extends BaseTimeThemeManager {

    public Common12ThemeManager() {
    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
    }


    @Override
    protected String getPureColor() {
        return "#FF000000";
    }

    @Override
    protected boolean blurBackground() {
        return false;
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
     * @return switch (index % getMipmapsCount()) {
     * //左右移动
     * case 0:
     * return 600;
     * case 1:
     * return 400;
     * case 3:
     * case 14:
     * case 15:
     * case 16:
     * case 2:
     * return 1200;
     * //缩放
     * case 7:
     * case 8:
     * case 9:
     * case 10:
     * case 11:
     * case 12:
     * return 400;
     * case 13:
     * //缩放平移
     * return 800;
     * case 17:
     * //左右移动，旋转
     * return 800;
     * case 26:
     * return 800;
     * case 27:
     * return 1200;
     * case 28:
     * return 1500;
     * case 29:
     * return 2000;
     * default:
     * return 600;
     * }
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        currentDuration = 600;
//        index = 6;
        switch (index % createActions()) {
            //左进左出
            case 0:
                currentDuration = 600;
                list.addAll(EvaluatorHelper.moveBLurLeft2Left(currentDuration, duration, width, height));
                break;
            //右进右出
            case 1:
                currentDuration = 400;
                list.addAll(EvaluatorHelper.moveBlurRight2Right(currentDuration, duration, width, height));
                break;
            case 2:
                currentDuration = 1200;
                //动感模糊缩小，向上平移，动感模糊放大
                list.addAll(EvaluatorHelper.zoomOut2MoveTop2ZoomIn(currentDuration, duration, width, height));
                break;
            case 3://动感模糊进，左出
                currentDuration = 1200;
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 3, 0)
                        .setMoveAction(new EaseOutMultiple(5, false, true, false))
                        .setScale(1.7f, 1f).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //@TODO 后续换弧度出场
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0, 0, -2f, 0)
                        .build());
                break;
            case 4:
                currentDuration = 1200;
                //右进，径向模糊缩小
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setCoordinate(2, 0, 0f, 0)
                        .setScale(1.4f, 1.4f)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setRatioBlurRange(0, 1, true)
                        .setScale(1.4f, 0.8f)
                        .build());
                break;
            case 5:
                //径向模糊缩小，旋转
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(1, 0, true)
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
                //旋转,径向模糊放大
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 90, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScaleAction(new EaseInCubic())
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 1)
                        .setScale(1.0f, 1.7f)
                        .build());

                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 18:
            case 19:
            case 20:
                currentDuration = 400;
                //径向模糊缩小*径向模糊缩小
                list.addAll(EvaluatorHelper.zoomOut2ZoomOut(duration, width, height));
                break;
            case 13:
            case 26:
                currentDuration = 800;
                //缩小上移放大
                list.addAll(EvaluatorHelper.zoomOut2MoveTop2ZoomIn(currentDuration, duration, width, height));
                break;

            case 14:
                currentDuration = 1200;
                //左进左出
                list.addAll(EvaluatorHelper.moveBLurLeft2Left(currentDuration, duration, width, height));
                break;
            //右进右出
            case 15:
                currentDuration = 1200;
                list.addAll(EvaluatorHelper.moveBlurRight2Right(currentDuration, duration, width, height));
                break;
            case 16:
                currentDuration = 1200;
                //左进左出
                list.addAll(EvaluatorHelper.moveBLurLeft2Left(currentDuration, duration, width, height));
                break;
            case 17:
                currentDuration = 800;
                //右进大角度旋转右出
                list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 0)
                        .setMoveAction(new EaseOutCubic(true, false, true))
                        .setRotaCenter(0.3f, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2, 1)
                        .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                        .setCoordinate(0.6f, 0, 0f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5f), width, height, true)
                        .setRotaCenter(0.3f, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -2)
                        .setRotateAction(new EaseCustom(x -> Easings.smoothStepAhead(x)))
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0.0f, 0, 2f, 0).build());
                break;

            case 21:
                //缩小-放大
                list.addAll(EvaluatorHelper.zoomOut2ZoomIn(duration, width, height));
                break;
            case 22:
            case 23:
            case 24:
            case 25:
                list.add(new AnimationBuilder((int) (duration), width, height, true)
                        .setScale(0.8f, 1.0f)
                        .build());
                break;
            case 27:
            case 28:
            case 29:
                list.addAll(EvaluatorHelper.zoomOut2ZoomIn(duration, width, height));
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 30;
    }


}
