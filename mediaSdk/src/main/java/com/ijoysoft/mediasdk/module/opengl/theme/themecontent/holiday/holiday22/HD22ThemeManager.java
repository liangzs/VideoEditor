package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday22;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
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
import java.util.Collections;
import java.util.List;


public class HD22ThemeManager extends BaseTimeThemeManager {

    public HD22ThemeManager() {
    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        if (index == 0) {
            return new ThemeWidgetInfo(0, 6800, 0, 0, 6800);
        }
        if (index == 1) {
            return new ThemeWidgetInfo(800, 2400, 1000, 2600, 6800);
        }
        if (index == 2) {
            return new ThemeWidgetInfo(0, 6800, 0, 0, 6800);
        }
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {
        if (index ==0) {
            widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f);
        }
        if (index == 1) {
            if (width > height) {
                widgetTimeExample.adjustScaling(0, 0.4f, 3f);
            } else if (width < height) {
                widgetTimeExample.adjustScaling(0, 0.7f, 1.5f);
            } else {
                widgetTimeExample.adjustScaling(0, 0.55f, 2f);
            }
            widgetTimeExample.setEnterAction(new AnimationBuilder(800).setFade(0, 1f));
            widgetTimeExample.setOutAction(new AnimationBuilder(1000).setFade(1f, 0f));
        }
        if (index == 2) {
            widgetTimeExample.adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 1f);
        }
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
        //1+2片段 渐变绿色
        super.customCreateWidget(widgetMipmaps);
        IParticleRender render = new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                .setListBitmaps(
                        Collections.singletonList(
                                BitmapUtil.createCircle(32, Color.parseColor("#a6c84d"), Paint.Style.FILL)
                        )
                )
                .setParticleCount(8)
                .setParticleSizeRatio(0.04f)
                .setLoadAllParticleInFirstFrame(true)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(0, 1800), render));
        //3眼睛
        render = new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                .setListBitmaps(
                        widgetMipmaps.subList(3, 7)
                )
                .setParticleCount(8)
                .setParticleSizeRatio(0.03f)
                .setLoadAllParticleInFirstFrame(true)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(1800, 2600), render));

        //4+5+6+7 蜘蛛
        render = new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                .setListBitmaps(
                        Collections.singletonList(widgetMipmaps.get(7))
                )
                .setParticleCount(12)
                .setSizeFloatingStart(0.3f)
                .setParticleSizeRatio(0.03f)
                .build();
        renders.add(TimeThemeRenderContainer.createContainer(new ThemeWidgetInfo(2600, 6800), render));


        return false;
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
     * @return
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        switch (index % createActions()) {
            case 0:
                //600ms
                //中心点模糊出现
                list.add(new AnimationBuilder((int) (duration / 2), width, height, true)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 3, 0)
                        .setMoveAction(new EaseOutMultiple(5, false, true, false))
                        .setScale(1.3f, 1f).build());
                //旋转90
                list.add(new AnimationBuilder((int) (duration / 2), width, height, true)
                        .setMoveAction(new EaseInMultiple(8, false, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 90)
                        .build());
                break;
            case 1:
                //右模糊进，进来后角度偏移，然后出去,注意角度和移动的时间差
                list.add(new AnimationBuilder((int) (duration * 0.4f), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 1)
                        .setMoveAction(new EaseOutCubic(true, false, true))
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 2, 1)
                        .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                        .setCoordinate(0.6f, 0, 0f, 0).build());

                list.add(new AnimationBuilder((int) (duration * 0.6f), width, height, true)
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -1)
                        .setRotateAction(new EaseOutCubic())
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0.0f, 0, 2f, 0).build());
                break;
            case 2:
            case 4:
                //左模糊进，进来后角度偏移，然后出去
                list.add(new AnimationBuilder((int) (duration * 0.4f), width, height, true)
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 5, 1)
                        .setRotaCenter(0, -0.7f)
                        .setMoveAction(new EaseOutCubic(true, false, true))
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 2, 1)
                        .setRotateAction(new EaseCustom(x -> Easings.smoothStepDelay(x)))
                        .setCoordinate(-0.6f, 0, 0f, 0).build());

                list.add(new AnimationBuilder((int) (duration * 0.6f), width, height, true)
                        .setRotaCenter(0, -0.7f)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 1, -1)
                        .setRotateAction(new EaseOutCubic())
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(0.0f, 0, -2f, 0).build());
                break;
            case 3:
                //中心点模糊出现
                list.add(new AnimationBuilder((int) (duration * 0.7), width, height, true)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 1, 0)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, 60, 0)
                        .setRotateAction(new EaseOutCubic())
                        .setScaleAction(new EaseOutCubic())
                        .setMoveAction(new EaseCustomNoOffset(x -> Easings.SpringYSmoothDelay(x)))
                        .setIsSprintY(true)
                        .setScale(1.4f, 1.0f).build());
                //模糊消失
                list.add(new AnimationBuilder((int) (duration * 0.3), width, height, true)
                        .setScale(1.0f, 1.7f)
                        .setScaleAction(new EaseInCubic())
                        .build());
                break;
            case 5:
                //拉伸旋转
                list.add(new AnimationBuilder((int) (duration * 0.5), width, height, true)
                        .setScale(1.7f, 1f)
                        .setScaleAction(new EaseOutCubic())
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 3, 0)
                        .build());

                list.add(new AnimationBuilder((int) (duration * 0.5), width, height, true)
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
