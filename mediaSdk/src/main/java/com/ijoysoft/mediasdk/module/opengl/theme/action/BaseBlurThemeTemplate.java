package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.graphics.Bitmap;

import androidx.annotation.CallSuper;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.RotateImageDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.List;

/**
 * 通用Action类
 * 此类与 {@link BaseThemeTemplate} 除了父类以外代码一致，若有改动务必共同修改！！！！！！！！！
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BirthDayActionOne}
 */
public abstract class BaseBlurThemeTemplate extends BaseBlurThemeExample {


    /**
     * 进出场效果可控
     */
    protected boolean enterOutDiy = false;


    /**
     * 非覆盖控件在widgets中的起始下标
     */
    protected int notCoverWidgetsStartIndex = 0;

    /**
     * 边框类型的控件（边框，半透明效果等拉伸填充屏幕的控件数量）
     */
    protected int paddingCount = 0;

    /**
     * 自身静态控件数组
     */
    protected BaseThemeExample[] widgets;

    /**
     * 旋转控件变大速度
     * 默认值6F {@link com.ijoysoft.mediasdk.module.opengl.particle.RotateImageSystem#scaleSpeed}
     */
    protected Float rotateScaleSpeed = null;


    /**
     * 屏幕宽度
     */
    protected int width;

    /**
     * 屏幕高度
     */
    protected int height;

    /**
     * 自身旋转控件数组
     */
    protected RotateImageDrawer[] rotateWidgets;

    /**
     * 其他粒子系统
     */
    protected List<IParticleRender> particleRenders = null;

    /**
     * 依赖其他控件定位
     * 一般不依赖则relative[i] = i;
     */
    protected int[] relative;


    public BaseBlurThemeTemplate(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        this.width = width;
        this.height = height;
        stayAction = initStayAction();
        enterAnimation = initEnterAnimation();
        outAnimation = initOutAnimation();

        setZView(initZView());
    }

    public BaseBlurThemeTemplate(int totalTime, int width, int height, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, isNoZaxis);
        this.width = width;
        this.height = height;
        stayAction = initStayAction();
        enterAnimation = initEnterAnimation();
        outAnimation = initOutAnimation();

        setZView(initZView());
    }

    public BaseBlurThemeTemplate(int totalTime, int width, int height, boolean enterOutDiy, boolean isNoZaxis) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, isNoZaxis);
        this.width = width;
        this.height = height;
        stayAction = initStayAction();

        //用户控制转场
        if (enterOutDiy) {
            this.enterOutDiy = true;
        } else {
            enterAnimation = initEnterAnimation();
            outAnimation = initOutAnimation();
        }

        setZView(initZView());
    }

    public BaseBlurThemeTemplate(int duration, int enterTime, int stayTime, int outTime, boolean isNoZaxis) {
        super(duration, enterTime, stayTime, outTime, isNoZaxis);
    }

    /**
     * 子类实现停留动画
     *
     * @return 具体停留动画
     */
    protected abstract BaseEvaluate initStayAction();

    /**
     * 子类实现进入动画
     *
     * @return 具体进入动画
     */
    protected BaseEvaluate initEnterAnimation() {
        return null;
    }

    /**
     * 子类实现退出动画
     *
     * @return 具体退出动画
     */
    protected BaseEvaluate initOutAnimation() {
        return null;
    }

    /**
     * 子类确定摄像机z轴位置
     *
     * @return 摄像机z轴位置
     */
    protected float initZView() {
        return 0;
    }


    /**
     * 初始化控件
     * 本类作为父类先初始化数组
     */
    @Override
    public void initWidget() {
        widgets = new BaseThemeExample[getWidgetsMeta().length];
    }


    /**
     * 初始化依赖定位数组
     */
    protected void initRelative() {
        relative = new int[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            relative[i] = i;
        }
    }



    /**
     * 控件绘制
     */
    @Override
    public void drawWiget() {
        //最先绘制
        drawBeforeWidget();
        //绘制控件
        drawWidgets();
        //绘制旋转对象
        drawRotateImage();
        //绘制粒子系统
        drawParticles();
        //最后绘制
        drawAfterWidget();
    }

    /**
     * 在控件之前绘制
     */
    protected void drawBeforeWidget() {

    }

    /**
     * 在控件之后绘制
     */
    protected void drawAfterWidget() {

    }

    /**
     * 绘制粒子系统
     */
    protected void drawParticles() {
        if (particleRenders != null) {
            for (IParticleRender particleRender : particleRenders) {
                particleRender.onDrawFrame();
            }
        }
    }

    /**
     * 绘制旋转对象
     */
    protected void drawRotateImage() {
        //旋转对象不播放out
        if (enterOutDiy || status != ActionStatus.OUT) {
            if (rotateWidgets != null) {
                for (RotateImageDrawer rotateWidget : rotateWidgets) {
                    if (rotateWidget != null) {
                        rotateWidget.onDrawFrame();
                    } else {
                        LogUtils.w(getClass().getSimpleName(), "rotateWidgets is null!");
                    }
                }
            }
        }
    }


    /**
     * 绘制控件
     */
    protected void drawWidgets() {
        for (BaseThemeExample widget : widgets) {
            if (widget != null) {
                widget.drawFrame();
            }
        }
    }


    private static final double SOURCE_RATIO = 16d / 9d;



    /**
     * mimap中，旋转素材放在静态素材之后！！ {@link com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment}'
     *
     * widgetsMeta 设置 Float.MAX_VALUE / Float.MIN_VALUE 代表快速定位屏幕边缘位置， 调用 {@link BaseThemeExample#adjustScaling(int, int, int, int, AnimateInfo.ORIENTATION, float, float)}
     */
    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        //素材位置信息元数据
        float[][] widgetsMeta = getWidgetsMeta();
        if (widgetsMeta.length == 0) {
            return;
        }

        //widgetsMeta[2]为竖屏缩放，[3]为横屏缩放，[4]为1:1时的缩放
        //增加判断防止未适配的主题报错
        int scaleIndex = width < height ? 2 : (width == height && widgetsMeta[0].length > 4)? 4 : 3;


        int i = 0;

        //边框控件快速拉伸
        for (; i < paddingCount; i++) {
            widgets[i].init(mimaps.get(i), width, height);
            widgets[i].adjustPaddingScaling();
        }



        //对铺满屏幕边框的控件进行拉伸
        for (; i < notCoverWidgetsStartIndex; i++) {
            widgets[i].init(mimaps.get(i), width, height);
            widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(),
                    (float) (widgetsMeta[i][0] * ((double) width / height) * SOURCE_RATIO),
                    widgetsMeta[i][1],
                    (float) (widgetsMeta[i][scaleIndex] * (1 / (((double) width / height) * SOURCE_RATIO))),
                    widgetsMeta[i][scaleIndex]);
            //控件依赖定位
            if (relative == null || relative[i] == i) {
                widgets[i].fixEnterOutAnimationStartEndAtScreenEdge();
            }
        }

        //对控件进行定位和拉伸
        for (; i < widgets.length; i++) {
            widgets[i].init(mimaps.get(i), width, height);
            //水平方向上贴紧左或右
            if (widgetsMeta[i][0] == Float.MAX_VALUE || widgetsMeta[i][0] == Float.MIN_VALUE) {
                AnimateInfo.ORIENTATION orientation;
                if (widgetsMeta[i][0] == Float.MAX_VALUE) {
                    //往右边贴
                    if (widgetsMeta[i][1] == Float.MAX_VALUE) {
                        //往右下边贴
                        widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), AnimateInfo.ORIENTATION.RIGHT_BOTTOM, 0f, widgetsMeta[i][scaleIndex]);
                    } else if (widgetsMeta[i][1] == Float.MIN_VALUE) {
                        //往右上边贴
                        widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), AnimateInfo.ORIENTATION.RIGHT_TOP, 0f, widgetsMeta[i][scaleIndex]);
                    } else {
                        widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), AnimateInfo.ORIENTATION.RIGHT, widgetsMeta[i][1], widgetsMeta[i][scaleIndex]);
                    }
                } else {
                    //往左边贴
                    if (widgetsMeta[i][1] == Float.MAX_VALUE) {
                        //往左下边贴
                        widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), AnimateInfo.ORIENTATION.LEFT_BOTTOM, 0f, widgetsMeta[i][scaleIndex]);
                    } else if (widgetsMeta[i][1] == Float.MIN_VALUE) {
                        //往左上边贴
                        widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), AnimateInfo.ORIENTATION.LEFT_TOP, 0f, widgetsMeta[i][scaleIndex]);
                    } else {
                        widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), AnimateInfo.ORIENTATION.LEFT, widgetsMeta[i][1], widgetsMeta[i][scaleIndex]);
                    }
                }

            } else if (widgetsMeta[i][1] == Float.MAX_VALUE || widgetsMeta[i][1] == Float.MIN_VALUE) {
                //竖直方向上贴紧上或下
                AnimateInfo.ORIENTATION orientation =
                        widgetsMeta[i][1] == Float.MAX_VALUE ? AnimateInfo.ORIENTATION.BOTTOM : AnimateInfo.ORIENTATION.TOP;
                widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(), orientation, widgetsMeta[i][0], widgetsMeta[i][scaleIndex]);
            } else {
                if (relative != null && relative[i] != i) {
                    //相对位置定位
                    int targetWidget = relative[i];
                    float[] cube = widgets[targetWidget].getCube();
                    widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(),
                            (widgetsMeta[i][0] *(((float) height))/((float) width)) + ((cube[0] + cube[4]) / 2f),
                            (widgetsMeta[i][1] *(((float) width))/((float) height)) +  ((cube[1] + cube[3]) / 2f),
                            widgetsMeta[i][scaleIndex], widgetsMeta[i][scaleIndex]);
                } else {
                    //什么都没有，最简单的依靠传入位置进行定位
                    widgets[i].adjustScaling(width, height, mimaps.get(i).getWidth(), mimaps.get(i).getHeight(),
                            widgetsMeta[i][0],
                            widgetsMeta[i][1],
                            widgetsMeta[i][scaleIndex], widgetsMeta[i][scaleIndex]);
                }
            }
            fixNotCoverWidgetsPositionManually(i);
            widgets[i].fixEnterOutAnimationStartEndAtScreenEdge();
        }

        //可能包含动画函数修复
        onWidgetBitmapInitialized();

        //旋转控件
        float[][][] rotateWidgetsMeta = getRotateWidgetsMeta();
        rotateWidgets = new RotateImageDrawer[rotateWidgetsMeta.length];
        for (int j = 0; j < rotateWidgetsMeta.length; j++) {
            if (rotateScaleSpeed == null) {
                rotateWidgets[j] = new RotateImageDrawer(rotateWidgetsMeta[j]);
            } else {
                rotateWidgets[j] = new RotateImageDrawer(rotateWidgetsMeta[j], rotateScaleSpeed);
            }
            rotateWidgets[j].init(mimaps.get(i++));
        }

//        if (particleRenders != null) {
//            for (IParticleRender particleRender : particleRenders) {
//                particleRender.onSurfaceCreated();
//            }
//        }



    }


    /**
     * 子类重写后手动修复控件位置
     */
    protected void fixNotCoverWidgetsPositionManually(int i) {

    }

    /**
     * init之后执行
     */
    protected void onWidgetBitmapInitialized() {

    }

    /**
     * 空控件数据
     */
    static final float[][] EMPTY_WIDGETS_META = new float[0][];

    /**
     * 获取素材元数据
     *
     * @return 素材元数据
     */
    protected float[][] getWidgetsMeta() {
        return EMPTY_WIDGETS_META;
    }

    ;

    /**
     * 空控件数据
     */
    static final float[][][] EMPTY_ROTATE_WIDGETS_META = new float[0][][];

    /**
     * 获取旋转控件元数据
     *
     * @return 旋转控件元数据
     */
    protected float[][][] getRotateWidgetsMeta() {
        return EMPTY_ROTATE_WIDGETS_META;
    }


    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (widgets != null) {
            for (BaseThemeExample widget : widgets) {
                if (widget != null) {
                    widget.onDestroy();
                }
            }
        }
        if (rotateWidgets != null) {
            for (RotateImageDrawer rotateWidget : rotateWidgets) {
                if (rotateWidget != null) {
                    rotateWidget.onDestroy();
                }
            }
        }
        if (particleRenders != null) {
            for (IParticleRender particleRender : particleRenders) {
                particleRender.onDestroy();
            }
        }
    }


    /**
     * 虚幻缩放，使用 {@link BaseBlurThemeExample} 的方法
     *
     * @param width  屏幕宽度
     * @param height 屏幕高度
     * @see BaseBlurThemeExample(int, int)
     */
    @Override
    public void adjustImageScaling(int width, int height) {
        super.adjustImageScaling(width, height);
    }

    /**
     * 比例调整后，将动画重置为未修复模式
     * @param widgetIndex 动画控件位置
     * @param in 进入
     * @param out 出场
     */
    public void resetWidgetAnimationToUnFix(int widgetIndex, AnimateInfo.ORIENTATION in, AnimateInfo.ORIENTATION out) {
        BaseThemeExample widget = widgets[widgetIndex];
        if (widget == null) {
            return;
        }

        //进入动画
        AnimationBuilder animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        if (zView == 0) {
            animationBuilder.setIsNoZaxis(true);
        } else {
            animationBuilder.setZView(zView);
        }
        switch (in) {
            case LEFT:
                animationBuilder.setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f);
                break;
            case TOP:
                animationBuilder.setCoordinate(0f, Float.MIN_VALUE, 0f, 0f);
                break;
            case RIGHT:
                animationBuilder.setCoordinate(Float.MAX_VALUE, 0f, 0f, 0f);
                break;
            case BOTTOM:
                animationBuilder.setCoordinate(0f, Float.MAX_VALUE, 0f, 0f);
                break;
            default:
                throw new IllegalArgumentException("multiply Orientation is not support!");
        }
        widget.setEnterAnimation(animationBuilder.build());
        //退出动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        if (zView == 0) {
            animationBuilder.setIsNoZaxis(true);
        } else {
            animationBuilder.setZView(zView);
        }
        switch (out) {
            case LEFT:
                animationBuilder.setCoordinate(0f, 0f, Float.MIN_VALUE, 0f);
                break;
            case TOP:
                animationBuilder.setCoordinate(0f, 0f, 0f, Float.MIN_VALUE);
                break;
            case RIGHT:
                animationBuilder.setCoordinate(0f, 0f, Float.MAX_VALUE, 0f);
                break;
            case BOTTOM:
                animationBuilder.setCoordinate(0f, 0f, 0f, Float.MAX_VALUE);
                break;
            default:
                throw new IllegalArgumentException("multiply Orientation is not support!");
        }
        widget.setOutAnimation(animationBuilder.build());
    }


    /**
     * 一行代码生成线性模板动画
     *
     * @param in        进入方向
     * @param out       退出方向
     * @param zView     z轴摄影机位置
     * @return 动画
     */
    public BaseThemeExample buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION in, AnimateInfo.ORIENTATION out, int zView) {
        return buildNewCoordinateTemplateAnimate(totalTime, in, out, zView);
    }

    /**
     * 一行代码生成线性模板动画
     *
     * @param totalTime 总时间
     * @param in        进入方向
     * @param out       退出方向
     * @param zView     z轴摄影机位置
     * @return 动画
     */
    public static BaseThemeExample buildNewCoordinateTemplateAnimate(int totalTime, AnimateInfo.ORIENTATION in, AnimateInfo.ORIENTATION out, int zView) {
        BaseThemeExample widget = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        //进入动画
        AnimationBuilder animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        if (zView == 0) {
            animationBuilder.setIsNoZaxis(true);
        } else {
            animationBuilder.setZView(zView);
        }
        switch (in) {
            case LEFT:
                animationBuilder.setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f);
                break;
            case TOP:
                animationBuilder.setCoordinate(0f, Float.MIN_VALUE, 0f, 0f);
                break;
            case RIGHT:
                animationBuilder.setCoordinate(Float.MAX_VALUE, 0f, 0f, 0f);
                break;
            case BOTTOM:
                animationBuilder.setCoordinate(0f, Float.MAX_VALUE, 0f, 0f);
                break;
            default:
                throw new IllegalArgumentException("multiply Orientation is not support!");
        }
        widget.setEnterAnimation(animationBuilder.build());
        //退出动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        if (zView == 0) {
            animationBuilder.setIsNoZaxis(true);
        } else {
            animationBuilder.setZView(zView);
        }
        switch (out) {
            case LEFT:
                animationBuilder.setCoordinate(0f, 0f, Float.MIN_VALUE, 0f);
                break;
            case TOP:
                animationBuilder.setCoordinate(0f, 0f, 0f, Float.MIN_VALUE);
                break;
            case RIGHT:
                animationBuilder.setCoordinate(0f, 0f, Float.MAX_VALUE, 0f);
                break;
            case BOTTOM:
                animationBuilder.setCoordinate(0f, 0f, 0f, Float.MAX_VALUE);
                break;
            default:
                throw new IllegalArgumentException("multiply Orientation is not support!");
        }
        widget.setOutAnimation(animationBuilder.build());
        widget.setZView(zView);
        return widget;
    }

    /**
     * 一行代码生成线性模板动画
     *
     * @param in        进入方向
     * @param out       退出方向
     * @return 动画
     */
    public BaseThemeExample buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION in, AnimateInfo.ORIENTATION out) {
        return buildNewCoordinateTemplateAnimate(totalTime, in, out, 0);
    }

    /**
     * 一行代码生成线性模板动画
     *
     * @param totalTime 总时间
     * @param in        进入方向
     * @param out       退出方向
     * @return 动画
     */
    public static BaseThemeExample buildNewCoordinateTemplateAnimate(int totalTime, AnimateInfo.ORIENTATION in, AnimateInfo.ORIENTATION out) {
        return buildNewCoordinateTemplateAnimate(totalTime, in, out, 0);
    }


    /**
     * 一行代码生成淡入淡出模板动画
     *
     * @param totalTime 总时间
     * @param zView     z轴摄影机位置
     * @return 动画
     */
    public static BaseThemeExample buildNewFadeInOutTemplateAnimate(int totalTime, int zView) {
        BaseThemeExample widget = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        //进入动画
        AnimationBuilder animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        if (zView == 0) {
            animationBuilder.setIsNoZaxis(true);
        } else {
            animationBuilder.setZView(zView);
        }
        //淡入
        animationBuilder.setFade(0f, 1f);
        widget.setEnterAnimation(animationBuilder.build());
        //退出动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        if (zView == 0) {
            animationBuilder.setIsNoZaxis(true);
        } else {
            animationBuilder.setZView(zView);
        }
        //淡出
        animationBuilder.setFade(1f, 0f);
        widget.setOutAnimation(animationBuilder.build());
        widget.setZView(zView);
        return widget;
    }





    /**
     * 一行代码生成淡入淡出模板动画
     * @return 动画
     */
    public BaseThemeExample buildNewFadeInOutTemplateAnimate() {
        return buildNewFadeInOutTemplateAnimate(totalTime);
    }


    /**
     * 一行代码生成淡入淡出模板动画
     *
     * @param totalTime 总时间
     * @return 动画
     */
    public static BaseThemeExample buildNewFadeInOutTemplateAnimate(int totalTime) {
        return buildNewFadeInOutTemplateAnimate(totalTime, 0);
    }

    /**
     * 一行代码生成变大变小模板动画
     * 以自己的中心为放大中心
     * @param indexInWidgetsMeta 空间数据中的位置
     * @return 动画
     */
    public BaseThemeExample buildNewScaleInOutTemplateAnimateAtSelfCenter(int indexInWidgetsMeta) {
        float x = getWidgetsMeta()[indexInWidgetsMeta][0];
//        x = x == Float.MAX_VALUE ? 1 : x == Float.MIN_VALUE ? -1 : x;
        float y = getWidgetsMeta()[indexInWidgetsMeta][1];
//        y = y == Float.MAX_VALUE ? 1 : y == Float.MIN_VALUE ? -1 : y;

        return buildNewScaleInOutTemplateAnimate(totalTime, x, y, 1f, 1f);
    }


    /**
     * 一行代码生成变大变小模板动画
     *
     * @param x         进入和退出相同的x坐标
     * @param y         进入和退出相同的y坐标
     * @return 动画
     */
    public BaseThemeExample buildNewScaleInOutTemplateAnimate(float x, float y) {
        return buildNewScaleInOutTemplateAnimate(totalTime, x, y, 1f, 1f);
    }

    /**
     * 一行代码生成变大变小模板动画
     *
     * @param totalTime 总时间
     * @param x         进入和退出相同的x坐标
     * @param y         进入和退出相同的y坐标
     * @return 动画
     */
    public static BaseThemeExample buildNewScaleInOutTemplateAnimate(int totalTime, float x, float y) {
        return buildNewScaleInOutTemplateAnimate(totalTime, x, y, 1f, 1f);
    }

    /**
     * 一行代码生成变大变小模板动画
     *
     * @param totalTime 总时间
     * @param x         进入和退出相同的x坐标
     * @param y         进入和退出相同的y坐标
     * @param enterMax 进入动画结束时的大小
     * @param outMax 退出动画开始时的大小
     * @return 动画
     */
    public static BaseThemeExample buildNewScaleInOutTemplateAnimate(int totalTime, float x, float y, float enterMax, float outMax) {
        BaseThemeExample widget = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        //进入动画
        AnimationBuilder animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setZView(0f);
        //变大
        animationBuilder.setScale(0.0001f, enterMax);
        animationBuilder.setStartX(x).setStartY(y);
        widget.setEnterAnimation(animationBuilder.build());
        //退出动画
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setZView(0f);
        //变小
        animationBuilder.setScale(1f, outMax);
        animationBuilder.setEndX(x).setEndY(y);
        widget.setOutAnimation(animationBuilder.build());
        widget.setZView(0f);
        return widget;
    }

    /**
     * 一行代码生成无动画控件
     * @return 动画
     */
    public BaseThemeExample buildNoneAnimationWidget() {
        BaseThemeExample widget = new BaseThemeExample(totalTime, 0, totalTime, 0, true);
        widget.setZView(0f);
        return widget;
    }





    /**
     * 获取依赖的控件动画，动画从依赖控件处克隆
     * 在子类重写 {@link #initWidget()} 方法处设置
     * @param widget 控件
     * @return 新动画
     */
    public BaseThemeExample relayWidget(BaseThemeExample widget) {
        BaseThemeExample me = new BaseThemeExample(widget.getTotalTime(), widget.getEnterTime(),
                widget.getStayTime(), widget.getOutTime(), widget.isNoZaxis);
        me.animateWith(widget);
        me.setZView(widget.getzView());
        return me;
    }

    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        if (widgets != null) {
            for (BaseThemeExample widget : widgets) {
                if (widget != null) {
                    widget.drawFramePreview();
                }
            }
        }
    }


    /**
     * 顺序进出
     * @param startIndex 起点控件下标
     * @param endIndex 结束控件下标
     */
    public void buildSequencingEnterOut(int startIndex, int endIndex, AnimateInfo.ORIENTATION in, AnimateInfo.ORIENTATION out) {
        //相邻控件出现和消失的时间间隔120毫秒
        final int count = endIndex - startIndex;
        final int clipTime = 600/count;
        //下标从2开始，多减去两倍的时间
        final int enterTime = DEFAULT_ENTER_TIME - clipTime * (count + startIndex);
        final int outTime = DEFAULT_ENTER_TIME + clipTime * (count+ startIndex);
        int curEnterTime;
        int curOutTime;
        float startX = 0f;
        float startY = 0f;
        float endX = 0f;
        float endY = 0f;
        for (int i = startIndex; i < endIndex; i++) {
            curEnterTime = enterTime + i * clipTime;
            curOutTime = outTime - i * clipTime;
            widgets[i] = new BaseThemeExample(totalTime, curEnterTime, DEFAULT_STAY_TIME, curOutTime, true);
            switch (in) {
                case TOP: startY = Float.MIN_VALUE; break;
                case BOTTOM: startY = Float.MAX_VALUE; break;
                case LEFT: startX = Float.MIN_VALUE; break;
                case RIGHT: startX = Float.MAX_VALUE; break;
                default: throw new IllegalArgumentException("Other Orientation is not supported!");
            }
            widgets[i].setEnterAnimation(new AnimationBuilder(curEnterTime)
                    .setCoordinate(startX, startY, endX, endY)
                    .setIsNoZaxis(true).build());
            startX = 0f;
            startY = 0f;
            endX = 0f;
            endY = 0f;
            switch (out) {
                case TOP: endY = Float.MIN_VALUE; break;
                case BOTTOM: endY = Float.MAX_VALUE; break;
                case LEFT: endX = Float.MIN_VALUE; break;
                case RIGHT: endX= Float.MAX_VALUE; break;
                default: throw new IllegalArgumentException("Other Orientation is not supported!");
            }
            widgets[i].setOutAnimation(new AnimationBuilder(curOutTime)
                    .setCoordinate(startX, startY, endX, endY)
                    .setIsNoZaxis(true).build());
            widgets[i].setZView(0);
            startX = 0f;
            startY = 0f;
            endX = 0f;
            endY = 0f;

        }
    }


    /**
     * 顺序进出
     * @param startIndex 起点控件下标
     * @param endIndex 结束控件下标
     */
    public void buildSequencingEnterOut(int startIndex, int endIndex, AnimateInfo.ORIENTATION in) {
        //相邻控件出现和消失的时间间隔120毫秒
        final int count = endIndex - startIndex;
        final int clipTime = 600/count;
        //下标从2开始，多减去两倍的时间
        final int enterTime = DEFAULT_ENTER_TIME - clipTime * (endIndex - 1);
        int curEnterTime;
        for (int i = startIndex; i < endIndex; i++) {
            curEnterTime = enterTime + i * clipTime;
            widgets[i] = new BaseThemeExample(totalTime, curEnterTime, DEFAULT_STAY_TIME + DEFAULT_ENTER_TIME, 0, true);
            switch (in) {
                case TOP:
                    widgets[i].setEnterAnimation(new AnimationBuilder(curEnterTime)
                            .setCoordinate(0f, Float.MIN_VALUE, 0f, 0f)
                            .setIsNoZaxis(true).build());
                     break;
                case BOTTOM:
                    widgets[i].setEnterAnimation(new AnimationBuilder(curEnterTime)
                            .setCoordinate(0f, Float.MAX_VALUE, 0f, 0f)
                            .setIsNoZaxis(true).build());
                    break;
                case LEFT:
                    widgets[i].setEnterAnimation(new AnimationBuilder(curEnterTime)
                            .setCoordinate(Float.MIN_VALUE, 0f, 0f, 0f)
                            .setIsNoZaxis(true).build());
                    break;
                case RIGHT:
                    widgets[i].setEnterAnimation(new AnimationBuilder(curEnterTime)
                            .setCoordinate(Float.MAX_VALUE, 0f, 0f, 0f)
                            .setIsNoZaxis(true).build());
                    break;
                default: throw new IllegalArgumentException("Other Orientation is not supported!");
            }

        }
    }


    /**
     * 设置区分边框控件，覆盖控件，普通控件
     * [0, paddingCount)为边框控件，
     * [paddingCount, notCoverWidgetsStartIndex]为覆盖控件，
     * [notCoverWidgetsStartIndex, length)为普通控件
     * @param paddingCount 边框控件数量
     * @param notCoverWidgetsStartIndex 非普通控件数量
     */
    protected void setCoverWidgetsCount(int paddingCount, int notCoverWidgetsStartIndex) {
        //顺序检测
        if (0 <= paddingCount && paddingCount <= notCoverWidgetsStartIndex && notCoverWidgetsStartIndex <= getWidgetsMeta().length) {
            this.paddingCount = paddingCount;
            this.notCoverWidgetsStartIndex = notCoverWidgetsStartIndex;
        } else {
            throw new IllegalArgumentException("The size relationship of these two variables is illegal.");
        }
    }





}
