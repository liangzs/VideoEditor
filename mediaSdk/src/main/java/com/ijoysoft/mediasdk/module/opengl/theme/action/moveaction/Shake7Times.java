package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

/**
 * @author hayring
 * @date 2022/1/6  11:38
 */
public class Shake7Times extends AbstractEase{

    @Deprecated
    public Shake7Times(boolean transition, boolean scale, boolean rotate) {
        super(transition, scale, rotate, false);
    }

    public Shake7Times(boolean transition, boolean scale, boolean rotate, boolean skew) {
        super(transition, scale, rotate, skew);
    }

    @Override
    public double function(double x) {
        return Easings.shake7Times(x);
    }
}
