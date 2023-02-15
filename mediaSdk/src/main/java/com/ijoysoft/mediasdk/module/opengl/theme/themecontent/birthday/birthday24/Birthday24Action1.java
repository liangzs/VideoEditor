package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
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
public class Birthday24Action1 extends BaseBlurThemeTemplate {



    public Birthday24Action1(int totalTime, int width, int height) {
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
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //红色效果
                    {0.5f, 0.8f, 0.6f, 0.6f, 0.6f},
                    //黄色效果
                    {-0.9f, -1f, 0.6f, 0.6f, 0.6f},
                    //标题
                    {0f, Float.MAX_VALUE, 1.1f, 2.2f, 1.2f},
            };
        }
        return widgetsMeta;
    }


    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 2) {
            widgets[2].setVertex(transitionOpenglPosArray(widgets[2].getCube(), 0f, -0.05f));
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        IParticleRender myMovingDrawer = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(
                        Arrays.asList(
                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_blur_heart" + SUFFIX),
                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_heart" + SUFFIX)
                        )
                )
                .setParticleCount(5)
                .setParticleSizeRatio(0.03f)
                .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                .setLoadAllParticleInFirstFrame(true)
                .build();
        myMovingDrawer.onSurfaceCreated();
        particleRenders = Collections.singletonList(myMovingDrawer);

    }


}
