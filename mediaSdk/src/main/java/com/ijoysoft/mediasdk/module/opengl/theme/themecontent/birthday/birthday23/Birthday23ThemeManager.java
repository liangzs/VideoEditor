package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23.Birthday23Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23.Birthday23Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23.Birthday23Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23.Birthday23Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23.Birthday23Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday23ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday23Action0.class,
            Birthday23Action1.class, Birthday23Action2.class, Birthday23Action3.class, Birthday23Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
