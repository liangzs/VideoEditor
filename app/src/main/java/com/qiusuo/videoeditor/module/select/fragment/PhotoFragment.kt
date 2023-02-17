package com.qiusuo.videoeditor.module.select.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.videoeditor.adapter.GridMediaAdapter
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.bean.MediaEntity
import com.qiusuo.videoeditor.common.constant.RequestCode
import com.qiusuo.videoeditor.common.data.MediaDataRepository
import com.qiusuo.videoeditor.databinding.FragmentMediaLayoutBinding
import com.qiusuo.videoeditor.module.select.LoadMediaViewModel
import com.qiusuo.videoeditor.module.select.SelectClipActivity
import com.qiusuo.videoeditor.module.select.adapter.MediaAdapterProxy
import com.qiusuo.videoeditor.module.select.adapter.MediaRecyclerAdapter
import com.qiusuo.videoeditor.ui.decoration.GridLayoutManagerNoAnimate
import com.qiusuo.videoeditor.ui.widgegt.selection.HeaderViewHolder
import com.qiusuo.videoeditor.ui.widgegt.selection.SectionedSpanSizeLookup
import com.qiusuo.videoeditor.util.SpUtil

class PhotoFragment : BaseFragment<FragmentMediaLayoutBinding>(FragmentMediaLayoutBinding::inflate),
    SelectFragment {


    private var dateGridLayoutManager: GridLayoutManager? = null
    private var normalGridLayoutManager: GridLayoutManager? = null
    private var currentLayoutManager: GridLayoutManager? = null
    private var gridAdapter: GridMediaAdapter? = null
    private var mMediaRecyclerAdapter: MediaRecyclerAdapter? = null
    private var adapter: RecyclerView.Adapter<*>? = null

    var viewModel: LoadMediaViewModel? = null;

    //private var dates: List<String?>? = null
    //private var hashMap: Map<String, List<MediaEntity>>? = null
    private var proxy: MediaAdapterProxy? = null


    override fun initView() {
        SelectClipActivity.vmOwner?.let {
            viewModel = ViewModelProvider(it).get(LoadMediaViewModel::class.java);
        }
        if (viewModel == null) {
            viewModel = ViewModelProvider(activity).get(LoadMediaViewModel::class.java);
        }
        //dates = MediaManager.getInstance().currentPhotoDates
        //hashMap = MediaManager.getInstance().currentPhotoMap
        dateGridLayoutManager = GridLayoutManagerNoAnimate(context, 4)
        currentLayoutManager = dateGridLayoutManager
        normalGridLayoutManager = GridLayoutManagerNoAnimate(context, 4)
        proxy = MediaAdapterProxy(
            context,
            viewModel?.getCurrentDateTitle(LoadMediaViewModel.PHOTO),
            viewModel?.getCurrentDateMap(LoadMediaViewModel.PHOTO),
            viewModel?.getCurrentGridList(LoadMediaViewModel.PHOTO)
        )
        gridAdapter = GridMediaAdapter(proxy!!)
        mMediaRecyclerAdapter = MediaRecyclerAdapter(proxy)
        adapter = mMediaRecyclerAdapter
        if (SpUtil.getInt(SpUtil.MEDIA_SHOW_TYPE, 1) == 1) {
            adapter = gridAdapter
            currentLayoutManager = normalGridLayoutManager
        }
        val lookup = SectionedSpanSizeLookup(mMediaRecyclerAdapter, dateGridLayoutManager)
        dateGridLayoutManager?.setSpanSizeLookup(lookup)
        viewBinding.mediaRecycler.layoutManager = currentLayoutManager
        proxy!!.onItemClickListener = object : MediaAdapterProxy.onItemClickListener {
            override fun onItemSelected(view: View, mediaItem: MediaEntity) {
                val childCoordinate = IntArray(2)
                view.getLocationInWindow(childCoordinate)
                val intent = Intent()
                intent.putExtra("mediaitem", mediaItem)
                intent.putExtra("locate", childCoordinate)
                intent.action = SelectClipActivity.ACTION_SELECT
                //MediaDataRepository.getInstance().tempMediaEntity = mediaItem
                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)
            }

            override fun onItemClick(view: View, mediaItem: MediaEntity, position: Int) {
                val childCoordinate = IntArray(2)
                view.getLocationInWindow(childCoordinate)
            }

            override fun selectAll(selectDate: String, toSelect: Boolean) {
            }
        }
        (viewBinding.mediaRecycler.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations =
            false
        viewBinding.mediaRecycler.adapter = adapter
        mMediaRecyclerAdapter!!.showSelectAll(showSelect)

        (viewModel as LoadMediaViewModel).photoLiveData.observe(this, {
            if (viewBinding.mediaRecycler.scrollState == RecyclerView.SCROLL_STATE_IDLE || !viewBinding.mediaRecycler.isComputingLayout) {
                try {
                    val refresh = proxy!!.setData(it.first, it.second, it.third)
                    requireActivity().runOnUiThread {
                        if (refresh) adapter?.notifyDataSetChanged()
                        if (!ObjectUtils.isEmpty(it.first)) {
                            viewBinding.noDataTip.visibility = View.GONE
                        } else {
                            viewBinding.noDataTip.visibility = View.VISIBLE
                        }
                        activity.endLoading()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        //recycleview的滑动底部监听
        viewBinding.mediaRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isBottom =
                    recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
                if (isBottom && !activity.loadShowing() && !(viewModel as LoadMediaViewModel).isPageMax()
                    && (viewModel as LoadMediaViewModel).folderType == LoadMediaViewModel.ALL && dy > 0
                ) {
                    (viewModel as LoadMediaViewModel).loadMoreMedia(LoadMediaViewModel.PHOTO)
                    activity.showLoading("")
                }
            }
        })
    }

    override fun updateData() {
        if (mMediaRecyclerAdapter == null) {
            return
        }
        if (viewBinding!!.mediaRecycler.scrollState == RecyclerView.SCROLL_STATE_IDLE ||
            !viewBinding!!.mediaRecycler.isComputingLayout
        ) {
            val dates =
                (viewModel as LoadMediaViewModel).getCurrentDateTitle(LoadMediaViewModel.PHOTO)
            val refresh = proxy!!.setData(
                dates,
                (viewModel as LoadMediaViewModel).getCurrentDateMap(LoadMediaViewModel.PHOTO),
                (viewModel as LoadMediaViewModel).getCurrentGridList(LoadMediaViewModel.PHOTO)
            )
            adapter!!.notifyDataSetChanged()
            if (!ObjectUtils.isEmpty(dates)) {
                viewBinding!!.noDataTip.visibility = View.GONE
            } else {
                viewBinding!!.noDataTip.visibility = View.VISIBLE
            }
            activity.endLoading()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (proxy != null) {
            proxy!!.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setMediaRecyclerFrozen(frozen: Boolean) {
        if (viewBinding != null) {
            viewBinding!!.mediaRecycler.isLayoutFrozen = frozen
        }
    }

    /**
     * 更新媒体元素右上角选中按钮的位置
     *
     * @param mediaId 素材唯一标识
     */
    fun updateSelectedItemIndex(mediaId: Int) {
        if (currentLayoutManager == null) {
            return
        }
        for (i in 0 until currentLayoutManager!!.childCount) {
            val view = currentLayoutManager!!.getChildAt(i)
            val holder: Any = viewBinding!!.mediaRecycler.getChildViewHolder(view!!)
            if (holder is MediaAdapterProxy.MyViewHolder) {
                val myHolder = holder
                if (myHolder.id == mediaId) {
                    val adapterPosition = myHolder.adapterPosition
                    requireActivity().runOnUiThread { adapter!!.notifyItemChanged(adapterPosition) }
                    //                    LogUtils.d(getClass().getSimpleName() + "#updateSelectedItemIndex_step1", "id:" + mediaId);
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
                    requireActivity().runOnUiThread {
                        adapter!!.notifyItemChanged(
                            adapterPosition,
                            RequestCode.SELECT
                        )
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
        if (mediaType == MediaEntity.TYPE_VIDEO) {
            return
        }
        var count = proxy!!.selectCount[date]
        if (count == null) {
            count = 0
        }
        if (increase) {
            proxy!!.selectCount[date] = count + 1
            LogUtils.v("selectCount Photo", date + "count:" + (count + 1))
        } else {
            proxy!!.selectCount[date] = count - 1
            LogUtils.v("selectCount Photo", date + "count:" + (count - 1))
        }
    }

    /**
     * 清除选择计数
     */
    override fun clearSelectCount() {
        for (date in proxy!!.selectCount.keys) {
            proxy!!.selectCount[date] = 0
            LogUtils.v("selectCount Photo", date + "count:0")
        }
    }

    /**
     * 更新全选按钮
     */
    override fun updateSections() {
        mMediaRecyclerAdapter!!.notifyItemRangeChanged(
            0,
            mMediaRecyclerAdapter!!.itemCount,
            RequestCode.SECTIONS
        )
    }

    override fun recountSections() {
        if (proxy != null) {
            proxy!!.selectCount.clear()
            LogUtils.v("selectCount Photo", "clear")
            mMediaRecyclerAdapter!!.notifyItemRangeChanged(
                0,
                mMediaRecyclerAdapter!!.itemCount,
                RequestCode.SECTIONS
            )
        }
    }
}