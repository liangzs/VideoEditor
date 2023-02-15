package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2021/12/30  17:46
 */
public class EaseInExpo extends AbstractEase{


    @Deprecated
    public EaseInExpo(boolean transition, boolean scale, boolean rotate) {
        super(transition, scale, rotate, false);
    }

    public EaseInExpo(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeInExpo(x);
    }
}
