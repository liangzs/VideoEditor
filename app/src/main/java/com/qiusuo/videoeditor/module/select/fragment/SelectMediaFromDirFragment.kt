package com.qiusuo.videoeditor.module.select.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.entity.MediaType
import com.ijoysoft.videoeditor.adapter.GridMediaAdapter
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.bean.MediaEntity
import com.qiusuo.videoeditor.common.constant.RequestCode
import com.qiusuo.videoeditor.common.data.MediaDataRepository
import com.qiusuo.videoeditor.databinding.SelectMediaFromDirLayoutBinding
import com.qiusuo.videoeditor.module.select.LoadMediaViewModel
import com.qiusuo.videoeditor.module.select.SelectClipActivity
import com.qiusuo.videoeditor.module.select.adapter.MediaAdapterProxy
import com.qiusuo.videoeditor.module.select.adapter.MediaRecyclerAdapter
import com.qiusuo.videoeditor.ui.decoration.GridLayoutManagerNoAnimate
import com.qiusuo.videoeditor.ui.widgegt.guide.NewbieGuide
import com.qiusuo.videoeditor.ui.widgegt.guide.core.Controller
import com.qiusuo.videoeditor.ui.widgegt.guide.model.GuidePage
import com.qiusuo.videoeditor.ui.widgegt.guide.model.HighLight
import com.qiusuo.videoeditor.ui.widgegt.guide.model.HighlightOptions
import com.qiusuo.videoeditor.ui.widgegt.guide.model.RelativeGuide
import com.qiusuo.videoeditor.ui.widgegt.selection.HeaderViewHolder
import com.qiusuo.videoeditor.ui.widgegt.selection.SectionedSpanSizeLookup
import com.qiusuo.videoeditor.util.SpUtil

/**
 * Created by DELL on 2019/5/6.
 */
class SelectMediaFromDirFragment : BaseFragment<SelectMediaFromDirLayoutBinding>(SelectMediaFromDirLayoutBinding::inflate), SelectFragment {

    //private var map: Map<String, List<MediaEntity>>? = null
    //private var dates: List<String>? = null
    private var dateGridLayoutManager: GridLayoutManager? = null
    private var normalGridLayoutManager: GridLayoutManager? = null
    private var currentLayoutManager: GridLayoutManager? = null
    private var gridAdapter: GridMediaAdapter? = null
    private var mMediaRecyclerAdapter: MediaRecyclerAdapter? = null
    private var adapter: RecyclerView.Adapter<*>? = null

    /**
     * 适配器代理类
     */
    private var proxy: MediaAdapterProxy? = null

    //贴图选择页面
    private var stickerSelect = false
    private var isDestroy = false
    lateinit var viewModel: LoadMediaViewModel;
    override fun initView() {
        viewModel = ViewModelProvider(activity).get(LoadMediaViewModel::class.java)
        dateGridLayoutManager = GridLayoutManagerNoAnimate(context, 4)
        currentLayoutManager = dateGridLayoutManager
        normalGridLayoutManager = GridLayoutManagerNoAnimate(context, 4)
        proxy = MediaAdapterProxy(context, viewModel.getCurrentDateTitle(LoadMediaViewModel.MIX),
            viewModel.getCurrentDateMap(LoadMediaViewModel.MIX), viewModel.getCurrentGridList(LoadMediaViewModel.MIX))
        if (!viewModel.getCurrentDateTitle(LoadMediaViewModel.MIX).isEmpty()) {
            activity.endLoading()
        }
        if (activity is SelectClipActivity) {
            proxy!!.isAddClip = (activity as SelectClipActivity?)!!.isAddClip
        }
        gridAdapter = GridMediaAdapter(proxy!!)
        mMediaRecyclerAdapter = MediaRecyclerAdapter(proxy)
        adapter = mMediaRecyclerAdapter
        if (SpUtil.getInt(SpUtil.MEDIA_SHOW_TYPE, 1) == 1) {
            adapter = gridAdapter
            currentLayoutManager = normalGridLayoutManager
        }
        val lookup = SectionedSpanSizeLookup(mMediaRecyclerAdapter, dateGridLayoutManager)
        dateGridLayoutManager!!.setSpanSizeLookup(lookup)
        viewBinding!!.mediaRecycler.layoutManager = currentLayoutManager
        //viewBinding!!.mediaRecycler.setEmptyView(viewBinding!!.noDataTip)
        mMediaRecyclerAdapter!!.setOnItemClickListener(object : MediaAdapterProxy.onItemClickListener {
            override fun onItemSelected(view: View, mediaItem: MediaEntity) {
                redirect2Crop(view, mediaItem)
            }

            override fun onItemClick(view: View, mediaItem: MediaEntity, position: Int) {
                if (stickerSelect) {
                    redirect2Crop(view, mediaItem)
                    return
                }
                val childCoordinate = IntArray(2)
                view.getLocationInWindow(childCoordinate)
                //if (!mediaItem.isImage) {
                //    MediaDataRepository.getInstance().tempMediaEntity = mediaItem
                //}
                //AppBus.get().post(PreviewEvent(mediaItem, childCoordinate, position))
            }

            override fun selectAll(selectDate: String, toSelect: Boolean) {
                //AppBus.get().post(PhotoSelectAll(selectDate, toSelect))
            }
        })
        viewBinding!!.mediaRecycler.layoutManager = currentLayoutManager
        (viewBinding!!.mediaRecycler.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        viewBinding!!.mediaRecycler.adapter = adapter
        isDestroy = false
        mMediaRecyclerAdapter!!.showSelectAll(showSelect)
        viewBinding!!.mediaRecycler.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (viewBinding!!.mediaRecycler.adapter!!.itemCount > 0) {
                    val view = viewBinding!!.mediaRecycler.getChildAt(0)
                    if (view != null) {
                        if (view.parent != null) {
                            initGuideView()
                            viewBinding!!.mediaRecycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
            }
        })
        viewModel.allLiveData.observe(this) {
            if (viewBinding!!.mediaRecycler.scrollState == RecyclerView.SCROLL_STATE_IDLE && !viewBinding!!.mediaRecycler.isComputingLayout) {
                val refresh = proxy!!.setData(it.first, it.second, it.third)
                if (refresh) {
                    adapter!!.notifyDataSetChanged()
                }
                if (!ObjectUtils.isEmpty(it.first)) {
                    viewBinding.noDataTip.visibility = View.GONE
                } else {
                    viewBinding.noDataTip.visibility = View.VISIBLE
                }
                activity.endLoading()
            }
        }
        //recycleview的滑动底部监听
        viewBinding.mediaRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isBottom = recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
                if (isBottom && !activity.loadShowing() && !(viewModel as LoadMediaViewModel).isPageMax()
                    && (viewModel as LoadMediaViewModel).folderType == LoadMediaViewModel.ALL && dy > 0
                ) {
                    (viewModel as LoadMediaViewModel).loadMoreMedia(LoadMediaViewModel.MIX)
                    activity.showLoading("")
                }
            }
        })
    }

    private fun redirect2Crop(view: View, mediaItem: MediaEntity) {
        val childCoordinate = IntArray(2)
        view.getLocationInWindow(childCoordinate)
        val intent = Intent()
        intent.putExtra("mediaitem", mediaItem)
        intent.putExtra("locate", childCoordinate)
        intent.action = SelectClipActivity.ACTION_SELECT
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent)
    }

    fun setMediaRecyclerFrozen(frozen: Boolean) {
        viewBinding!!.mediaRecycler.isLayoutFrozen = frozen
    }

    override fun updateData() {
        if (adapter == null) {
            return
        }
        if (viewBinding!!.mediaRecycler.scrollState == RecyclerView.SCROLL_STATE_IDLE && !viewBinding!!.mediaRecycler.isComputingLayout) {
            val dates = (viewModel as LoadMediaViewModel).getCurrentDateTitle(LoadMediaViewModel.MIX)
            val refresh = proxy!!.setData(
                dates,
                (viewModel as LoadMediaViewModel).getCurrentDateMap(LoadMediaViewModel.MIX),
                (viewModel as LoadMediaViewModel).getCurrentGridList(LoadMediaViewModel.MIX))
            adapter!!.notifyDataSetChanged()
            if (!ObjectUtils.isEmpty(dates)) {
                viewBinding!!.noDataTip.visibility = View.GONE
            } else {
                viewBinding!!.noDataTip.visibility = View.VISIBLE
            }

        }
    }

    /**
     * 更新媒体元素右上角选中按钮的位置
     *
     * @param mediaId   素材唯一标识
     * @param mediaType 素材类型
     */
    fun updateSelectedItemIndex(mediaId: Int, mediaType: MediaType) {
        if (currentLayoutManager == null) {
            return
        }
        for (i in 0 until currentLayoutManager!!.childCount) {
            val view = currentLayoutManager!!.getChildAt(i)
            val holder: Any = viewBinding!!.mediaRecycler.getChildViewHolder(view!!)
            if (holder is MediaAdapterProxy.MyViewHolder) {
                val myHolder = holder
                if (myHolder.id == mediaId && myHolder.mediaType == mediaType) {
                    val adapterPosition = myHolder.adapterPosition
                    requireActivity().runOnUiThread {
                        if (viewBinding!!.mediaRecycler.scrollState == RecyclerView.SCROLL_STATE_IDLE && !viewBinding!!.mediaRecycler.isComputingLayout) {
                            adapter!!.notifyItemChanged(adapterPosition)
                        }
                    }
                    return
                }
            } else {
                val headerViewHolder = holder as HeaderViewHolder
                mMediaRecyclerAdapter!!.updateSelecting(headerViewHolder)
            }
        }
    }

    /**
     * 更新媒体元素右上角选中按钮的位置
     */
    override fun updateCopiedSelectedItemIndex(path: String) {
        if (currentLayoutManager == null) {
            return
        }
        for (i in 0 until currentLayoutManager!!.childCount) {
            val view = currentLayoutManager!!.getChildAt(i)
            val holder: Any = viewBinding!!.mediaRecycler.getChildViewHolder(view!!)
            if (holder is MediaAdapterProxy.MyViewHolder) {
                val myHolder = holder
                if (myHolder.mediaEntity.getPath() == path) {
                    val adapterPosition = myHolder.adapterPosition
                    requireActivity().runOnUiThread { adapter!!.notifyItemChanged(adapterPosition, RequestCode.SELECT) }
                    //                    LogUtils.d(getClass().getSimpleName() + "#updateSelectedItemIndex_step1", "id:" + mediaId);
                    return
                }
            } else {
                val headerViewHolder = holder as HeaderViewHolder
                mMediaRecyclerAdapter!!.updateSelecting(headerViewHolder)
            }
        }
    }

    fun setStickerSelect(stickerSelect: Boolean) {
        this.stickerSelect = stickerSelect
    }

    var controller: Controller? = null
    private fun initGuideView() {
        if (isDestroy) {
            return
        }
        if (viewBinding!!.mediaRecycler.getChildAt(0) != null) {
            //检测是否需要显示
            controller = NewbieGuide.with(this).setLabel("guide1").controller
            if (!controller!!.shouldShow()) {
                LogUtils.i(javaClass.simpleName, "NewbieGuide shoul not show.")
                return
            }
            val builder = NewbieGuide.with(this)
            val highltBuilder = HighlightOptions.Builder()
            highltBuilder.setOnClickListener { v: View? ->
                if (controller != null) {
                    controller!!.remove()
                }
            }
            highltBuilder.setRelativeGuide(RelativeGuide(R.layout.guide_view_layout, Gravity.BOTTOM, -50))
            builder.setLabel("guide1")
                .addGuidePage(GuidePage.newInstance()
                    .addHighLightWithOptions(viewBinding!!.mediaRecycler.getChildAt(0), HighLight.Shape.RECTANGLE, highltBuilder.build())
                    .setBackgroundColor(resources.getColor(R.color.black_80_color))
                    .setEverywhereCancelable(true))
            if (!isDestroy) {
                controller = builder.show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        isDestroy = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isDestroy = true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 切换布局方式
     */
    override fun switchAdapter(position: Int) {
        if (adapter === gridAdapter) {
            if (position == 1) {
                return
            }
            adapter = mMediaRecyclerAdapter
            currentLayoutManager = dateGridLayoutManager
        } else {
            if (position == 0) {
                return
            }
            adapter = gridAdapter
            currentLayoutManager = normalGridLayoutManager
        }
        viewBinding!!.mediaRecycler.adapter = adapter
        viewBinding!!.mediaRecycler.layoutManager = currentLayoutManager
    }

    private var showSelect = false

    /**
     * 显示全选按钮
     */
    override fun showSelect() {
        showSelect = true
    }

    override fun updateSelect() {
        if (adapter != null) {
            adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount, RequestCode.SELECT)
        }
    }

    override fun updateDateCount(date: String, mediaType: Int, increase: Boolean) {
        var count = proxy!!.selectCount[date]
        if (count == null) {
            count = 0
        }
        if (increase) {
            proxy!!.selectCount[date] = count + 1
            LogUtils.v("selectCount All", date + "count:" + (count + 1))
        } else {
            proxy!!.selectCount[date] = count - 1
            LogUtils.v("selectCount All", date + "count:" + (count - 1))
        }
    }

    /**
     * 清除选择计数
     */
    override fun clearSelectCount() {
        for (date in proxy!!.selectCount.keys) {
            LogUtils.v("selectCount All", date + "count:0")
        }
    }

    /**
     * 更新全选按钮
     */
    override fun updateSections() {
        mMediaRecyclerAdapter!!.notifyItemRangeChanged(0, mMediaRecyclerAdapter!!.itemCount, RequestCode.SECTIONS)
    }

    override fun recountSections() {
        if (proxy != null) {
            proxy!!.selectCount.clear()
            LogUtils.v("selectCount All", "clear")
            mMediaRecyclerAdapter!!.notifyItemRangeChanged(0, mMediaRecyclerAdapter!!.itemCount, RequestCode.SECTIONS)
        }
    }

    companion object {
        private const val PARAM = "param"
        private const val PARAM1 = "param1"

        /**
         * 媒体模式标签
         */
        private const val MODE_TAG = "mediaMode"

        @JvmStatic
        fun newInstance(id: Int): SelectMediaFromDirFragment {
            val fragment = SelectMediaFromDirFragment()
            val bundle = Bundle()
            bundle.putInt(PARAM, id)
            fragment.arguments = bundle
            return fragment
        }
    }
}