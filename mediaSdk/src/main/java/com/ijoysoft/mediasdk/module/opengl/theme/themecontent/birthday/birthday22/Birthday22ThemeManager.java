package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22.Birthday22Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22.Birthday22Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22.Birthday22Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22.Birthday22Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22.Birthday22Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday22ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday22Action0.class,
            Birthday22Action1.class, Birthday22Action2.class, Birthday22Action3.class, Birthday22Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
