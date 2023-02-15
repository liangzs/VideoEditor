package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26;

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26.Birthday26Action0;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26.Birthday26Action1;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26.Birthday26Action2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26.Birthday26Action3;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26.Birthday26Action4;

import java.util.Arrays;
import java.util.List;

/**
 * @author hayring
 * @date 2021/12/27  19:56
 */
public class Birthday26ThemeManager extends ThemeOpenglManager.ReflectOpenglThemeManager {

    private static final List<Class<? extends ImageOriginFilter>> ACTION_CLASSES = Arrays.asList(Birthday26Action0.class,
            Birthday26Action1.class, Birthday26Action2.class, Birthday26Action3.class, Birthday26Action4.class);

    @Override
    public List<Class<? extends ImageOriginFilter>> getFinalActionClasses() {
        return ACTION_CLASSES;
    }
}
