package com.ijoysoft.mediasdk.module.opengl.theme;

import android.os.Build;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

import java.lang.reflect.Constructor;

/**
 * TODO bitmap重复利用 性能优化
 * Action一样的ThemeManager
 * @author hayring
 * @date 2021/12/13  9:29
 */
public class SameActionOpenglThemeManager extends ThemeOpenglManager {



    /**
     * 是否拥有第四个参数index
     */
    Boolean hasIndexParameter = null;


    Constructor<? extends ImageOriginFilter> constructor;

    public SameActionOpenglThemeManager(Constructor<? extends ImageOriginFilter> constructor) {
        this.constructor = constructor;
    }

    public SameActionOpenglThemeManager(Class<? extends ImageOriginFilter> clazz) {
        //大于1说明有含有
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                //使用参数数量来判断是构造方法参数中是否含有index
                if (clazz.getConstructors()[0].getParameterCount() > 3) {
                    constructor = clazz.getConstructor(int.class, int.class, int.class, int.class);
                    hasIndexParameter = true;
                } else {
                    constructor = clazz.getConstructor(int.class, int.class, int.class);
                    hasIndexParameter = false;
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Constructor not found! Create SameActionOpenglThemeManager failed, class name = " + clazz.getSimpleName(), e);
            }
        } else {
            //尝试获取4参数构造方法，如果失败则使用3参数方法，以此判断构造函数参数中是否含有index
            try {
                constructor = clazz.getConstructor(int.class, int.class, int.class, int.class);
                hasIndexParameter = true;
            } catch (NoSuchMethodException e) {
                try {
                    constructor = clazz.getConstructor(int.class, int.class, int.class);
                    hasIndexParameter = false;
                } catch (NoSuchMethodException noSuchMethodException) {
                    throw new RuntimeException("Constructor not found! Create SameActionOpenglThemeManager failed, class name = " + clazz.getSimpleName(), noSuchMethodException);
                }
            }
        }
    }

    /**
     * 设置步骤处理类
     *
     * @return IAction action实例
     */
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        try {
            IAction iAction;
            if (hasIndexParameter == null) {
                //大于1说明有含有
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //使用参数数量来判断是构造方法参数中是否含有index
                    if (constructor.getParameterCount() > 3) {
                        iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height, index);
                        hasIndexParameter = true;
                    } else {
                        iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height);
                        hasIndexParameter = false;
                    }
                } else {
                    //尝试获取4参数构造方法，如果失败则使用3参数方法，以此判断构造函数参数中是否含有index
                    try {
                        iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height, index);
                        hasIndexParameter = true;
                    } catch (IllegalArgumentException e) {
                        iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height);
                        hasIndexParameter = false;
                    }
                }
            } else if (hasIndexParameter) {
                iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height, index);
            } else {
                iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height);
            }

            //频繁打印的日志放至-verbose级别中
            LogUtils.v(getClass().getSimpleName(), "create instance reflecting successfully: " + constructor.getName());
            return iAction;
        } catch (Exception e) {
            LogUtils.e(getClass().getSimpleName(), "create instance reflecting failed: " + constructor.getName());
            throw new RuntimeException(e);
        }
    }


}
