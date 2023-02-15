package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2022/1/12  14:46
 */
public class EaseInBounce extends AbstractEase {

    public EaseInBounce(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeInBounce(x);
    }
}
