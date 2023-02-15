package com.ijoysoft.mediasdk.module.opengl.theme

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.action.*
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample.*

/**
 * 补充三元表达式
 */
fun <T> Boolean?.ternary(positiveValue: T, negativeValue: T) =
    if (this != null && this) positiveValue else negativeValue

fun <T> Boolean?.ternary(positiveValueProvider: () -> T, negativeValueProvider: () -> T): T =
    if (this != null && this) positiveValueProvider() else negativeValueProvider()

/**
 *控件挂载在主题管理类中
 */
abstract class ThemeManagerWithwidget : ThemeOpenglManager(), IThemeClipAction {
    val ENTER = DEFAULT_ENTER_TIME;
    val STAY = DEFAULT_STAY_TIME;
    val OUT = DEFAULT_OUT_TIME;

    /**
     * 全局控件，包括背景，边框等
     */
    protected var globalWidget: MutableList<ImageOriginFilter>? = null

    /**
     * 背景图
     */
    var bgImageFilter: ImageOriginFilter? = null

    var realIndex = 0;

    /**
     * 创建片段
     */
    abstract fun createClip(): Array<Triple<Int, Int, Int>>;

    /**
     * 创建片段动作
     */
    abstract fun createClipAction(): List<Array<BaseEvaluate?>?>;

    /**
     * 创建片段位置
     */
    abstract fun createClipLocation(): Array<FloatArray>;

    /**
     * 创建片段挂载的控件
     */
    abstract fun createWidget(): Array<Array<Triple<Int, Int, Int>>>;

    /**
     * 创建控件动画
     */
    abstract fun createWidgetAction(): List<Array<Array<BaseEvaluate?>?>?>;

    /**
     * 控件位置
     */
    abstract fun createWidgetLocation(index: Int, widgetIndex: Int, widget: BaseThemeExample, bitW: Int, bitH: Int);

    /**
     * 主题循环周期
     */
    abstract fun createPreiod(): Int

    abstract fun blurExample(): Boolean

    /**
     * 返回ThemeContainWidgetBlur
     */
    override fun drawPrepareIndex(mediaItem: MediaItem?, index: Int, width: Int, height: Int): IAction {
        return if (blurExample()) {
            ThemeContainWidgetBlur()
        } else {
            ThemeContainWidget()
        }
    }

    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem?, index: Int) {
        super.drawPrepare(mediaDrawer, mediaItem, index)
        realIndex = index % createPreiod();
        creatClip(mediaItem!!.duration, createClip()[realIndex])
        createWidget().let {
            if (it.size > 0) {
                createWidgetImpl(it[realIndex], mediaItem.mimapBitmaps)
            }
        };
        createClipAction().let {
            if (it.size > 0) {
                clipAction(it[realIndex])
            }
        }
        createWidgetAction().let {
            if (it.size > 0) {
                widgetAction(it[realIndex])
            }
        }

        val widgets = (actionRender as IThemeClipAction).getWidgetList();
        for (i in widgets.indices) {
            widgets[i].apply {
                createWidgetLocation(realIndex, i, this, mediaItem.mimapBitmaps.get(i).width, mediaItem.mimapBitmaps.get(i).height)
            }
        }
        if (globalWidget == null && existGlobalWidget()) {
            createGlobalWidget(mediaItem.mimapBitmaps)
        }
    }

    /**
     * 是否存在去全局性控件
     */
    open fun existGlobalWidget(): Boolean {
        return false;
    }

    abstract fun createGlobalWidget(mimaps: List<Bitmap>)


    /**
     * 创建主体
     */
    override fun creatClip(duration: Long, durationTriple: Triple<Int, Int, Int>) {
        (actionRender as IThemeClipAction).creatClip(duration, durationTriple);
    }


    override fun createWidget(array: Array<Triple<Int, Int, Int>>, mimaps: List<Bitmap>, width: Int, height: Int) {
    }

    /**
     * 创建控件对象
     */
    fun createWidgetImpl(array: Array<Triple<Int, Int, Int>>, mimaps: List<Bitmap>) {
        (actionRender as IThemeClipAction).createWidget(array, mimaps, width, height);
    }

    /**
     * 设置主题动作
     */
    override fun clipAction(actions: Array<BaseEvaluate?>?) {
        (actionRender as IThemeClipAction).clipAction(actions);
    }

    /**
     * 设置控件动作
     */
    override fun widgetAction(array: Array<Array<BaseEvaluate?>?>?) {
        (actionRender as IThemeClipAction).widgetAction(array);
    }

    open fun isforGround(): Boolean {
        return false;
    }


    override fun onDrawFrame() {
        if (!isforGround()) {
            bgImageFilter?.draw()
        }
        super.onDrawFrame()
        globalWidget?.let {
            for (i in it) {
                i.draw()
            }
        }
        if (isforGround()) {
            bgImageFilter?.draw()
        }
    }

    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        bgImageFilter?.create()
    }

    override fun onSurfaceChanged(offsetX: Int, offsetY: Int, width: Int, height: Int, screenWidth: Int, screenHeight: Int) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight)
        globalWidget?.let {
            for (i in it) {
                i.onDestroy()
            }
        }
        globalWidget = null
        bgImageFilter?.onSizeChanged(width, height)
    }

    override fun onDestroy() {
        super.onDestroy()
        globalWidget?.let {
            for (i in it) {
                i.onDestroy()
            }
        }
        bgImageFilter?.onDestroy()
    }

}