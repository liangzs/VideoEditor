package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 生日21
 */
public class Birthday21Action0 extends BaseBlurThemeTemplate {


    public Birthday21Action0(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        initWidgetLazy();
    }




    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    public void initWidgetLazy() {
        super.initWidget();
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        widgets[1] = buildNewFadeInOutTemplateAnimate();
        widgets[2] = buildNewFadeInOutTemplateAnimate();
        widgets[3] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.TOP);
        if (width <= height) {
            buildSequencingEnterOut(4, 10, AnimateInfo.ORIENTATION.TOP);
        } else {
            buildSequencingEnterOut(4, 16, AnimateInfo.ORIENTATION.TOP);
        }

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    /**
     * 空间元数据 16:9
     */
    float[][] widgetsMeta_16_9;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //三种颜色效果
                    {1f, 0.1f, 0.7f, 1.4f, 1.4f},
                    {-1f, 0.2f, 0.7f, 1.4f, 1.4f},
                    {-0.8f, -0.8f, 0.7f, 1.4f, 1.4f},
                    //气球
                    {-0.6f, Float.MAX_VALUE, 2.2f, 1.1f, 1.6f},
                    //六只蜡烛
                    {-5f/6f, Float.MAX_VALUE, 16f, 8f, 10f},
                    {-3f/6f, Float.MAX_VALUE, 16f, 8f, 10f},
                    {-1f/6f, Float.MAX_VALUE, 16f, 8f, 10f},
                    {1f/6f, Float.MAX_VALUE, 16f, 8f, 10f},
                    {3f/6f, Float.MAX_VALUE, 16f, 8f, 10f},
                    {5f/6f, Float.MAX_VALUE, 16f, 8f, 10f},
            };
        }
        if (width <= height) {
            return widgetsMeta;
        } else {
            if (widgetsMeta_16_9 == null) {
                widgetsMeta_16_9 = new float[][]{
                        widgetsMeta[0],
                        widgetsMeta[1],
                        widgetsMeta[2],
                        widgetsMeta[3],
                        //12只蜡烛
                        {-11f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {-9f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {-7f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {-5f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {-3f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {-1f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {1f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {3f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {5f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {7f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {9f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                        {11f/12f, Float.MAX_VALUE, 16f, 8f, 10f},
                };
            }
            return widgetsMeta_16_9;
        }
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        if (width <= height) {
            super.init(bitmap, tempBit, mimaps, width, height);
        } else {
            List<Bitmap> resizeMap = new ArrayList<>(16);
            resizeMap.addAll(mimaps);
            resizeMap.addAll(resizeMap.subList(4, 10));
            super.init(bitmap, tempBit, resizeMap, width, height);
        }
    }
}
