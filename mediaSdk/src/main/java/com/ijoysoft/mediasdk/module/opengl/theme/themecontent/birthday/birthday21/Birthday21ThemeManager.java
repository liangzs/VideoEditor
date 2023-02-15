package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21.Birthday21Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21.Birthday21Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21.Birthday21Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21.Birthday21Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21.Birthday21Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday21ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday21Action0.class,
            Birthday21Action1.class, Birthday21Action2.class, Birthday21Action3.class, Birthday21Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
