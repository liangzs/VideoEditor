package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2022/1/10  15:00
 */
public class EaseOutQuad extends AbstractEase{

    public EaseOutQuad(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.easeOutQuad(x);
    }
}
