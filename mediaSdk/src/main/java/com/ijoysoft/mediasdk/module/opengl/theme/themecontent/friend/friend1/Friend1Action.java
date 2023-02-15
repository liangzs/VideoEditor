package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend1;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.List;

/**
 * 朋友1
 */
public class Friend1Action extends BaseBlurThemeTemplate {


    public Friend1Action(int totalTime, int width, int height, int index) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
        this.index = index % MOD;
        initWidgetAfter();
    }

    /**
     * 当前顺序
     */
    int index;

    /**
     * 不同种类数量
     */
    private static final int MOD = 5;



    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    public void initWidgetAfter() {
        widgets[0] = buildNoneAnimationWidget();
//        if (index == 0) {
//            widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
//        } else {
            widgets[1] = buildNoneAnimationWidget();
//        }
    }

    /**
     * 空间元数据
     */
    private static final float[][] widgetsMeta = new float[][]{
            //边框
            {0f, 0f, 1f, 1f},
            //标题
            {0f, 0.45f, 1.7f, 3f, 3f},
    };




    @Override
    protected float[][] getWidgetsMeta() {
        return widgetsMeta;
    }


}
