package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude7;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Attitude7ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Attitude7Action0.class,
            Attitude7Action1.class, Attitude7Action2.class, Attitude7Action3.class, Attitude7Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
