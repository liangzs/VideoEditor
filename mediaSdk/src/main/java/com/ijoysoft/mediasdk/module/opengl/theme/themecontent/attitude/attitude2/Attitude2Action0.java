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
public class Attitude2Action0 extends BaseThemeTemplate {


    public Attitude2Action0(int totalTime, int width, int height) {
        super(totalTime, width, height);
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
                    {0f, 0.65f, 2.6f, 2.9f, 2.9f},
            };
        }
//        if (width < height) {
//            widgetsMeta[0][1] = 0.65f;
//        } else {
//            widgetsMeta[0][1] = 0.65f;
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

    @Override
    protected BaseEvaluate initStayAction() {
        StayAnimation res = new StayAnimation(DEFAULT_STAY_TIME, AnimateInfo.STAY.SPRING_Y);
        res.setZView(BD_ONE_ZVIEW);
        return res;
    }

    @Override
    protected BaseEvaluate initEnterAnimation() {
        return new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).setZView(BD_ONE_ZVIEW).build();
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
        return new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, -2.5f, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 0, 360).setZView(BD_ONE_ZVIEW).build();
    }

    /**
     * z轴摄影机位置
     * @return 0
     */
    @Override
    protected float initZView() {
        return BD_ONE_ZVIEW;
    }


}