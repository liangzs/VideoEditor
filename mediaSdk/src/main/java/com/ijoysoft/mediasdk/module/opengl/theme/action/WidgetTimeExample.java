package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

public class WidgetTimeExample extends BaseThemeExample {


    private ThemeWidgetInfo widgetInfo;

    /**
     * 如果只有start和end，action设置到out里面
     * @param widgetInfo
     */
    public WidgetTimeExample(ThemeWidgetInfo widgetInfo) {
        super((int) widgetInfo.getTime(), widgetInfo.getEnter(), widgetInfo.getStay(), widgetInfo.getOut(), true);
        this.widgetInfo = widgetInfo;
    }

    /**
     * 默认赋值时间，width，height,z=0
     *
     * @param builder
     */
    public void setEnterAction(AnimationBuilder builder) {
        if (builder.duration == 0) {
            builder.setDuration(widgetInfo.getEnter());
        }
        setEnterAnimation(builder.setWidthHeight(width, height).setIsNoZaxis(true).build());
    }

    public void setStayAction(AnimationBuilder builder) {
        if (builder.duration == 0) {
            builder.setDuration(widgetInfo.getStay());
        }
        setStayAction(builder.setWidthHeight(width, height).setIsNoZaxis(true).build());
    }

    /**
     * 默认赋值时间，width，height
     *
     * @param builder
     */
    public void setOutAction(AnimationBuilder builder) {
        if (builder.duration == 0) {
            builder.setDuration(widgetInfo.getOut());
        }
        setOutAnimation(builder.setWidthHeight(width, height).setIsNoZaxis(true).build());
    }


    /**
     * 默认赋值时间，width，height,z=0
     */
    public void setEnterAction(BaseEvaluate baseEvaluate) {
        if (baseEvaluate.duration == 0) {
            baseEvaluate.setDuration(widgetInfo.getEnter());
        }
        baseEvaluate.setWidth(width);
        baseEvaluate.setHeight(height);
        setEnterAnimation(baseEvaluate);
    }

    public void setStayAction(BaseEvaluate baseEvaluate) {
        if (baseEvaluate.duration == 0) {
            baseEvaluate.setDuration(widgetInfo.getStay());
        }
        baseEvaluate.setWidth(width);
        baseEvaluate.setHeight(height);
        super.setStayAction(baseEvaluate);
    }

    /**
     * 默认赋值时间，width，height
     */
    public void setOutAction(BaseEvaluate baseEvaluate) {
        if (baseEvaluate.duration == 0) {
            baseEvaluate.setDuration(widgetInfo.getOut());
        }
        baseEvaluate.setWidth(width);
        baseEvaluate.setHeight(height);
        setOutAnimation(baseEvaluate);
    }


    public ThemeWidgetInfo getWidgetInfo() {
        return widgetInfo;
    }

    public void setWidgetInfo(ThemeWidgetInfo widgetInfo) {
        this.widgetInfo = widgetInfo;
    }

    public float[] adjustScalingFixX(AnimateInfo.ORIENTATION orientation) {
        if (isWidgetNotnull()) {
            return super.adjustScalingFixX(width, height, widgetInfo.getBitmap().getWidth(), widgetInfo.getBitmap().getHeight(), orientation, 1f);
        }
        return pos;
    }

    public float[] adjustScalingFixX(AnimateInfo.ORIENTATION orientation, float scale) {
        if (isWidgetNotnull()) {
            return super.adjustScalingFixX(width, height, widgetInfo.getBitmap().getWidth(), widgetInfo.getBitmap().getHeight(), orientation, scale);
        }
        return pos;
    }

    public float[] adjustScaling(float centerX, float centerY, float scale) {
        if (isWidgetNotnull()) {
            return super.adjustScaling(width, height, widgetInfo.getBitmap().getWidth(), widgetInfo.getBitmap().getHeight(), centerX, centerY, scale, scale);
        }
        return pos;
    }

    public float[] adjustScaling(AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        if (isWidgetNotnull()) {
            cube = super.adjustScaling(width, height, widgetInfo.getBitmap().getWidth(), widgetInfo.getBitmap().getHeight(), orientation, coord, scale);
            mVerBuffer.clear();
            mVerBuffer.put(cube).position(0);
            return cube;
        }
        return pos;
    }

    public float[] adjustScalingWithoutSettingCube(AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        if (isWidgetNotnull()) {
            return super.adjustScalingWithoutSettingCube(width, height, widgetInfo.getBitmap().getWidth(), widgetInfo.getBitmap().getHeight(), orientation, coord, scale);
        }
        return pos;
    }

    public float[] getCenterAfterScaling() {
        return new float[]{(cube[0] + cube[4]) / 2f, (cube[1] + cube[3]) / 2f};
    }

    /**
     *
     */
    public void init() {
        if (isWidgetNotnull()) {
            create();
            initTexture(widgetInfo.getBitmap());
        }
    }

    private boolean isWidgetNotnull() {
        if (widgetInfo.getBitmap() != null && !widgetInfo.getBitmap().isRecycled()) {
            return true;
        }
        LogUtils.e("WidgetTimeExample", "assert isWidgetNotnull failed");
        return false;
    }

    private boolean hadRest;

    public void onDrawFrame(int position) {
        if (widgetInfo.isInRange(position)) {
            drawFrame();
            hadRest = false;
            if (widgetInfo.isLastFrame(position)) {
                reset();
                hadRest = true;
            }
        } else {
            if (!hadRest) {
                reset();
            }
            hadRest = true;
        }
    }


}
