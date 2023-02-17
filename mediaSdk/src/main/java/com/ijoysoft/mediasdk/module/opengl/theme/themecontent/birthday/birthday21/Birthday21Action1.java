package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 生日21
 */
public class Birthday21Action1 extends BaseBlurThemeTemplate {



    public Birthday21Action1(int totalTime, int width, int height) {
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
        widgets[0] = buildNewScaleInOutTemplateAnimateAtSelfCenter(0);
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
        widgets[3] = buildNewScaleInOutTemplateAnimateAtSelfCenter(3);
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
                    {0.7f, -1f, 0.7f, 1.4f, 1.4f},
                    {-1f, -0.7f, 0.7f, 1.4f, 1.4f},
                    {1f, 0.4f, 0.7f, 1.4f, 1.4f},
                    //标题
                    {0f, Float.MAX_VALUE, 1.2f, 1.6f, 1.8f},
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

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 4) {
            float[] cube = widgets[4].getCube();
            float deltaY = cube[3] - cube[1];
            widgets[3].setVertex(transitionOpenglPosArray(widgets[3].getCube(), 0f, deltaY));
        }
    }
}