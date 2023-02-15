package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2021/12/30  18:07
 */
public class EaseInQuart extends AbstractEase{

    @Deprecated
    public EaseInQuart(boolean transition, boolean scale, boolean rotate) {
        super(transition, scale, rotate, false);
    }

    public EaseInQuart(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeInQuart(x);
    }
}
