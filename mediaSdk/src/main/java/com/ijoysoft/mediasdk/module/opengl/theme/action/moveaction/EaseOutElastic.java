package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2022/1/12  15:11
 */
public class EaseOutElastic extends AbstractEase {



    public EaseOutElastic(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeOutElastic(x);
    }
}
