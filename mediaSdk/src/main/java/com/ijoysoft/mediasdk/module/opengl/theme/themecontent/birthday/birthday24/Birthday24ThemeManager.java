package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24.Birthday24Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24.Birthday24Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24.Birthday24Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24.Birthday24Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24.Birthday24Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday24ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday24Action0.class,
            Birthday24Action1.class, Birthday24Action2.class, Birthday24Action3.class, Birthday24Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
