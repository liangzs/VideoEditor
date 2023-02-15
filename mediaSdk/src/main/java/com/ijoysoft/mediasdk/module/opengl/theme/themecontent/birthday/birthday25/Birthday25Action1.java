package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25;

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
 * 生日25
 */
public class Birthday25Action1 extends BaseBlurThemeTemplate {



    public Birthday25Action1(int totalTime, int width, int height) {
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
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.LEFT);
        widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.RIGHT);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //颜色效果
                    {0f, Float.MAX_VALUE, 1f, 1f},
                    //标题
                    {-0.3f, Float.MAX_VALUE, 2f, 4f},
                    //气球
                    {0.6f, Float.MAX_VALUE, 2.5f, 5f},
            };
        }
        return widgetsMeta;
    }



}
