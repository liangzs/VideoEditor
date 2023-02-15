package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2021/12/31  9:24
 */
public class EaseOutBack extends AbstractEase {

    @Deprecated
    public EaseOutBack(boolean transition, boolean scale, boolean rotate) {
        super(transition, scale, rotate, false);
    }

    public EaseOutBack(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeOutBack(x);
    }
}
