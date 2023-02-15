package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude7;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 态度5
 */
public class Attitude7Action2 extends BaseBlurThemeTemplate {



    public Attitude7Action2(int totalTime, int width, int height) {
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
        widgets[2] = buildNewFadeInOutTemplateAnimate();
        widgets[4] = buildNewScaleInOutTemplateAnimate(-1f, 1f);
        initRelative();
        relative[3] = 2;
        widgets[3] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
        widgets[3].setWidgetRelayed(widgets[2]);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //粉色效果
                    {-0.8f,- 0.8f, 0.3f, 0.3f, 0.6f},
                    //黄色效果
                    {0.8f, 0.5f, 0.3f, 0.3f, 0.6f},
                    //眼睛
                    {Float.MAX_VALUE, Float.MIN_VALUE, 2.5f, 5f, 2.5f},
                    //手
                    {-0.3f, 0f, 4f, 8f, 4f},
                    //hahahaha
                    {Float.MIN_VALUE, Float.MAX_VALUE, 0.6f, 1.2f, 0.6f}
            };
        }
        return widgetsMeta;
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //修复手的位置
        float[] eyesCube = widgets[2].getCube();
        float[] handCube = widgets[3].getCube();
        float right = eyesCube[4] - 1.1f * (eyesCube[4] - eyesCube[0]);
        float left = right - handCube[4] + handCube[0];
        handCube[0] = handCube[2] = left;
        handCube[4] = handCube[6] = right;
        handCube[1] = handCube[5] = handCube[5] + 0.05f;
        handCube[3] = handCube[7] = handCube[7] + 0.05f;
        widgets[3].setVertex(handCube);
        eyesCube[1] = eyesCube[5] = eyesCube[5] + 0.05f;
        eyesCube[3] = eyesCube[7] = eyesCube[7] + 0.05f;
        widgets[2].setVertex(eyesCube);
    }
}
