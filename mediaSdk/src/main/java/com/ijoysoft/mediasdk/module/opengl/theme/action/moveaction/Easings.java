package com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction;

import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;

import static java.lang.Math.*;

/**
 * 缓动函数
 * https://www.desmos.com/calculator?lang=zh-CN
 *
 * @author hayring
 * @date 2021/12/30  17:34
 */
public class Easings {


    /**
     * https://easings.net/cn#easeOutExpo
     */
    public static double easeOutExpo(double x) {
        return x == 1 ? 1 : 1 - pow(2, -10 * x);
    }


    /**
     * https://easings.net/cn#easeInExpo
     */
    public static double easeInExpo(double x) {
        return x == 0 ? 0 : pow(2, 10 * x - 10);
    }

    /**
     * https://easings.net/cn#easeOutQuart
     */
    public static double easeOutQuart(double x) {
        return 1 - pow(1 - x, 4);
    }


    /**
     * https://easings.net/cn#easeInQuart
     */
    public static double easeInQuart(double x) {
        return x * x * x * x;
    }


    /**
     * https://easings.net/cn#easeInSine
     */
    public static double easeInSine(double x) {
        return 1 - cos((x * PI) / 2);
    }

    /**
     * https://easings.net/cn#easeOutSine
     */
    public static double easeOutSine(double x) {
        return sin((x * PI) / 2);
    }

    /**
     * 原x * x * (3 - 2 * x)
     * 提前走完流程
     * 4x*x*(3-2*2x)
     *
     * @return
     */
    public static float smoothStepAhead(float x) {
        if (x > 0.5) {
            return 1;
        }
        return 4f * x * x * (3f - 4f * x);
    }

    /**
     * 原x * x * (3 - 2 * x)
     * 延缓走完流程
     * 4\left(x-0.5\right)^{2}\left(3-4\left(x-0.5\right)\right)
     *
     * @return
     */
    public static float smoothStepDelay(float x) {
        if (x < 0.5) {
            return 0;
        }
        return (float) (4f * Math.pow(x - 0.5, 2) * (3f - 4f * (x - 0.5)));
    }

    /**
     * c1, c3 different from:
     * https://easings.net/cn#easeOutBack
     */
    public static double easeOutBack(double x) {
        final double c1 = 3;
        final double c3 = c1 + 1;
        return 1 + c3 * pow(x - 1, 3) + c1 * pow(x - 1, 2);
    }

    /**
     * sqrt(1 - pow(x - 1, 2));
     *
     * @param x
     * @return
     */
    public static float easeOutCirc(float x) {
        return (float) Math.sqrt(1 - Math.pow(x - 1, 2));
    }

    /**
     * 2^{-1\cdot\left(x\right)}\cdot\sin\left(2\pi\left(x-0.2\right)\right)\cdot0.2
     * <p>
     * factor = 0.2
     * * pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor)
     *
     * @param x
     * @return
     */
    public static float SpringYSmoothDelay(float x) {
        if (x < 0.2) {
            return 0;
        }
        return (float) (Math.pow(2, -x) * Math.sin(2 * Math.PI * (x - 0.2)) * 0.2);
    }

    /**
     * 2^{-3\cdot\left(x+0.5\right)}\cdot\sin\left(5\pi x\right)10
     * (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(x * Math.PI / 0.2f) * 10f);
     * <p>
     * factor = 0.2
     * * pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor)
     *
     * @param x
     * @return
     */
    public static float easeSpring(float x) {
        return (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(5 * Math.PI * x) * 10);
    }

    public static float easeSpring(float x, float frator) {
        return (float) (Math.pow(2, -3 * (x + 0.5)) * Math.sin(5 * Math.PI * x) * frator);
    }

    /**
     * 2^{-10\cdot x}\cdot\sin\left(5\pi\left(x+0.2\right)\right)
     * 延迟0.2缓慢弹动
     *
     * @param x
     * @return
     */
    public static float easeSpringSmoothDelay(float x) {
        return (float) (Math.pow(2, -10 * x) * Math.sin(2 * Math.PI * (x)) * 0.1);
    }

    /**
     * 2^{-2\cdot\left(x\right)}\cdot\sin\left(2\pi x\right)\cdot0.2
     * 弹簧两次
     *
     * @param x
     * @return
     */
    public static float easeSpringTwice(float x) {
        return (float) (Math.pow(2, -2 * x) * Math.sin(2 * Math.PI * x) * 0.2);
    }

    /**
     * -5\left(x-0.2\right)
     * 2^{-10x}\cdot\sin\left((x-0.5)*(2\pi)/0.2\right)
     * 弹簧两次
     *
     * @param x
     * @param revers 1,-1
     *               s
     * @return
     */
    public static float easeSpringCommon15(float x, float fractor, float revers) {
        if (x < 0.2f) {
            return -10f * (x - 0.2f) * revers;
        }
        return (float) (Math.pow(2, -10 * x) * Math.sin(10 * Math.PI * (x - 0.5)) * fractor * revers);
    }

    /**
     * 配合{{@link #easeSpringCommon15(float, float, float)}} 小于0.2走直线趋势
     *
     * @param x
     * @param revers
     * @return
     */
    public static float easeSpringCommon15X(float x, float revers) {
        if (x < 0.2f) {
            return -5f * (x - 0.2f) * revers;
        }
        return 0;
    }

    /**
     * 2^{-6x}\cdot\sin\left(10(x)\right)\cdot0.5
     * 弹簧两次
     *
     * @param x
     * @param revers 1,-1
     *               s
     * @return
     */
    public static float easeSpringCommon15A(float x, float fractor, float revers) {
        return (float) (Math.pow(2, -6 * x) * Math.sin(10 * Math.PI * x) * fractor * revers);
    }

    /**
     * z轴微转
     *
     * @param x
     * @param fractor
     * @param revers
     * @return
     */
    public static float easeSpringCommon15B(float x, float fractor, float revers) {
        return (float) (Math.pow(2, -10 * x) * Math.sin(10 * Math.PI * x) * fractor * revers);
    }

    /**
     * 2^{-10x}\cdot\sin\left(10*(x-0.5)*(2\pi)/0.2\right)
     *
     * @param x
     * @param startConor
     * @return
     */
    public static float easeSpringCommon15Conor(float x, float startConor) {
        if (x < 0.2f) {
            return startConor;
        }
        return (float) (Math.pow(2, -10f * x) * Math.sin(10f * Math.PI * (x - 0.5f)) * -10f);
    }

    /**
     * 2^{-4x}\cdot\sin\left(5\left(x\cdot\pi\right)\right)
     * <p>
     * <p>
     * y=-4\left(x-0.2\right)
     *
     * @param x
     * @param startConor
     * @return
     */
    public static float easeSpringchristmas5(float x, float startConor) {
        if (x < 0.2f) {
            return -4f * (x - 0.2f) * startConor;
        }
        return (float) (Math.pow(2, -10f * x) * Math.sin(5f * Math.PI * x)) * startConor;
    }

    public static float easeSpringFood0(float x, float startConor) {
        return -1f * (x - 1f) * startConor;
    }


    /**
     * 延迟
     *
     * @param x
     * @return
     */
    public static float customWave(float x) {
        if (x < 0.3) {
            return 0;
        }
        return x;
    }

    /**
     * 慢快慢
     */
    public static float easeInOutSine(float x) {
        return (float) (-(cos(PI * x) - 1) / 2);
    }

    public static double shake7Times(double x) {
        return (x - 1d) * Math.cos(20d * (x - 1d));
    }

    public static double shake3Times(double x) {
        return 0.2 * Math.PI * (x - 1d) * Math.cos(4 * Math.PI * (x - 1d));
    }


    /**
     * https://easings.net/cn#easeInQuad
     */
    public static double easeInQuad(double x) {
        return x * x;
    }


    /**
     * https://easings.net/cn#easeOutQuad
     */
    public static double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }


    /**
     * https://easings.net/cn#easeOutBounce
     */
    public static double easeOutBounce(double x) {
        final double n1 = 7.5625;
        final double d1 = 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            return n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            return n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
    }


    /**
     * https://easings.net/cn#easeInBounce
     */
    public static double easeInBounce(double x) {
        return 1 - easeOutBounce(1 - x);
    }


    /**
     * https://easings.net/cn#easeInElastic
     */
    public static double easeInElastic(double x) {
        final double c4 = (2 * Math.PI) / 3;

        return x == 0
                ? 0
                : x == 1
                ? 1
                : -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * c4);
    }


    /**
     * https://easings.net/cn#easeOutElastic
     */
    public static double easeOutElastic(double x) {
        final double c4 = (2 * Math.PI) / 3;

        return x == 0
                ? 0
                : x == 1
                ? 1
                : pow(2, -10 * x) * sin((x * 10 - 0.75) * c4) + 1;
    }

}
