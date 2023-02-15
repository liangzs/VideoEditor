package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting6;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Greeting6ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Greeting6Action0.class,
            Greeting6Action1.class, Greeting6Action2.class, Greeting6Action3.class, Greeting6Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
