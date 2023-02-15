package com.ijoysoft.mediasdk.module.opengl.transition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class TransitionFactory {

    public static TransitionFilter initFilters(TransitionType type) {
        if (type == null) {
            return null;
        }
        try {
            Constructor<? extends TransitionFilter> constructor = type.transitionClass.getConstructor(TransitionType.class);
            return constructor.newInstance(type);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 随机转场组，原来基础转场作为一组，然后
     */
    private void randomTransitionGroup() {


    }


}
