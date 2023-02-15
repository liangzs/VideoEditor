package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2022/1/20  18:30
 */
public class Shake3TimesEasingOutExpo extends AbstractEase{

    public Shake3TimesEasingOutExpo(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.shake3Times(Easings.easeOutExpo(x));
    }
}
