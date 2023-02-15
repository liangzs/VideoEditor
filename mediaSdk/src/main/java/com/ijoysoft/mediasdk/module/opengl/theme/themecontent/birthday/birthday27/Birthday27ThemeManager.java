package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27.Birthday27Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27.Birthday27Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27.Birthday27Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27.Birthday27Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27.Birthday27Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday27ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday27Action0.class,
            Birthday27Action1.class, Birthday27Action2.class, Birthday27Action3.class, Birthday27Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
