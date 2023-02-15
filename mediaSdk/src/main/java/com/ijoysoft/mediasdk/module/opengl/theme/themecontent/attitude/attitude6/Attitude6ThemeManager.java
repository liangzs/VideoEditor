package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude6;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Attitude6ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Attitude6Action0.class,
            Attitude6Action1.class, Attitude6Action2.class, Attitude6Action3.class, Attitude6Action4.class, Attitude6Action5.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
