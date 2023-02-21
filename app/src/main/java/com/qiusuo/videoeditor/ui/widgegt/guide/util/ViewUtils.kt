package com.qiusuo.videoeditor.ui.widgegt.guide.util

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlin.math.exp


/**
 *
 *
 * @date 2022/3/11  10:35
 * @author hayring
 */
object ViewUtils {

    /**
     * 省略号
     */
    private const val ELLIPSIS = "..."

    /**
     * 设置标题, 在某一行处截取,加上...
     * @param title 标题文字
     * @param breakTextRowIndex 第几行开始截断, 第一行截取输入0
     * @param maxWidth 宽度上限
     */
    fun breakAndSetTextWithTextView(titleTextView: TextView, title: String, breakTextRowIndex: Int, maxWidth: Int) {
        if(maxWidth <= 0) {
            titleTextView.text = title
            return
        }
        //标题字符缓存
        var rowIndex = breakTextRowIndex
        val charBuffer = StringBuilder(title)
        //输出
        val result = StringBuilder()
        //此TextView的Paint，用于测量
        val textPaint: TextPaint = titleTextView.paint
        var count: Int
        //对非截取行进行处理，手动换行
        while (rowIndex > 0) {
            //计算本行字符个数
            count =
                textPaint.breakText(charBuffer, 0, charBuffer.length, true, maxWidth.toFloat(), null)
            if (count == charBuffer.length) {
                //所有字符用完且不需要截取，直接添加
                result.append(charBuffer)
                charBuffer.delete(0, charBuffer.length)
                break
            } else {
                //添加截取的文字
                result.append(charBuffer, 0, count)
                result.append('\n')
                //缓存删除
                charBuffer.delete(0, count)
            }
            rowIndex--
        }
        if (charBuffer.isNotEmpty()) {
            //还有剩余
            //计算最后一行是否需要截取
            count =
                textPaint.breakText(charBuffer, 0, charBuffer.length, true, maxWidth.toFloat(), null)
            if (count < charBuffer.length) {
                //需要截取，获取"..."的显示宽度
                val bounds = Rect()
                textPaint.getTextBounds(ELLIPSIS, 0, 3, bounds)
                //将删除超出字符，添加...
                count = textPaint.breakText(
                    charBuffer,
                    0,
                    charBuffer.length,
                    true,
                    //宽度限制删除"..."的宽度
                    (maxWidth - bounds.width()).toFloat(),
                    null
                )
                charBuffer.delete(count, charBuffer.length)
                charBuffer.append(ELLIPSIS)
            }
            //将剩余字符添加进结果
            result.append(charBuffer)
        }
        //设置显示字符
        titleTextView.text = result.toString()
    }


    private fun autoSplitText(tv: TextView): String {
        val rawText = tv.text.toString() //原始文本
        val tvPaint: Paint = tv.paint //paint，包含字体等信息
        val tvWidth = (tv.width - tv.paddingLeft - tv.paddingRight).toFloat() //控件可用宽度

        //将原始文本按行拆分
        val rawTextLines = rawText.replace("\r".toRegex(), "").split("\n".toRegex()).toTypedArray()
        val sbNewText = java.lang.StringBuilder()
        for (rawTextLine in rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine)
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                var lineWidth = 0f
                var cnt = 0
                while (cnt != rawTextLine.length) {
                    val ch = rawTextLine[cnt]
                    lineWidth += tvPaint.measureText(ch.toString())
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch)
                    } else {
                        sbNewText.append("\n")
                        lineWidth = 0f
                        --cnt
                    }
                    ++cnt
                }
            }
            sbNewText.append("\n")
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length - 1)
        }
        return sbNewText.toString()
    }

    public class OnTvGlobalLayoutListener(private val mText: TextView) : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            mText.viewTreeObserver.removeOnGlobalLayoutListener(this)
            val newText: String = autoSplitText(mText)
            if (!TextUtils.isEmpty(newText)) {
                mText.text = newText
            }
        }
    }

//    fun showEasyExitDialog(activity: Activity, dark: Boolean, action: Runnable) {
//        var layoutId = if (dark) {
//            R.layout.dialog_exit_layout_dark
//        } else {
//            R.layout.dialog_exit_layout
//        }
//        val view: View = activity.layoutInflater.inflate(layoutId, null)
//        val builder = AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog_Alert)
//        val ok = view.findViewById<TextView>(R.id.ok)
//        val cancel = view.findViewById<TextView>(R.id.cancel)
//        builder.setView(view)
//        val alertDialog = builder.create()
//        ok.setOnClickListener {
//            action.run()
//        }
//        cancel.setOnClickListener { alertDialog.dismiss() }
//        /* show之前调用，取消弹出状态栏 */
//        DialogUtil.cancelDialogfocusable(alertDialog)
//        alertDialog.show()
//        DialogUtil.fullScreenImmersive(alertDialog.window!!.decorView)
//        DialogUtil.recoverDialogFocusable(alertDialog)
//    }

    /**
     * ImageView高度等比例变化
     */
    @Deprecated("需要handler post")
    fun matchAll(context: Context, view: View) {

        //重设ImageView宽高
        val params = view.layoutParams

        //获取图片宽高
        val drawable: Drawable = if (view is ImageView) {
            view.drawable
        } else {
            view.background
        }


        val dWidth: Int = drawable.intrinsicWidth
        val dHeight: Int = drawable.intrinsicHeight


        //图片宽高比
        val dScale = dHeight / dWidth.toDouble()
        val ivScale = view.height / view.width.toDouble()
        if (exp(dScale - ivScale) > 0.01f) {
            //判断差距，一面一直执行


            params.height = (dScale * view.width).toInt()
            view.layoutParams = params
            //这样就获得了一个既适应屏幕有适应内部图片的ImageView，不用再纠结该给ImageView设定什么尺寸合适了
        }
    }

    /**
     * ImageView高度等比例变化
     * 使用了measure，可以直接执行，不用post
     * @return measureWidth, measureHeight
     */
    fun imageViewMatchWidth(context: Context, view: View, width: Int): Array<Int> {

        //重设ImageView宽高
        val params = view.layoutParams

        //获取图片宽高
        val drawable: Drawable = if (view is ImageView) {
            view.drawable ?: view.background
        } else {
            view.background
        }

        //宽度确定，高度不限制
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
        //测量
        view.measure(widthMeasureSpec, heightMeasureSpec)

        val dWidth: Int = drawable.intrinsicWidth
        val dHeight: Int = drawable.intrinsicHeight


        //图片宽高比
        val dScale = dHeight / dWidth.toDouble()
        val ivScale = view.measuredHeight / view.measuredWidth.toDouble()
        if (exp(dScale - ivScale) > 0.01f) {
            //判断差距，一面一直执行


            params.height = (dScale * view.measuredWidth).toInt()
            view.layoutParams = params
            //这样就获得了一个既适应屏幕有适应内部图片的ImageView，不用再纠结该给ImageView设定什么尺寸合适了
        }
        return arrayOf(view.measuredWidth, view.measuredHeight)
    }


    /**
     * 关闭tabLayout的长按事件
     * @thread main
     * @callby onCreate
     * @author hayring
     */
    fun disableTabLayoutToast(tabLayout: TabLayout) {

        for (i in 0 until tabLayout.getTabCount()) {
            val tab: TabLayout.Tab? = tabLayout.getTabAt(i)
            tab?.let {
                it.view.isLongClickable = false
                // 针对android 26及以上需要设置setTooltipText为null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 可以设置null也可以是""
                    it.view.tooltipText = null
                    // tab.view.setTooltipText("");
                }
            }
        }
    }

    /**
     * 滑动指定项目的left贴合parent的最左侧
     */
    fun smoothScrollRecyclerViewItemToStart(recyclerView: RecyclerView, layoutManager: LinearLayoutManager, position: Int) {
        //当前完全显示的第一个
        val first = layoutManager.findFirstCompletelyVisibleItemPosition()

        //如果不在第一个则滑动
        if (position < first) {
            //往左滑，则直接滑动
            recyclerView.smoothScrollToPosition(
                position
            )
        }
        recyclerView?.adapter?.let {
            if (first < position) {
                val last = layoutManager.findLastCompletelyVisibleItemPosition()
                //因为会滑动到可见的最后一个位置，所以继续往后滑几个
                val targetIndex = last - first + position
                //不要越界
                if (targetIndex < (recyclerView.adapter?.itemCount ?: 0)) {
                    val scroller = LeftLinearSmoothScroller(recyclerView.context)
                    scroller.targetPosition = position
                    layoutManager.startSmoothScroll(scroller)
//                        layoutManager.scrollToPositionWithOffset(targetIndex, 0)
                } else {
                    recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
                }
            }
        }
    }




    /**
     * 滑动指定项目的left贴合parent的最左侧
     */
    fun scrollRecyclerViewItemToStart(recyclerView: RecyclerView, layoutManager: LinearLayoutManager, position: Int) {
        //当前完全显示的第一个
        val first = layoutManager.findFirstCompletelyVisibleItemPosition()

        //如果不在第一个则滑动
        if (position < first) {
            //往左滑，则直接滑动
            recyclerView.scrollToPosition(
                position
            )
        }
        if (first < position) {
            val last = layoutManager.findLastCompletelyVisibleItemPosition()
            //因为会滑动到可见的最后一个位置，所以继续往后滑几个
            val targetIndex = last - first + position
            //不要越界
            if (targetIndex < recyclerView.adapter!!.itemCount) {
                recyclerView.scrollToPosition(targetIndex)
//                        layoutManager.scrollToPositionWithOffset(targetIndex, 0)
            } else {
                recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
            }
        }
    }

    private const val FRAGMENT_CON = "NoSaveStateFrameLayout"
    fun getLocationInView(parent: View?, child: View?): Rect? {
        require(!(child == null || parent == null)) { "parent and child can not be null ." }
        var decorView: View? = null
        val context = child.context
        if (context is Activity) {
            decorView = context.window.decorView
        }
        val result = Rect()
        val tmpRect = Rect()
        var tmp = child
        if (child === parent) {
            child.getHitRect(result)
            return result
        }
        while (tmp !== decorView && tmp !== parent) {
            tmp!!.getHitRect(tmpRect)
            if (!tmp.javaClass.equals(FRAGMENT_CON)) {
                result.left += tmpRect.left
                result.top += tmpRect.top
            }
            tmp = tmp.parent as View
            requireNotNull(tmp) { "the view is not showing in the window!" }

            //added by isanwenyu@163.com fix bug #21 the wrong rect user will received in ViewPager
            if (tmp.parent != null && tmp.parent is ViewPager) {
                tmp = tmp.parent as View
            }
        }
        result.right = result.left + child.measuredWidth
        result.bottom = result.top + child.measuredHeight
        return result
    }

}


/**
 * 目标item滑动到左边贴着屏幕边缘
 */
class LeftLinearSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference() = SNAP_TO_START
}