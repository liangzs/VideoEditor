package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan5;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Rakshabandhan5ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Rakshabandhan5Action0.class,
            Rakshabandhan5Action1.class, Rakshabandhan5Action2.class, Rakshabandhan5Action3.class, Rakshabandhan5Action4.class, Rakshabandhan5Action5.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
