package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11;

import android.graphics.Bitmap;

import com.google.android.exoplayer2.C;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustom;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustomNoOffset;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Easings;

import java.util.ArrayList;
import java.util.List;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Common11ThemeManager extends BaseTimeThemeManager {

    public Common11ThemeManager() {
    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }

    /*      case 0:
                return 1000;
            case 1:  1800
            case 2:   2600
            case 3:   3400
            case 4:   4200
            case 5:   5000
            case 6:z
                return 6800;*/
    @Override
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        //1+2片段、中心扩散+上下移动+白点闪耀
        IParticleRender render = new ParticleBuilder(ParticleType.EXPAND,
                new ArrayList<>(widgetMipmaps.subList(0, 1))).setParticleCount(24)
                .setPointSize(2, 20)
                .setLifeTime(1, 0.5f)
                .setExpanSpeedMulti(20)
                .setColorMap(new Common11Paticle3())
//                .setLoadAllParticleInFirstFrame(true)
                .setSelfRotate(true)
                .setScale(true)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 1800), render));
        //3片段、黄星闪耀
        render = new ParticleBuilder(ParticleType.SCALE_LOOP, new ArrayList<>(widgetMipmaps.subList(3, 4))).setParticleCount(30)
                .setPointSize(3, 10)
                .setPointScalePeriod(1.5f)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(1800, 2600), render));

        //4+5+6片段 白星闪耀,心
        //      case 3:   3400
        //            case 4:   4200
        //            case 5:   5000
        render = new ParticleBuilder(ParticleType.SCALE_LOOP, new ArrayList<>(widgetMipmaps.subList(2, 3))).setParticleCount(30)
                .setPointSize(3, 10)
                .setPointScalePeriod(1.5f)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(2600, 5000), render));

        render = new ParticleBuilder(ParticleType.HOVER, new ArrayList<>(widgetMipmaps.subList(4, 5))).setParticleCount(20)
                .setPointSize(2, 10)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(3400, 5000), render));

//        7 片段固定位置缩放, 六个纹理
        Common11Paticle2 common11Paticle2 = new Common11Paticle2();
        render = new ParticleBuilder(ParticleType.SCALE_LOOP, new ArrayList<>(widgetMipmaps.subList(5, 11))).setParticleCount(7)
                .setPointSize(1, 4)
                .setLifeTime(Float.MAX_VALUE, 0f)
                .setPointScalePeriod(1.5f)
                .setPointScaleNodispear(true)
                .setPointScaleNoRandom(true)
                .setLoadAllParticleInFirstFrame(true)
                .setILocationAction(common11Paticle2)
                .setITexture(common11Paticle2)
                .build();
        //2000
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(5000, 6800), render));
        return true;
    }


    @Override
    protected int computeThemeCycleTime() {
        return 6800;
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
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * case 0:
     * return 1000;
     * case 1:
     * case 2:
     * case 3:
     * case 4:
     * case 5:
     * return 800;
     * case 6:
     * return 1800;
     * }
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        currentDuration = 800;
        switch (index % createActions()) {
            case 0:
                currentDuration = 1000;
                //中心点模糊出现
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 3, 0)
                        .setMoveAction(new EaseOutMultiple(5, false, true, false))
                        .setScale(1.3f, 1f).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //旋转90
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true)
                        .setMoveAction(new EaseInMultiple(8, false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 90)
                        .build());
                break;
            case 1:
                //右模糊进，进来后角度偏移，然后出去,注意角度和移动的时间差
                list.add(new AnimationBuilder((int) (currentDuration * 0.4f), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 1)
                        .setMoveAction(new EaseOutCubic(true, false, true))
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2, 1)
                        .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                        .setCoordinate(0.6f, 0, 0f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.6f), width, height, true)
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -1)
                        .setRotateAction(new EaseOutCubic())
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0.0f, 0, 2f, 0).build());
                break;
            case 2:
            case 4:
                //左模糊进，进来后角度偏移，然后出去
                list.add(new AnimationBuilder((int) (currentDuration * 0.4f), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 1)
                        .setRotaCenter(0, -0.7f)
                        .setMoveAction(new EaseOutCubic(true, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 2, 1)
                        .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                        .setCoordinate(-0.6f, 0, 0f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.6f), width, height, true)
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -1)
                        .setRotateAction(new EaseOutCubic())
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0.0f, 0, -2f, 0).build());
                break;
            case 3:
                //中心点模糊出现
                list.add(new AnimationBuilder((int) (currentDuration * 0.7), width, height, true)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 1, 0)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 60, 0)
                        .setRotateAction(new EaseOutCubic())
                        .setScaleAction(new EaseOutCubic())
                        .setMoveAction(new EaseCustomNoOffset(x -> Easings.SpringYSmoothDelay(x)))
                        .setIsSprintY(true)
                        .setScale(1.4f, 1.0f).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //模糊消失
                list.add(new AnimationBuilder((int) (currentDuration * 0.3), width, height, true)
                        .setScale(1.0f, 1.7f)
                        .setScaleAction(new EaseInCubic())
                        .build());
                break;
            case 5:
                //拉伸旋转
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setScale(1.7f, 1f)
                        .setScaleAction(new EaseOutCubic())
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 3, 0)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                list.add(new AnimationBuilder((int) (currentDuration * 0.5), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 0, 60)
                        .setScale(1f, 1.4f)
                        .setMoveAction(new EaseInCubic(false, true, true))
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 2)
                        .build());
                break;
            case 6:
                //缩小旋转，旋转放大，停顿，恢复
                list.add(new AnimationBuilder((int) (duration * 0.3), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 60, 0)
                        .setScale(1.4f, 1.0f)
                        .setMoveAction(new EaseOutCubic(false, true, true))
                        .setRatioBlurRange(2, 0, true)
                        .build());
                list.add(new AnimationBuilder((int) (duration * 0.3), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 0, 6)
                        .setScale(1.0f, 1.2f)
                        .build());
                list.add(new AnimationBuilder((int) (duration * 0.1), width, height, true)
                        .build());
                list.add(new AnimationBuilder((int) (duration * 0.3), width, height, true)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 6, 3)
                        .setScale(1.2f, 1.1f)
                        .build());
                break;

        }
        return list;
    }

    @Override
    protected int createActions() {
        return 7;
    }


}
