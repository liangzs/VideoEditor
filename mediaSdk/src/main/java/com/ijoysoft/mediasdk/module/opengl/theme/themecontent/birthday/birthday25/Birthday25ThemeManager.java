package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25.Birthday25Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25.Birthday25Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25.Birthday25Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25.Birthday25Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25.Birthday25Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday25ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday25Action0.class,
            Birthday25Action1.class, Birthday25Action2.class, Birthday25Action3.class, Birthday25Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
