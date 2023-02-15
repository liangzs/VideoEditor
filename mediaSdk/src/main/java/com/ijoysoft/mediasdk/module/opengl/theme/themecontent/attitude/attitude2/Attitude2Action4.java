package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude2;

import static com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneThemeManager.BD_ONE_ZVIEW;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

/**
 * 态度2
 */
public class Attitude2Action4 extends BaseThemeTemplate {


    public Attitude2Action4(int totalTime, int width, int height) {
        super(totalTime, width, height);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayAnimation res = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.ROTATE_Z, width, height, BD_ONE_ZVIEW);
        res.setZView(BD_ONE_ZVIEW);
        return res;
    }

    @Override
    protected BaseEvaluate initEnterAnimation() {
        return new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2f, 2f, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).setZView(BD_ONE_ZVIEW).build();
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
        return new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 2f, 0f).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 90).setZView(BD_ONE_ZVIEW).build();
    }

    @Override
    protected float initZView() {
        return BD_ONE_ZVIEW;
    }

    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        //中心表情
        widgets[0] = buildNewScaleInOutTemplateAnimateAtSelfCenter(0);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //表情
                    {0.6f, 0.65f, 3f, 3f, 2.6f},
            };
        }
//        if (width < height) {
//            widgetsMeta[0][1] = 0.75f;
//        } else {
//            widgetsMeta[0][1] = 0.6f;
//        }
        return widgetsMeta;
    }

//    @Override
//    public void onSizeChanged(int width, int height) {
//        super.onSizeChanged(width, height);
//        getWidgetsMeta();
//        if (widgets[0] == null) {
//            widgets[0] = buildNewScaleInOutTemplateAnimateAtSelfCenter(0);
//        } else {
//            //widgetsMeta[2]为竖屏缩放，[3]为横屏缩放，[4]为1:1时的缩放
//            //增加判断防止未适配的主题报错
//            int scaleIndex = width < height ? 2 : (width == height && widgetsMeta[0].length > 4) ? 4 : 3;
//            widgets[0].adjustSelfScaling(width, height, widgetsMeta[0][0], widgetsMeta[0][1], widgetsMeta[0][scaleIndex], widgetsMeta[0][scaleIndex]);
//            widgets[0].getEnterAnimation().setCoordinatesLazy(null, widgetsMeta[0][1], null, null);
//            widgets[0].getOutAnimation().setCoordinatesLazy(null, null, null, widgetsMeta[0][1]);
//        }
//    }

}
