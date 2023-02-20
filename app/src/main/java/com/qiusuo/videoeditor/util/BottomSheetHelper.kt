package com.qiusuo.videoeditor.util

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.qiusuo.videoeditor.R

/**
 *
 *
 * @date 2022/7/6  19:29
 * @author hayring
 *
 * 需要在布局中的dragview 配置：
 * android:layout_height="0dp"
 * app:behavior_peekHeight="@dimen/dp_180"
 * app:layout_behavior="@string/bottom_sheet_behavior"
 */
class BottomSheetHelper<T:ViewGroup>(
    /**
     * 内部需要调整大小的RecyclerView
     */
    val recyclerView: RecyclerView,

    /**
     * 可滑动的视图
     */
    val dragView: T,

    /**
     * 收缩后recyclerView的高度
     */
    val recyclerCollapsedHeight: Int,

    /**
     * 展开后相对父布局占比
     */
    val bottomSheetExpandWeight: Float = 0.7f,

    /**
     * 滑动行为
     */
    var listener: SlidingUpListener? = null

) {


    companion object {

        /**
         * 填充父布局
         */
        const val MODE_MATCH = 0

        /**
         * 不做处理
         */
        const val MODE_IGNORE = 1

        /**
         * 权重
         */
        const val MODE_WEIGHT = 2
    }


    /**
     * 调整RecyclerView的方式
     */
    var fitRecyclerMode = MODE_MATCH

    /**
     * 联动行为控制类
     */
    val behavior: BottomSheetBehavior<T> = BottomSheetBehavior.from(dragView)

    var imageDirect: ImageView? = dragView.findViewById<ImageView?>(R.id.image_direct)?.also {
        it.setOnClickListener {

            val behavior: BottomSheetBehavior<T> = BottomSheetBehavior.from<T>(dragView)
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                //如果是展开状态，则关闭，反之亦然
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            } else {
                fixRecyclerView(true)
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    val callback = object : BottomSheetCallback() {
        /**
         * 上次的状态
         */
        var previousState = BottomSheetBehavior.STATE_COLLAPSED
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        /**
         * 展开时显示marchparent，收起时只显示对应高度
         * @param bottomSheet
         * @param newState
         */
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (previousState == BottomSheetBehavior.STATE_EXPANDED
                && newState == BottomSheetBehavior.STATE_DRAGGING
            ) { // 即将关闭
                imageDirect?.setImageResource(R.drawable.vm_vector_up)
                listener?.onDraggingIn()
            } else if (previousState == BottomSheetBehavior.STATE_COLLAPSED
                && newState == BottomSheetBehavior.STATE_DRAGGING
            ) { // 即将打开
//                    mImageDirect.setImageResource(R.drawable.vm_vector_down);
                fixRecyclerView(true)
                listener?.onDraggingOut()
            }
            if (newState == BottomSheetBehavior.STATE_EXPANDED) { // 开
                imageDirect?.setImageResource(R.drawable.vm_vector_down)
                fixRecyclerView(true)
                listener?.onExpanded()
            } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) { // 收
                imageDirect?.setImageResource(R.drawable.vm_vector_up)
                //设置收缩高度
                fixRecyclerView(false)
                listener?.onCollapsed()
            }
            previousState = newState
        }
    }

    /**
     *
     */
    fun fixRecyclerView(expand: Boolean) {
        if (expand) {
            when (fitRecyclerMode) {
                MODE_MATCH -> {
                    //设置默认高度
                    val lp: ViewGroup.LayoutParams = recyclerView.layoutParams
                    if (lp.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                        recyclerView.layoutParams = lp
                    }
                }
                MODE_WEIGHT -> {
                    if (recyclerView.parent is ConstraintLayout) {
                        val lp = recyclerView.layoutParams as ConstraintLayout.LayoutParams
                        lp.verticalWeight = 1F
                        lp.height = 0
                        recyclerView.layoutParams = lp
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(recyclerView.parent as ConstraintLayout)
                        constraintSet.connect(recyclerView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                        constraintSet.applyTo(recyclerView.parent as ConstraintLayout)
                    } else if (recyclerView.parent is LinearLayout) {
                        val lp = recyclerView.layoutParams as LinearLayout.LayoutParams
                        lp.weight = 1f
                        lp.height = 0
                        recyclerView.layoutParams = lp
                    }
                }
            }
        } else {
            when (fitRecyclerMode) {
                MODE_MATCH -> {
                    //设置默认高度
                    val lp: ViewGroup.LayoutParams = recyclerView.layoutParams
                    if (lp.height != recyclerCollapsedHeight) {
                        lp.height = recyclerCollapsedHeight
                        recyclerView.layoutParams = lp
                    }
                }
                MODE_WEIGHT -> {
                    if (recyclerView.parent is ConstraintLayout) {
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(recyclerView.parent as ConstraintLayout)
                        constraintSet.clear(recyclerView.id, ConstraintSet.BOTTOM)
                        constraintSet.applyTo(recyclerView.parent as ConstraintLayout)
                        val lp = recyclerView.layoutParams as ConstraintLayout.LayoutParams
                        lp.verticalWeight = 0f
                        lp.height = recyclerCollapsedHeight
                        recyclerView.layoutParams = lp

                    } else if (recyclerView.parent is LinearLayout) {
                        val lp = recyclerView.layoutParams as LinearLayout.LayoutParams
                        lp.weight = 0f
                        lp.height = recyclerCollapsedHeight
                        recyclerView.layoutParams = lp
                    }
                }
            }
        }
    }


    init {
        //滑动开始时，Recyclerview会收到ACTION_DOWN,
        // 滑动状态会改变，这时去关闭dragview的滑动，之后就不会再收到ACTION_CANCEL
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val behavior = BottomSheetBehavior.from<T>(dragView)
                behavior.isDraggable = newState != ViewPager.SCROLL_STATE_DRAGGING
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        behavior.addBottomSheetCallback(callback)

        //消费事件，不传到下层
        //TODO: 看看有没有什么更好的解决办法
        dragView.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            true
        }

    }


    /**
     * 设置高度，用于post
     * 在onCreate过程中post
     * @return true 高度设置成功
     * @return false 高度设置失败， 父布局高度获取失败
     */
    fun setExpandHeight(): Boolean {
        val lp: ViewGroup.LayoutParams = dragView.layoutParams
        val parentHeight = (dragView.parent as ViewGroup).height
        if (parentHeight == 0) return false
        lp.height = (parentHeight * bottomSheetExpandWeight).toInt()
        dragView.layoutParams = lp
        return true
    }

}

/**
 * 滑动行为监听
 */
interface SlidingUpListener {

    /**
     * 回收完成
     */
    fun onExpanded()

    /**
     * 正在展开
     */
    fun onDraggingOut()

    /**
     * 正在回收
     */
    fun onDraggingIn()

    /**
     * 回收完成
     */
    fun onCollapsed()

}