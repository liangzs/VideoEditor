package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude1;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 态度1
 */
public class Attitude1Action2 extends BaseBlurThemeTemplate {



    public Attitude1Action2(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    /**
     * 图片环绕渲染
     */
    RepeatAroundFilter repeatAroundFilter;


    @Override
    public void initWidget() {
        super.initWidget();
        //背景模糊效果
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //粉色效果
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //背景模糊效果
                    {0f, 0.42f, 1f, 1f},
                    //粉色效果
                    {1f, 0f, 0.5f, 0.5f}
            };
        }
        return widgetsMeta;
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //频谱
        repeatAroundFilter = new RepeatAroundFilter();
        repeatAroundFilter.init(mimaps.get(2), width, height, AnimateInfo.ORIENTATION.BOTTOM);
        repeatAroundFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM);
    }

    @Override
    protected void drawAfterWidget() {
        repeatAroundFilter.draw();
    }

}
