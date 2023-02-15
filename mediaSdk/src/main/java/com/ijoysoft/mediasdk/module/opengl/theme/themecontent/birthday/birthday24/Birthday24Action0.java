package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 生日24
 */
public class Birthday24Action0 extends BaseBlurThemeTemplate {


    public Birthday24Action0(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }




    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        widgets[1] = buildNewFadeInOutTemplateAnimate();
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.LEFT);
        widgets[3] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.RIGHT);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //绿色效果
                    {-0.7f, 0.8f, 0.7f, 0.6f, 0.7f},
                    //蓝色效果
                    {1f, -0.75f, 0.6f, 0.6f, 0.6f},
                    //戴帽子的emoji
                    {Float.MIN_VALUE, Float.MAX_VALUE, 2f, 1.333f, 1.333f},
                    {Float.MAX_VALUE, Float.MAX_VALUE, 2f, 1.333f, 1.333f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 2 || i == 3) {
            float direct = i == 2 ? 1 : -1;
            float[] cube = widgets[i].getCube();
            //往中间移动半个屏幕
            cube = transitionOpenglPosArray(cube, direct, -0.05f);
            //从中间往两边移动
            cube = transitionRatioPosArray(cube, -direct * 1.7f, 0f);
            widgets[i].setVertex(cube);
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        IParticleRender myMovingDrawer = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(
                        Arrays.asList(
                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_hart1" + SUFFIX),
                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_hart2" + SUFFIX)
                        )
                )
                .setParticleCount(6)
                .setParticleSizeRatio(0.04f)
                .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                .setLoadAllParticleInFirstFrame(true)
                .build();
        myMovingDrawer.onSurfaceCreated();
        particleRenders = Collections.singletonList(myMovingDrawer);

    }

}
