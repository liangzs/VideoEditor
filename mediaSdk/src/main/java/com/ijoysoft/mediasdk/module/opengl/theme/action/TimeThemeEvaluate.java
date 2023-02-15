package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.List;

/**
 * @author hayring
 * @date 2022/1/6  10:25
 */
public class TimeThemeEvaluate extends BaseEvaluate{

    public TimeThemeEvaluate(AnimationBuilder builder) {
        super(builder);
    }

    List<IRender> extraRender = null;

    /**
     * render生命周期
     * onCreated
     */
    public void onRendersSurfaceCreated() {
        if (extraRender != null) {
            for (IRender render : extraRender) {
                render.onSurfaceCreated();
            }
        }
    }

    /**
     * render生命周期
     * onSurfaceChanged
     */
    public void onRenderSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        if (extraRender != null) {
            for (IRender render : extraRender) {
                render.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
            }
        }
    }

    /**
     * render生命周期
     * onDrawFrame
     */
    public void onRenderDrawFrame() {
        if (extraRender != null) {
            LogUtils.v(getClass().getSimpleName(), "extra render draw");
            for (IRender render : extraRender) {
                render.onDrawFrame();
            }
        }
    }

    /**
     * render生命周期
     * onDestroy
     */
    public void onRenderDestroy() {
        if (extraRender != null) {
            for (IRender render : extraRender) {
                render.onDestroy();
            }
        }
    }

    public List<IRender> getExtraRender() {
        return extraRender;
    }

    public void setExtraRender(List<IRender> extraRender) {
        this.extraRender = extraRender;
    }
}
