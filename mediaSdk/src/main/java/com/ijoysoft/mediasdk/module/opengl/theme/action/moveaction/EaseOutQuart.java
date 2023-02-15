package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2021/12/30  18:08
 */
public class EaseOutQuart extends AbstractEase {

    @Deprecated
    public EaseOutQuart(boolean transition, boolean scale, boolean rotate) {
        super(transition, scale, rotate, false);
    }

    public EaseOutQuart(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeOutQuart(x);
    }
}
