package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam2;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 欧南节2
 */
public class Onam2Action4 extends BaseBlurThemeTemplate {


    public Onam2Action4(int totalTime, int width, int height) {
        super(totalTime, width, height, true,  true);
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
        //海, x轴拉伸后恢复
//        widgets[0] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME + DEFAULT_STAY_TIME, 0, DEFAULT_OUT_TIME, true);
//        widgets[0].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME + DEFAULT_STAY_TIME)
//                .setIsNoZaxis(true).setScale(2f, 1f).setZView(0)
//                .setOnlyScaleX(true)
//                .build());
//        widgets[0].getEnterAnimation().setIsSubAreaWidget(true, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
//        widgets[0].setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setFade(1f, 0f).build());
//        widgets[0].setZView(0);
//        widgets[0] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        //船
        widgets[0] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        //左右云
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //海
//                    {0f, 0.71f, 1f, 1f},
                    //船
                    {-0.05f, 0.5f, 1.5f, 3f},
                    //左云
                    {-0.5861f, -0.8375f, 2.416f, 4.832f},
                    //右云
                    {0.3736f, -0.696f, 1.6f, 3.2f},
            };
        }
        return widgetMeta;
    }



    /**
     * 图片环绕渲染
     */
    RepeatAroundFilter repeatAroundFilter;
    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        repeatAroundFilter = new RepeatAroundFilter();
        repeatAroundFilter.init(mimaps.get(3), width, height, AnimateInfo.ORIENTATION.BOTTOM);
        repeatAroundFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM);

    }
    //海-素材 绘制在最下面
    @Override
    protected void drawBeforeWidget() {
        repeatAroundFilter.draw();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        repeatAroundFilter.onDestroy();
    }


}
