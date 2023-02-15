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
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 生日25
 */
public class Birthday25Action2 extends BaseBlurThemeTemplate {




    public Birthday25Action2(int totalTime, int width, int height) {
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
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);

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
                    {0f, Float.MIN_VALUE, 1f, 1f},
                    //蛋糕
                    {0f, Float.MAX_VALUE, 1.8f, 3.6f},
            };
        }
        return widgetsMeta;
    }

    /**
     * 图片环绕渲染
     */
    RepeatAroundFilter repeatAroundFilter;

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        repeatAroundFilter = new RepeatAroundFilter();
        repeatAroundFilter.init(mimaps.get(2), width, height, AnimateInfo.ORIENTATION.TOP);
        repeatAroundFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.TOP);

    }
    //灯-素材 绘制在最下面
    @Override
    protected void drawAfterWidget() {
        repeatAroundFilter.draw();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        repeatAroundFilter.onDestroy();
    }


}
