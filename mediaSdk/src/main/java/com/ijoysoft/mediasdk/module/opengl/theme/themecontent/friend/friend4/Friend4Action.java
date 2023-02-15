package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend4;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.GifDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.List;

/**
 * 朋友4
 */
public class Friend4Action extends BaseBlurThemeTemplate {



    public Friend4Action(int totalTime, int width, int height, int index) {
        super(totalTime, DEFAULT_ENTER_TIME, 1800, DEFAULT_OUT_TIME, true);

        this.width = width;
        this.height = height;
        stayAction = initStayAction();
        enterAnimation = initEnterAnimation();
        outAnimation = initOutAnimation();

        setZView(initZView());


        setCoverWidgetsCount(1, 1);
        this.index = index;
        initWidgetAfterSetValue();
    }

    /**
     * 当前item位置
     */
    int index;


    @Override
    protected BaseEvaluate initEnterAnimation() {
        return new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(0f, 1f).setZView(0).setIsWidget(true).build();
    }

    @Override
    protected BaseEvaluate initOutAnimation() {
        return new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1f, 0f).setZView(0).setIsWidget(true).build();
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(1800, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    public void initWidgetAfterSetValue() {
        widgets[0] = buildNoneAnimationWidget();
        AnimationBuilder animationBuilder;


        BaseThemeExample[] widgetsSrc = new BaseThemeExample[4];

        //四个光效顺时针位移
        //左上
        widgetsSrc[0] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 1800, DEFAULT_OUT_TIME, true);
        //进入动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, 1.8f, 0f, 0f);
        widgetsSrc[0].setEnterAnimation(animationBuilder.build());
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, 0f, 1.8f, 0f);
        widgetsSrc[0].setOutAnimation(animationBuilder.build());
        widgetsSrc[0].setZView(0f);



        //右上
        widgetsSrc[1] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 1800, DEFAULT_OUT_TIME, true);
        //进入动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(-1.8f, 0f, 0f, 0f);
        widgetsSrc[1].setEnterAnimation(animationBuilder.build());
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, 0f, 0f, 1.8f);
        widgetsSrc[1].setOutAnimation(animationBuilder.build());
        widgetsSrc[1].setZView(0f);


        //右下
        widgetsSrc[2] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 1800, DEFAULT_OUT_TIME, true);
        //进入动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, -1.8f, 0f, 0f);
        widgetsSrc[2].setEnterAnimation(animationBuilder.build());
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, 0f, -1.8f, 0f);
        widgetsSrc[2].setOutAnimation(animationBuilder.build());
        widgetsSrc[2].setZView(0f);

        //左下
        widgetsSrc[3] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 1800, DEFAULT_OUT_TIME, true);
        //进入动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(1.8f, 0f, 0f, 0f);
        widgetsSrc[3].setEnterAnimation(animationBuilder.build());
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, 0f, 0f, -1.8f);
        widgetsSrc[3].setOutAnimation(animationBuilder.build());
        widgetsSrc[3].setZView(0f);

        widgets[5] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);

        //根据当前index， 与4取模确定动画类型
        int j = index % 4;
        int i = 1 ;
        while (j < 4) {
            widgets[i++] = widgetsSrc[j++];
        }
        j = 0;
        while (i < 5) {
            widgets[i++] = widgetsSrc[j++];
        }


    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f, 1f},null,null,null,null,null
            };
        }
        //根据当前index， 与4取模确定光效位置
        int j = index % 4;
        int i = 1 ;
        float[][] src = new float[][]{
                //左上
                {-0.9f, -0.9f, 0.5f, 1f, 1f},
                //右上
                {0.9f, -0.9f, 0.5f, 1f, 1f},
                //右下
                {0.9f, 0.9f, 0.5f, 1f, 1f},
                //左下
                {-0.9f, 0.9f, 0.5f, 1f, 1f},
        };
        while (j < 4) {
            widgetsMeta[i++] = src[j++];
        }
        j = 0;
        while (i < 5) {
            widgetsMeta[i++] = src[j++];
        }
        widgetsMeta[5] = TITLE_WIDGET_META[index%5];
        return widgetsMeta;
    }

    /**
     * 不同步骤的标题位置
     */
    private static final float[][] TITLE_WIDGET_META = new float[][]{
            {0f, 0.7f, 1.2f, 2.4f, 1.2f},
            {0f, 0.7f, 1.2f, 2.4f, 1.4f},
            {0f, Float.MAX_VALUE, 1.2f, 2f, 1.4f},
            {0f, 0.7f, 1.4f, 2.8f, 1.6333f},
            {0f, 0.75f, 1.5f, 3f, 1.5f}
    };

    /**
     * 七只蝴蝶的位置
     */
    private static final float[][] BUTTERFLY_WIDGET_META = new float[][] {
            {-0.7f, -0.7f, 5f, 5f, 5f},
            {-0.5f, -0.1f, 5f, 5f, 5f},
            {-0.5f, 0.4f, 5f, 5f, 5f},
            {0.1f, -0.35f, 5f, 5f, 5f},
            {0.4f, 0.25f, 5f, 5f, 5f},
            {0.5f, -0.2f, 5f, 5f, 5f},
            {0.8f, -0.8f, 5f, 5f, 5f},
    };

    /**
     * 组成爱心的蝴蝶的位置
     */
    private static final float[][] BUTTERFLY_PARTICLE_META = new float[][] {
            {0f, 0f, 0f, 0.2f},
            {0f, -0.3f, 0f, 0.2f},
            {-0.1f, -0.2f, 0f, 0.2f},
            {0.1f, -0.2f, 0f, 0.2f},
            {-0.2f, -0.35f, 0f, 0.2f},
            {0.2f, -0.35f, 0f, 0.2f},
            {-0.23f, -0.07f, 0f, 0.2f},
            {0.23f, -0.07f, 0f, 0.2f},
            {-0.37f, -0.18f, 0f, 0.2f},
            {0.37f, -0.18f, 0f, 0.2f},
            {-0.35f, -0.4f, 0f, 0.2f},
            {0.35f, -0.4f, 0f, 0.2f},
            {-0.5f, -0.3f, 0f, 0.2f},
            {0.5f, -0.3f, 0f, 0.2f},
    };




    GifOriginFilter[] butterflies;
    GifDrawer gifDrawer;

    @Override
    public void initGif(@NonNull List<List<GifDecoder.GifFrame>> gifs, int width, int height) {
        super.initGif(gifs, width, height);
        if (index % 5 != 4) {
            butterflies = new GifOriginFilter[BUTTERFLY_WIDGET_META.length];
            //widgetsMeta[2]为竖屏缩放，[3]为横屏缩放，[4]为1:1时的缩放
            //增加判断防止未适配的主题报错
            int scaleIndex = width < height ? 2 : (width == height && widgetsMeta[0].length > 4)? 4 : 3;
            for (int i = 0; i < BUTTERFLY_WIDGET_META.length; i++) {
                butterflies[i] = new GifOriginFilter();
                butterflies[i].onCreate();
                butterflies[i].setFrames(gifs.get(0));
                butterflies[i].adjustScaling(width, height, BUTTERFLY_WIDGET_META[i][0], BUTTERFLY_WIDGET_META[i][1], BUTTERFLY_WIDGET_META[i][scaleIndex], BUTTERFLY_WIDGET_META[i][scaleIndex]);
            }

        } else {
            gifDrawer = new GifDrawer(BUTTERFLY_PARTICLE_META);
            gifDrawer.init(gifs.get(0));
            gifDrawer.onSurfaceCreated();

        }
    }


    @Override
    protected void drawAfterWidget() {
        if (status != ActionStatus.OUT) {
            if (index % 5 != 4) {
                for (GifOriginFilter gif : butterflies) {
                    gif.draw();
                }
            } else {
                gifDrawer.onDrawFrame();
            }
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (index % 5 != 4) {
            for (GifOriginFilter gif : butterflies) {
                gif.onDestroy();
            }
        } else {
            gifDrawer.onDestroy();
        }
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (index % 5 == 2 && i == 5) {
            widgets[5].setVertex(transitionOpenglPosArray(widgets[5].getCube(), 0f, -0.1f));
        }
    }
}
