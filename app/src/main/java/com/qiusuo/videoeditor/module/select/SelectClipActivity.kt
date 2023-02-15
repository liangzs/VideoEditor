package com.qiusuo.videoeditor.module.select

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PointF
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.global.ThreadPoolManager
import com.ijoysoft.mediasdk.common.global.ThreadPoolMaxThread
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.entity.DurationInterval
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.MediaType
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem
import com.ijoysoft.mediasdk.module.mediacodec.FfmpegBackgroundHelper
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.base.ViewModelActivity
import com.qiusuo.videoeditor.common.bean.MediaItemPicker
import kotlinx.coroutines.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.lang.Runnable
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by wh on 2019/4/26.
 */
class SelectClipActivity : ViewModelActivity<LoadMediaViewModel, ActivitySelectClipBinding>(LoadMediaViewModel::class.java,ActivitySelectClipBinding),
    MediaItemPicker, EasyPermissions.PermissionCallbacks, View.OnClickListener {
    override fun createRootBinding(): ActivitySelectClipBinding {
        return ActivitySelectClipBinding.inflate(layoutInflater)
    }

    private var mBottomContentRecyclerAdapter: BottomContentRecyclerAdapter? = null
    private var mMyBroadCarst: MyBroadCarst? = null
    private var img: MoveImageView? = null
    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null
    private var fragment: SelectFragment? = null

    private val DIRFRAGMENT_TAG = "DIRFRAGMENT_TAG"
    private var mVibrator: Vibrator? = null
    private var isSave = false // 是否直接保存
    var isAddClip = false // 是否从其他界面来添加数据
        private set

    /**
     * 直接跳转到贴纸
     */
    private var toStickerGroup: String? = null

    /**
     * 直接跳转到字幕
     */
    private var toFontEntity: String? = null
    private var isShowOriginData = false // 是否顯示已經添加的數據
    private var selectAllMenuItem: MenuItem? = null
    private var moreItem: MenuItem? = null
    private var mediaListCopy: ArrayList<MediaItem>? = null
    private val videoOrPhoto = 0

    //素材查询
    //private var contentDataLoadTask: ContentDataLoadTask? = null

    /**
     * 预览素材点击的屏幕位置
     */
    var previewClickCoordinate: IntArray? = null

    /**
     * 抽屉布局助手
     * 调节BottomSheet的高度需要调节的三个地方：
     * 增加减少的量要一致
     * activity_select_clip 的rl_container 的 paddingBottom
     * activity_select_clip 的recycler 的 layout_height
     * helper 初始化时传入的参数 recyclerCollapsedHeight
     */
    var bottomSheetHelper: BottomSheetHelper<ConstraintLayout>? = null

    /**
     * 文件夹三角形所占用的宽度
     */
    var visibleModeWidth = 0f

    /**
     * 布局模式菜单
     */
    private var menuPopup: BaseTitleMenu? = null

    /**
     * 二级菜单
     */
    private var commonMenuPop: CommonMenuPop? = null

    //后台回收数据
    private var isReCreate = false
    private var mediaFolderSelectPopup: MediaFolderSelectPopup? = null

    //选中fragment
    private var selectFragmtnType = SelectFragmentEvent.SELECT_ALL

    /**
     * 文件默认排序
     */
    private var mediaSortType: MediaSortType? = null

    /**
     * 布局模式
     */
    private var mediaShowType = 0

    /**
     * addClip模式下原来有的数量
     */
    var originSize = 0

    override fun getIntentExtras(intent: Intent?) {
        if (intent != null) {
            isAddClip = intent.getBooleanExtra(ContactUtils.ADD_CLIP, false)

            if (isAddClip) {
                originSize = MediaDataRepository.getInstance().dataOperate.size
            }
            if (isAddClip || intent.getBooleanExtra(ContactUtils.DRAFT_EDIT_TO_CLIP, false)) {
                mediaListCopy = MediaDataRepository.getInstance().dataOperateCopy
            }
            isShowOriginData = intent.getBooleanExtra(ContactUtils.SHOW_ORIGIN_ITEM, false)
            toStickerGroup = intent.getStringExtra(ContactUtils.STICKER_GROUP)
            toFontEntity = intent.getStringExtra(ContactUtils.FONT_ENTITY)
        }
    }

    /**
     * 选中前景色
     */
    var themeSpan: ForegroundColorSpan? = null

    /**
     * 左侧提示字符
     */
    var hintLeft: String? = null

    /**
     * 右侧提示字符
     */
    var hintRight: String? = null

    /**
     * 提示
     */
    var hint = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        vmOwner = this;
        super.onCreate(savedInstanceState)
    }

    override fun initEventAndData(savedInstanceState: Bundle?) {
        showLoading(true, "")
        hintLeft = getString(R.string.photo)
        hintRight = getString(R.string.video)
        themeSpan = ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.text_num, null))
        binding.textNum.viewTreeObserver.addOnGlobalLayoutListener {
            if (binding.textNum.width < binding.textNum.paint.measureText(hint)) {
                val hint = hint.substring(hintLeft!!.length)
                val builder = SpannableStringBuilder(hint)
                //为不同位置字符串设置不同颜色
                builder.setSpan(themeSpan, 0, hint.length - hintRight!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                //最后为textview赋值
                binding.textNum.text = builder
            }
            //                mTextNum.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        binding.ivClear.setOnClickListener(this)
        binding.llFolder.root.setOnClickListener(this)
        binding.buttonNext.setOnClickListener(this)
        mVibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        bottomSheetHelper = BottomSheetHelper(binding.recycler, binding.dragView, resources.getDimension(R.dimen.dp_102).toInt(), 0.7f, object : SlidingUpListener {
            override fun onExpanded() {
//                        setTipsParams(RelativeLayout.LayoutParams.MATCH_PARENT);
                if (mBottomContentRecyclerAdapter!!.datas.isEmpty()) {
                    // setTipsParams(RelativeLayout.LayoutParams.MATCH_PARENT);
                    binding.dragTip.visibility = View.INVISIBLE
                } else {
                    binding.dragTip.visibility = View.VISIBLE
                }
            }

            override fun onDraggingOut() {
//                        setTipsParams(RelativeLayout.LayoutParams.MATCH_PARENT);
                if (mBottomContentRecyclerAdapter!!.datas.isEmpty()) {
//                             setTipsParams(RelativeLayout.LayoutParams.MATCH_PARENT);
                    binding.dragTip.visibility = View.INVISIBLE
                } else {
                    binding.dragTip.visibility = View.VISIBLE
                }
            }

            override fun onDraggingIn() {
//                        setTipsParams((int) getResources().getDimension(R.dimen.dp_120));
                if (mBottomContentRecyclerAdapter!!.datas.isEmpty()) {
//                             setTipsParams((int) getResources().getDimension(R.dimen.dp_120));
                    binding.dragTip.visibility = View.INVISIBLE
                } else {
                    binding.dragTip.visibility = View.VISIBLE
                }
            }

            override fun onCollapsed() {
//                        setTipsParams((int) getResources().getDimension(R.dimen.dp_120));
                if (mBottomContentRecyclerAdapter!!.datas.isEmpty()) {
                    // setTipsParams((int) getResources().getDimension(R.dimen.dp_120));
                    binding.dragTip.visibility = View.INVISIBLE
                } else {
                    binding.dragTip.visibility = View.VISIBLE
                }
            }
        })
        //rv会自动调整
        bottomSheetHelper!!.fitRecyclerMode = BottomSheetHelper.MODE_WEIGHT

        //在布局完成后调整高度
        binding.recycler.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (bottomSheetHelper!!.setExpandHeight()) {
                    binding.recycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
        initFragment(savedInstanceState)
        initData()
        initToolbar()
        viewModel.checkLoadData(LoadMediaViewModel.MIX)
    }

    override fun createViewModel(): LoadMediaViewModel {
        return ViewModelProvider(this).get(LoadMediaViewModel::class.java)
    }

    private fun initToolbar() {
        binding.toolbar.title = ""
        binding.llFolder.tvTitle.setText(R.string.all)
        //必须先setSupportActionBar， 再设置监听setNavigationOnClickListener， 否则不起效果
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.vector_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        //具体宽度看布局
        visibleModeWidth = resources.getDimension(R.dimen.sp_16) + resources.getDimension(R.dimen.dp_12)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        mediaShowType = SharedPreferencesUtil.getInt(ContactUtils.MEDIA_SHOW_TYPE, 1)
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()
        if (savedInstanceState != null) {
            val listFragment = fragmentManager!!.fragments
            for (fragment: Fragment in listFragment) {
                fragmentTransaction!!.remove(fragment)
            }
        }
        //val mediaMode = MediaManager.MIX
        //MediaManager.getInstance().setMode(mediaMode)
        //MediaManager.getInstance().folderType = MediaManager.ALL
        fragment = SelectMediaFragment.newInstance(viewModel.mediaMode)
        (fragment as SelectMediaFragment?)!!.showSelect()
        fragmentTransaction!!.replace(R.id.rl_container, (fragment as Fragment?)!!, "SelectMediaFragment_clip")
        if (savedInstanceState == null) {
            fragmentTransaction!!.addToBackStack("0")
        }
        fragmentTransaction!!.commitAllowingStateLoss()
    }

    private fun initData() {
        val gridLayoutManager = SmoothScrollLayoutManager(this, 4)
        binding.recycler.layoutManager = gridLayoutManager
        val mDragCallback = ItemDragHelperCallback { position -> position >= 0 }
        mDragCallback.isLongPressDragEnabled = true
        val mItemTouchHelper = ItemTouchHelper(mDragCallback)
        mItemTouchHelper.attachToRecyclerView(binding.recycler)
        mBottomContentRecyclerAdapter = BottomContentRecyclerAdapter(this, ArrayList())
        mBottomContentRecyclerAdapter!!.setOriginSize(originSize)
        if (!isAddClip && MediaDataRepository.getInstance().isLocal) {
            LogUtils.i("SelectClipActivity", "isLocal")
            if (ObjectUtils.isEmpty(MediaDataRepository.getInstance().dataOperate)) {
                LogUtils.i("SelectClipActivity", "empty return ")
                return
            }
            LogUtils.i("SelectClipActivity", "not empty continue return ")
            mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
            binding.addClipTip.visibility = View.INVISIBLE
            binding.recycler.visibility = View.VISIBLE
            binding.dragTip.visibility = View.VISIBLE
        } else if (isAddClip && isShowOriginData) {
            if (ObjectUtils.isEmpty(MediaDataRepository.getInstance().dataOperate)) {
                return
            }
            mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
            binding.addClipTip.visibility = View.INVISIBLE
            binding.recycler.visibility = View.VISIBLE
            binding.dragTip.visibility = View.VISIBLE
        }
        if (mBottomContentRecyclerAdapter!!.datas.size == 0) {
            binding.addClipTip.visibility = View.VISIBLE
            binding.recycler.visibility = View.INVISIBLE
            binding.dragTip.visibility = View.INVISIBLE
            updateNum(0, 0)
        }
        binding.buttonNext.isEnabled = mBottomContentRecyclerAdapter!!.datas.size != 0
        mBottomContentRecyclerAdapter!!.setItemClickListener(object : BottomContentRecyclerAdapter.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (!AndroidUtil.chekcFastClickShort()) {
                    val moperateData = MediaDataRepository.getInstance().dataOperate
                    if (moperateData == null || position >= moperateData.size) {
                        T.showShort(this@SelectClipActivity, resources.getString(R.string.delete_media_tip))
                        return
                    }
                    ThreadPoolManager.getThreadPool().execute(Runnable {
                        MediaDataRepository.getInstance().remove(position + originSize)
                    })
                    if (position >= mBottomContentRecyclerAdapter!!.datas.size || position < 0) {
                        return
                    }
                    mBottomContentRecyclerAdapter!!.remove(view, position)
                    if (mBottomContentRecyclerAdapter!!.datas.size == 0) {
                        binding.addClipTip.visibility = View.VISIBLE
                        binding.recycler.visibility = View.INVISIBLE
                        binding.dragTip.visibility = View.INVISIBLE
                    }
                    updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
                    //TODO:性能可能会比较差，权宜之计
                    fragment!!.recountSections()
                    view.post { mBottomContentRecyclerAdapter!!.notifyDataSetChanged() }
                }
            }

            override fun onItemLongClick() {
                mVibrator!!.vibrate(100)
            }
        })
        mBottomContentRecyclerAdapter!!.setMoveOutBoundsListener {
            T.showShort(this@SelectClipActivity, resources.getString(R.string.move_media_tip))
        }
        LogUtils.i("SelectClipActivity", "binding.recycler.setAdapter")
        binding.recycler.adapter = mBottomContentRecyclerAdapter
        mediaSortType = MediaSortType.getSortType(SharedPreferencesUtil.getInt(ContactUtils.MEDIA_SORT, 0))
        viewModel.setMediaView(mediaSortType!!, MediaShowType.getSortType(mediaShowType))
    }

    /**
     * 更新选择数字
     */
    @Synchronized
    private fun updateNum(photoNum: Int, videoNum: Int) {
        runOnUiThread {
            val sb = StringBuffer(hintLeft)
            sb.append("(").append(photoNum).append(")")
            sb.append(" ")
            sb.append(hintRight).append("(").append(videoNum).append(")")
            binding.textNum.text = sb.toString()
            binding.buttonNext.isEnabled = (photoNum + videoNum) != 0
        }
    }

    private val mHandler = Handler()

    /**
     * 当前文件夹id
     */
    var bucketId = -1


    /**
     * 选中文件夹
     */
    //@Subscribe
    //fun selectMediaSetEvent(selectMediaSetEvent: SelectMediaSetEvent) {
    //    if (selectMediaSetEvent.name == null) {
    //        selectMediaSetEvent.name = getString(R.string.all)
    //    }
    //    bucketId = selectMediaSetEvent.bucketId
    //    breakAndSetTextWithTextView(binding.llFolder.tvTitle, selectMediaSetEvent.name, 0, binding.llFolder.root.width - visibleModeWidth.toInt())
    //    lifecycleScope.launch {
    //        mediaFolderSelectPopup?.dismiss()
    //        delay(100)
    //        showLoading("")
    //        withContext(Dispatchers.IO) {
    //            if (bucketId == -1) {
    //                viewModel.folderType = LoadMediaViewModel.ALL
    //            } else {
    //                viewModel.folderType = LoadMediaViewModel.FOLDER
    //            }
    //            viewModel.queryMediaSetBybucket(bucketId);
    //        }
    //        fragment!!.updateData()
    //        endLoading()
    //    }
    //}

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState);
        isReCreate = true
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isReCreate = true
        LogUtils.i("test", "isReCreate:$isReCreate")
    }

    private var mPreviewItem = MediaEntity()

//    @Subscribe
//    fun mediaNotify(event: PhotoNotifyEvent) {
//        fragment!!.updateData()
//    }

//    @Subscribe
//    fun mediaNotify(event: PhotoSelectAll) {
//        showLoading("")
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                val list: List<MediaEntity>? = viewModel.getMediaByDate(event.date)
//                if (event.isToSelect) {
//                    selectAll(list!!)
//                } else {
//                    unSelectAll(list!!)
//                }
//            }
//        }
//    }

    private suspend fun selectAll(mediaEntities: List<MediaEntity>) {
        val count: CountDownLatch = CountDownLatch(mediaEntities.size)
        var over = false
        var toShowVideoToast = true
        var toShowMoreDialog = true
        //预先处理，避免多线程等待
        MediaDataRepository.getInstance().preTrementGif()
        //for (mediaEntity: MediaEntity in mediaEntities) {
        mediaEntities.forEach { mediaEntity ->
            //}
            //视频数量超过40检测
            if (MediaDataRepository.getInstance().videoCount >= 40 && mediaEntity.type == MediaEntity.TYPE_VIDEO) {
                if (toShowVideoToast) {
                    T.showShort(this@SelectClipActivity, resources.getString(R.string.not_add_more_video))
                    toShowVideoToast = false
                }
                //跳过这个视频
                count.countDown()
                return@forEach
            }

            if (MediaDataRepository.getInstance().dataOperate.size == 100 && !over && toShowMoreDialog) {
                over = withContext(Dispatchers.Main) {
                    //暂停协程，并询问是否愿意超过100张
                    !askContinueSelectDialog()
                }
                toShowMoreDialog = false
            }

            if (over || MediaDataRepository.getInstance().dataOperate.size >= 200) {
                over = true
                //超过了200的限制
                count.countDown()
                return@forEach
            }
            val index = MediaDataRepository.getInstance().indexOfMediaEntity(mediaEntity)
            if (index == -1) {
                MediaDataRepository.getInstance().addMediaItem(mediaEntity, true) { m: MediaItem? ->
                    count.countDown()
                    runOnUiThread {
                        if (loadShowing()) {
                            //如果已经关闭则不显示
                            showLoading(resources.getString(R.string.selected) + " ${MediaDataRepository.getInstance().dataOperate.size} " + resources.getString(R.string.clips), true)
                        }
                    }
                }
                val date = TimeUtils.format(mediaEntity.dateModify, TimeUtils.Date_PATTER)
                fragment?.updateDateCount(date, mediaEntity.getType(), true)
            } else {
                if (isAddClip && mBottomContentRecyclerAdapter!!.indexOfPath(mediaEntity.path) == -1) {
                    //添加模式下还未被复制
                    MediaDataRepository.getInstance().copyMediaItemToPosition(index, MediaDataRepository.getInstance().dataOperate.size)
                    val date = TimeUtils.format(mediaEntity.dateModify, TimeUtils.Date_PATTER)
                    fragment?.updateDateCount(date, mediaEntity.getType(), true)
                    runOnUiThread {
                        if (loadShowing()) {
                            //如果已经关闭则不显示
                            showLoading(resources.getString(R.string.selected) + " ${MediaDataRepository.getInstance().dataOperate.size} " + resources.getString(R.string.clips), true)
                        }
                    }
                }
                count.countDown()
            }
        }
        try {
            //等待60秒
            count.await(60, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            //                    throw new RuntimeException(e);
        }
        withContext(Dispatchers.Main) {
            if (MediaDataRepository.getInstance().dataOperate.size > 60) {
                T.showShort(this@SelectClipActivity, resources.getString(R.string.out_of_memory))
            }
            binding.dragTip.visibility = View.VISIBLE
            binding.addClipTip.visibility = View.INVISIBLE
            binding.recycler.visibility = View.VISIBLE
            if (originSize > 0) {
                mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate.subList(originSize, MediaDataRepository.getInstance().dataOperate.size)
            } else {
                mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate
            }
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
            fragment!!.updateSelect()
            endLoading()
            if (MediaDataRepository.getInstance().dataOperate.size == 200) {
                T.showShort(this@SelectClipActivity, resources.getString(R.string.not_add_more_photo))
            }
        }
    }

    private suspend fun unSelectAll(mediaEntities: List<MediaEntity>) {
        val index: MutableList<Int> = ArrayList(mediaEntities.size)
        val dataoperate = mBottomContentRecyclerAdapter!!.datas
        val size = dataoperate.size
        //复制为链表
        val tempEntities = LinkedList(mediaEntities)
        for (i in 0 until size) {
            val length = tempEntities.size
            //遍历一次，转一圈为止
            for (j in 0 until length) {
                //移除第一个
                val entity = tempEntities.removeFirst()
                //判断满足
                if ((entity.id == dataoperate[i].id //添加模式下已选择的素材路径一致
                            || isAddClip && (entity.getPath() == dataoperate[i].path))
                ) {
                    index.add(i)
                    val date = TimeUtils.format(entity.dateModify, TimeUtils.Date_PATTER)
                    fragment!!.updateDateCount(date, entity.getType(), false)
                    break
                } else {
                    //不满足则加到最后
                    tempEntities.offerLast(entity)
                }
            }
        }
        //从大到小
        index.sortWith { a: Int, b: Int -> b - a }
        for (i: Int in index) {
            MediaDataRepository.getInstance().remove(i + originSize)
        }
        withContext(Dispatchers.Main) {
            if (MediaDataRepository.getInstance().dataOperate.isEmpty()) {
                binding.dragTip.visibility = View.INVISIBLE
                binding.addClipTip.visibility = View.VISIBLE
                binding.recycler.visibility = View.INVISIBLE
            }
            if (MediaDataRepository.getInstance().dataOperate.size == originSize) {
                mBottomContentRecyclerAdapter!!.clear()
            } else {
                if (originSize > 0) {
                    mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate.subList(originSize, MediaDataRepository.getInstance().dataOperate.size)
                } else {
                    mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate
                }
            }
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
            fragment!!.updateSelect()
            endLoading()
        }
    }


    /**
     * 暂停协程，弹窗并询问是否继续
     * 将回调包装成协程，回调前挂起，回调后唤醒
     */
    private suspend fun askContinueSelectDialog(): Boolean {
        return suspendCoroutine { continuation ->
            val view = layoutInflater.inflate(R.layout.dialog_save_draft_layout, null)
            val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
            val ok = view.findViewById<TextView>(R.id.ok)
            val cancel = view.findViewById<TextView>(R.id.cancel)
            view.findViewById<TextView>(R.id.message).setText(R.string.dialog_ask_select_over_100)
            builder.setView(view)
            alertDialog = builder.create()

            val onClickListener = object : OnClickListener {

                var result: Boolean? = null

                override fun onClick(v: View?) {
                    //之前没赋值就赋值一次，赋值了就
                    result ?: run {
                        //只有ok才能返回true
                        //没做任何处理都返回false
                        (v == ok).let {
                            continuation.resume(it)
                            result = it
                        }
                    }
                    //按钮逻辑dismiss
                    v?.let {
                        alertDialog?.dismiss()
                    }
                }
            }

            ok.setOnClickListener(onClickListener)
            cancel.setOnClickListener(onClickListener)
            alertDialog?.setOnDismissListener {
                onClickListener.onClick(null)
            }

            /* show之前调用，取消弹出状态栏 */DialogUtil.cancelDialogfocusable(alertDialog)
            alertDialog!!.show()
            DialogUtil.fullScreenImmersive(alertDialog!!.window!!.decorView)
            DialogUtil.recoverDialogFocusable(alertDialog)
        }

    }

    /**
     * 收到预览消息
     *
     * @param previewImg
     */
    //@Subscribe
    //fun setPreviewImg(previewImg: PreviewEvent) {
    //    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
    //        mPreviewItem = previewImg.mediaItem
    //        if (mPreviewItem == null) {
    //            return
    //        }
    //        if (mPreviewItem.type == MediaEntity.TYPE_VIDEO) {
    //            mPreviewItem = MediaDataRepository.getInstance().tempMediaEntity
    //        }
    //        //已选中则不需要添加动画坐标，用来标记原来的状态
    //        if (getMediaItemSelectedIndex(mPreviewItem) == null) {
    //            previewClickCoordinate = previewImg.previewClickCoordinate
    //        } else {
    //            previewClickCoordinate = null
    //        }
    //        var sourceIndex = if (previewImg.isBottom) {
    //            previewImg.sourceIndex
    //        } else {
    //            viewModel.getCurrentGridList(viewModel.mediaMode).indexOf(previewImg.mediaItem)
    //        }
    //        if (previewImg.isBottom) {
    //            PreviewPhotoActivity.start(this, originSize, sourceIndex, ContactUtils.MODE_MAKE, true)
    //        } else {
    //            PreviewPhotoActivity.start(this, originSize, sourceIndex, ContactUtils.MODE_MAKE)
    //        }
    //    }
    //}
    //
    //@Subscribe
    //fun SelectFragmentEvent(selectFragmentEvent: SelectFragmentEvent) {
    //    selectFragmtnType = selectFragmentEvent.type
    //    when (selectFragmtnType) {
    //        SelectFragmentEvent.SELECT_ALL -> {
    //            moreItem!!.isVisible = true
    //            viewModel.mediaMode = LoadMediaViewModel.MIX
    //            //MediaManager.getInstance().mediaMode = MIX;
    //        }
    //        SelectFragmentEvent.SELECT_PHOTO -> {
    //            moreItem!!.isVisible = true
    //            viewModel.mediaMode = LoadMediaViewModel.PHOTO
    //            //MediaManager.getInstance().mediaMode = PHOTO;
    //        }
    //        SelectFragmentEvent.SELECT_VIDEO -> {
    //            moreItem!!.isVisible = true
    //            viewModel.mediaMode = LoadMediaViewModel.VIDEO
    //            //MediaManager.getInstance().mediaMode = VIDEO;
    //        }
    //        SelectFragmentEvent.SELECT_MATERIAL -> {
    //            moreItem!!.isVisible = false
    //        }
    //    }
    //}

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_clear -> {
                isSave = false
                showLoading(false, "")
                ThreadPoolMaxThread.getInstance().execute {
                    ThreadPoolManager.getThreadPool().shutDownNow()
                    if (ThreadPoolManager.getThreadPool().awaitTermination(5)) {
                        ThreadPoolManager.getThreadPool().release()
                        Dlog.i(TAG, "release")
                        endLoading()
                    }
                    //这里初始化视频播放类信息
                    endLoading()
                    if (isAddClip && !isShowOriginData) {
                        var i = MediaDataRepository.getInstance().dataOperate.size - 1
                        while (i >= originSize) {
                            MediaDataRepository.getInstance().remove(i)
                            i--
                        }
                    } else {
                        MediaDataRepository.getInstance().clear()
                    }
                    runOnUiThread {
                        fragment!!.clearSelectCount()
                        binding.addClipTip.visibility = View.VISIBLE
                        binding.recycler.visibility = View.INVISIBLE
                        binding.dragTip.visibility = View.INVISIBLE
                        updateNum(0, 0)
                        view.post {
                            mBottomContentRecyclerAdapter!!.clear()
                            binding.buttonNext.isEnabled = false
                            mediaNotify(PhotoNotifyEvent(true))
                        }
                    }
                }
            }
            R.id.button_next -> {
                if (mBottomContentRecyclerAdapter!!.datas.isEmpty()) {
                    T.showShort(this, getString(R.string.select_clip_enable_tip))
                    return
                }
                if (!AndroidUtil.chekcFastClick()) {
                    isSave = true
                    showLoading(true, getString(R.string.loading))
                    next()
                    MediaDataRepository.getInstance().isHasEdit = true
                    MediaDataRepository.getInstance().resetMusic()
                }
            }
            R.id.ll_folder -> showMediaFolder()
        }
    }

    private fun showMediaFolder() {
        if (mediaFolderSelectPopup == null) {
            mediaFolderSelectPopup = MediaFolderSelectPopup(this,
                MediaManager.getInstance().getCurrentMediaSets(selectFragmtnType), selectFragmtnType)
            //具体高度要看布局再做调整不要盲目调整
            //部分手机有1像素的误差，加1
            mediaFolderSelectPopup!!.height = ((1 + binding.rlContainer.height) - binding.toolbar.height - resources.getDimension(R.dimen.dp_184)).toInt()
            mediaFolderSelectPopup!!.showAsDropDown(binding.toolbar)
        } else {
            //具体高度要看布局再做调整不要盲目调整
            //部分手机有1像素的误差，加1
            mediaFolderSelectPopup!!.height = ((1 + binding.slideuppannel.height) - binding.toolbar.height - resources.getDimension(R.dimen.dp_184)).toInt()
            mediaFolderSelectPopup!!.showAsDropDown(binding.toolbar)
            mediaFolderSelectPopup!!.setMediaSets(MediaManager.getInstance().getCurrentMediaSets(selectFragmtnType), selectFragmtnType)
        }
    }

    /**
     * 下一步
     */
    operator fun next() {
        ThreadPoolMaxThread.getInstance().execute(object : Runnable {
            override fun run() {
                MediaDataRepository.getInstance().preTrementInfo()
                ThreadPoolManager.getThreadPool().shutDown()
                if (ThreadPoolManager.getThreadPool().awaitTermination(60)) {
                    ThreadPoolManager.getThreadPool().release()
                    Dlog.i(TAG, "release")
                    endLoading()
                }
                //这里初始化视频播放类信息
                if (!isSave) {
                    return
                }
                //                MediaPlayerWrapper.getInstance().setDataSource(MediaDataRepository.getInstance().getDataOperate(),false);
                ThreadPoolManager.getThreadPool().release()
                MediaDataRepository.getInstance().checkExtractVideoAudio()
                runOnUiThread(object : Runnable {
                    override fun run() {
                        endLoading()
                        if (!isAddClip) {
                            if (intent.getBooleanExtra(ContactUtils.MATERIAL_CREATE, false)) {
                                if (ConstantMediaSize.transitionSeries != null && ConstantMediaSize.transitionSeries !== TransitionSeries.NONE) {
                                    if (MediaDataRepository.getInstance().dataOperate.size < 2) {
                                        //一张图片不给进，防止崩溃
                                        T.showShort(this@SelectClipActivity, getString(R.string.single_not_support_transition))
                                        return
                                    } else {
                                        //移除记录，只跳转一次
                                        intent.removeExtra(ContactUtils.MATERIAL_CREATE)
                                        ProjectImplement.editTransition(this@SelectClipActivity)
                                    }
                                } else {
                                    //移除记录，只跳转一次
                                    intent.removeExtra(ContactUtils.MATERIAL_CREATE)
                                    val intent = Intent(this@SelectClipActivity, EditorActivity::class.java)
                                    intent.putExtra(ContactUtils.MATERIAL_CREATE, true)
                                    startActivityForResult(intent, ContactUtils.REQUEST_SELECT_TO_EDIT)
                                }
                            } else {
                                if (toStickerGroup != null) {
                                    val intent = Intent(this@SelectClipActivity, EditStickerActivity::class.java)
                                    intent.putExtra(ContactUtils.STICKER_GROUP, toStickerGroup)
                                    startActivityForResult(intent, ContactUtils.REQUEST_SELECT_TO_EDIT)
                                    toStickerGroup = null
                                } else if (toFontEntity != null) {
                                    val intent = Intent(this@SelectClipActivity, EditSubTitleActivity::class.java)
                                    intent.putExtra(ContactUtils.FONT_ENTITY, toFontEntity)
                                    startActivityForResult(intent, ContactUtils.REQUEST_SELECT_TO_EDIT)
                                    toFontEntity = null
                                } else {
                                    val intent = Intent(this@SelectClipActivity, EditorActivity::class.java)
                                    startActivityForResult(intent, ContactUtils.REQUEST_SELECT_TO_EDIT)
                                }
                            }
                        } else {
                            val intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }
                })
            }
        })
    }

    // 添加照片动画
    private fun addView(mediaItem: MediaEntity?, childCoordinate: IntArray) {
        LogUtils.i("SelectClipActivity", "addView animate")
        val selectCount = mBottomContentRecyclerAdapter!!.itemCount
        if (ConstantMediaSize.getHeapSize() < 128 && selectCount > 46) { // 手机内存小的，限定45个图片
            T.showShort(this@SelectClipActivity, getString(R.string.select_over_photo))
            return
        }
        MediaDataRepository.getInstance().dataOperate?.let {
            if (mediaItem!!.isImage) {
                if (MediaDataRepository.getInstance().dataOperate.size == 60) {
                    T.showShort(this@SelectClipActivity, resources.getString(R.string.out_of_memory))
                }
                if (MediaDataRepository.getInstance().dataOperate.size >= 200) {
                    T.showShort(this@SelectClipActivity, resources.getString(R.string.not_add_more_photo))
                    return
                }
            }
            if (!mediaItem.isImage) {
                if (MediaDataRepository.getInstance().videoCount >= 40) {
                    T.showShort(this@SelectClipActivity, resources.getString(R.string.not_add_more_video))
                    return
                }
            }
        }
        mBottomContentRecyclerAdapter!!.inCrease(mediaItem!!.isImage)
        val parentCoordinate = IntArray(2)
        val textCoordinate = IntArray(2)
        binding.rlContainer.getLocationInWindow(parentCoordinate)
        binding.textNum.getLocationInWindow(textCoordinate)
        img = MoveImageView(this@SelectClipActivity)
        img!!.scaleType = ImageView.ScaleType.FIT_XY
        val layoutParams = RelativeLayout.LayoutParams(60, 60)
        img!!.layoutParams = layoutParams
        img!!.x = (childCoordinate[0] - parentCoordinate[0]).toFloat()
        img!!.y = (childCoordinate[1] - parentCoordinate[1]).toFloat()
        Glide.with(this@SelectClipActivity).load(mediaItem.path).diskCacheStrategy(DiskCacheStrategy.NONE).override(100, 100).into(img!!)
        binding.rlContainer.addView(img)
        val startP = PointF()
        val endP = PointF()
        val controlP = PointF()
        // 开始的数据点坐标就是 选择图片的坐标
        startP.x = (childCoordinate[0] - parentCoordinate[0]).toFloat()
        startP.y = (childCoordinate[1] - parentCoordinate[1]).toFloat()
        // 结束的数据点坐标就是 text num的坐标
        endP.x = (textCoordinate[0] - parentCoordinate[0]).toFloat()
        endP.y = (textCoordinate[1] - parentCoordinate[1]).toFloat()
        // 控制点坐标 x等于 numtext x；y等于 选择图片的y
        controlP.x = endP.x
        controlP.y = startP.y
        val animator = ObjectAnimator.ofObject(img, "mPointF", PointFTypeEvaluator(controlP), startP, endP)
        animator.duration = 500
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                // 动画结束后 父布局移除 img
                val target = (animation as ObjectAnimator).target
                binding.rlContainer.removeView(target as View?)
                // 动画完成通知更新UI
                binding.recycler.post {
                    mBottomContentRecyclerAdapter!!.notifyDataSetChanged()
                    try {
                        binding.recycler.scrollToPosition(mBottomContentRecyclerAdapter!!.itemCount - 1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (mBottomContentRecyclerAdapter!!.itemCount > 0) {
                        binding.dragTip.visibility = View.VISIBLE
                        binding.addClipTip.visibility = View.INVISIBLE
                        binding.recycler.visibility = View.VISIBLE
                    }
                    binding.buttonNext.isEnabled = mBottomContentRecyclerAdapter!!.itemCount > 0
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.start()
        LogUtils.i("SelectClipActivity", "addView updateNum")
        ThreadPoolMaxThread.getInstance().execute {
            if (isAddClip) {
                //使用copy防止重复
                var i: Int = 0
                val dataOperate: List<MediaItem> = MediaDataRepository.getInstance().dataOperate
                val length: Int = dataOperate.size
                while (i < length) {
                    if (mediaItem.id == dataOperate[i].id) {
                        break
                    }
                    i++
                }
                if (i < length) {
                    //已经存在，采用复制方式
                    val mediaItem1: MediaItem = MediaDataRepository.getInstance().copyMediaItemToPosition(i, MediaDataRepository.getInstance().dataOperate.size)
                    runOnUiThread {
                        mBottomContentRecyclerAdapter!!.update(mediaItem1)
                        updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
                    }
                    return@execute
                }
            }
            MediaDataRepository.getInstance().addMediaItem(mediaItem, true) { mediaItem ->
                mBottomContentRecyclerAdapter!!.update(mediaItem)
                updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
            }
        }
    }

    internal inner class MyBroadCarst : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ((intent.action == ACTION_SELECT)) {
                val mediaItem = intent.getSerializableExtra("mediaitem") as MediaEntity?
                val childCoordinate = intent.getIntArrayExtra("locate")
                onMediaItemSelectingChanged(mediaItem, childCoordinate)
                // 数据添加给mediaRepository更新数据
                // MediaDataRepository.getInstance().addItem();
            }
        }
    }

    /**
     * 某个MediaItem选中状态变化
     * 注意，临时生成用于预览的MediaEntity，运行时务必不能运行到index == null 代码段
     * 确保其在MediaDataRepository中，确保getMediaItemSelectedIndex !=null，因为临时生成的实例，数据缺失
     *
     * @param mediaItem       MediaEntity
     * @param childCoordinate 触摸位置
     */
    fun onMediaItemSelectingChanged(mediaItem: MediaEntity?, childCoordinate: IntArray?) {
        val index: Int? = getMediaItemSelectedIndex((mediaItem)!!)
        if (index != null) {
            ThreadPoolManager.getThreadPool().execute({ MediaDataRepository.getInstance().remove(index + originSize) })
            val date = TimeUtils.format(mediaItem.dateModify, TimeUtils.Date_PATTER)
            fragment!!.updateDateCount(date, mediaItem.getType(), false)
            mBottomContentRecyclerAdapter!!.remove(null, index)
            if (mBottomContentRecyclerAdapter!!.datas.size == 0) {
                binding.addClipTip.visibility = View.VISIBLE
                binding.recycler.visibility = View.INVISIBLE
                binding.dragTip.visibility = View.INVISIBLE
            }
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)

            return
        }
        val path = mediaItem.path
        if (ObjectUtils.isEmpty(path)) {
            return
        }
        val file = File(path)
        if (!file.exists()) {
            T.showShort(this, resources.getString(R.string.file_not_exist))
            return
        }
        if (mediaItem == null || childCoordinate == null) {
            return
        }
        val date = TimeUtils.format(mediaItem.dateModify, TimeUtils.Date_PATTER)
        fragment!!.updateDateCount(date, mediaItem.getType(), true)
        addView(mediaItem, childCoordinate)
    }

    override fun onPause() {
        super.onPause()
        if (mMyBroadCarst != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMyBroadCarst!!)
        }
        try {
            AppBus.get().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            AppBus.get().register(this)
        } catch (ignored: Exception) {
        }
        if (mMyBroadCarst == null) {
            mMyBroadCarst = MyBroadCarst()
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_SELECT)
        intentFilter.addAction(ACTION_REMOVE)
        LocalBroadcastManager.getInstance(this).registerReceiver((mMyBroadCarst)!!, intentFilter)
        //if (viewModel.checkIsFolder()) {
        //    selectMediaSetEvent(SelectMediaSetEvent(bucketId, folderName))
        //}
        if (!isAddClip) {
            LogUtils.i("SelectClipActivity", "!isAddClip onResume")
            if (ObjectUtils.isEmpty(MediaDataRepository.getInstance().dataOperate)) {
                mBottomContentRecyclerAdapter!!.clear()
                binding.addClipTip.visibility = View.VISIBLE
                binding.recycler.visibility = View.INVISIBLE
                binding.dragTip.visibility = View.INVISIBLE
            } else {
                mBottomContentRecyclerAdapter!!.datas = MediaDataRepository.getInstance().dataOperate
                binding.addClipTip.visibility = View.INVISIBLE
                binding.recycler.visibility = View.VISIBLE
                binding.dragTip.visibility = View.VISIBLE
            }
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
        } else {
            val subList = MediaDataRepository.getInstance().dataOperate.let {
                it.subList(originSize, it.size)
            }
            if (subList.isEmpty()) {
                mBottomContentRecyclerAdapter!!.clear()
                binding.addClipTip.visibility = View.VISIBLE
                binding.recycler.visibility = View.INVISIBLE
                binding.dragTip.visibility = View.INVISIBLE
            } else {
                mBottomContentRecyclerAdapter!!.datas = subList
                binding.addClipTip.visibility = View.INVISIBLE
                binding.recycler.visibility = View.VISIBLE
                binding.dragTip.visibility = View.VISIBLE
            }
            updateNum(mBottomContentRecyclerAdapter!!.photoCount, mBottomContentRecyclerAdapter!!.videoCount)
        }
        fragment?.recountSections()
        fragment?.updateSelect()
        endLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaScannerConnection != null) {
            mMediaScannerConnection!!.disconnect()
        }
        //if (contentDataLoadTask != null) {
        //    contentDataLoadTask!!.onDestroy()
        //    contentDataLoadTask!!.setmOnContentDataLoadListener(null)
        //}
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.hide()
        }
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        this.setResult(-1) // 取消添加
        if (isAddClip && fragmentManager!!.backStackEntryCount == 1) { // 如果是二次添加不清除数据
            MediaDataRepository.getInstance().overideData(mediaListCopy)
            setResult(RESULT_CANCELED)
            finish()
            return
        }
        LogUtils.i("test", "fragmentManager.getBackStackEntryCount():" + fragmentManager!!.backStackEntryCount)
        if (fragmentManager!!.backStackEntryCount == 1) {
            if (!ObjectUtils.isEmpty(mBottomContentRecyclerAdapter!!.datas)) { // MediaDataRepository.getInstance().getDataOperate().size()
                if (MediaDataRepository.getInstance().isLocal || isSave) {
                    // 如果是本地草稿，只保留进入当前页面时刻的数据
                    MediaDataRepository.getInstance().overideData(mediaListCopy)
                    saveLocalLoading()
                } else {
                    showSaveDraftDialog()
                }
            } else {
                MediaDataRepository.getInstance().onDestory()
                finish()
            }
            return
        }
        super.onBackPressed()
    }

    private var alertDialog: AlertDialog? = null
    private fun showSaveDraftDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_save_draft_layout, null)
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
        val ok = view.findViewById<TextView>(R.id.ok)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        builder.setView(view)
        alertDialog = builder.create()
        ok.setOnClickListener {
            alertDialog!!.dismiss()
            saveLocalLoading()
        }
        cancel.setOnClickListener {
            alertDialog!!.dismiss()
            showLoading("")
            ThreadPoolMaxThread.getInstance().execute {
                ThreadPoolManager.getThreadPool().shutDownNow()
                endLoading()
                MediaDataRepository.getInstance().clearData() // 取消直接清除数据
                Dlog.i(TAG, "size:" + MediaDataRepository.getInstance().dataOperate.size)
                runOnUiThread { finish() }
            }
        }
        /* show之前调用，取消弹出状态栏 */DialogUtil.cancelDialogfocusable(alertDialog)
        alertDialog!!.show()
        DialogUtil.fullScreenImmersive(alertDialog!!.window!!.decorView)
        DialogUtil.recoverDialogFocusable(alertDialog)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.select_clip_menu, menu)
        //        mVideoMenuItem = menu.findItem(R.id.select_clip_menu_video);
//        mPhotoMenuItem = menu.findItem(R.id.select_clip_menu_camera);
        selectAllMenuItem = menu.findItem(R.id.select_all)
        //        selectAllMenuItem.setVisible(ContactUtils.MODE_MAKE.equals(mode));
        moreItem = menu.findItem(R.id.grid_layout)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.grid_layout -> {
                if (menuPopup == null) {
                    menuPopup = BaseTitleMenu(this, listOf(PopupItem.create(R.string.camera), PopupItem.createSubMenu(R.string.view_mode), PopupItem.createSubMenu(R.string.sort_by))) label@{ pos: Int ->
                        when (pos) {
                            0 -> {
                                if (MediaDataRepository.getInstance().dataOperate.size == 200) {
                                    T.showShort(this@SelectClipActivity, resources.getString(R.string.not_add_more_photo))
                                    return@label
                                }
                                commonMenuPop = null
                                val isGranted: Boolean = PermissionUtil.verifyCameraPermissions(this)
                                if (isGranted) {
                                    openCamera()
                                }
                            }
                            1 -> commonMenuPop = CommonMenuPop(this, mediaShowType, listOf(R.string.select_clip_layout_list, R.string.select_clip_layout_grid), CommonMenuPop.ItemClick { position: Int ->
                                mediaShowType = position
                                SharedPreferencesUtil.putInt(ContactUtils.MEDIA_SHOW_TYPE, position)
                                viewModel.mediaSortType = mediaSortType!!
                                fragment!!.switchAdapter(position)
                            })
                            2 -> commonMenuPop = CommonMenuPop(this, mediaSortType!!.type, listOf(R.string.date, R.string.name, R.string.compress_size), CommonMenuPop.ItemClick { position: Int ->
                                //更新大小
                                showLoading("")
                                lifecycleScope.launch {
                                    mediaSortType = MediaSortType.getSortType(position)
                                    withContext(Dispatchers.IO) {
                                        SharedPreferencesUtil.putInt(ContactUtils.MEDIA_SORT, position)
                                        viewModel.mediaFolderSort(mediaSortType!!);
                                    }
                                    endLoading()
                                }
                                //viewModel.mediaSortType=mediaSortType!!;
                                //ExecutorFactory.io().execute(Runnable {
                                //    contentDataLoadTask!!.synchMediaData()
                                //    MediaManager.getInstance()
                                //        .mediaFolderSort(mediaSortType)
                                //    try {
                                //        AppBus.get().post(PhotoNotifyEvent(true))
                                //    } catch (e: Exception) {
                                //        e.printStackTrace()
                                //    }
                                //    runOnUiThread(Runnable { endLoading() })
                                //})
                            })
                        }
                        commonMenuPop?.showAtWithoutMargin(binding.toolbar.findViewById(R.id.grid_layout), Gravity.TOP or Gravity.END)
                    }
                }
                menuPopup!!.show(binding.toolbar.findViewById(R.id.grid_layout))
            }
            R.id.select_all -> DialogUtil.createCommonDialog(this, getString(R.string.select_folder_all)) {

                val list: List<MediaEntity> = viewModel.getCurrentFolderPhotos()
                //MediaManager.getInstance().currentFolderPhotos
                if (list.isNotEmpty()) {
                    showLoading("")
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            selectAll(list)
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var photoUri: Uri? = null
    private var file: File? = null
    override fun openCamera() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val dirParent = File(PathUtil.IMAGE_PATH)
            if (!dirParent.exists()) {
                dirParent.mkdirs()
            }
            file = File(dirParent, PathUtil.getImageChildPath())
            photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 兼容7.0以上
                FileProvider.getUriForFile(this, "$packageName.fileProvider", file!!)
            } else {
                Uri.fromFile(file)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, TAKE_PHOTO)
            MyApplication.getInstance().isCamera = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var videoUri: Uri? = null
    private fun openVideoCamera() {
        try {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val dirParent = File(PathUtil.IMAGE_PATH)
            if (!dirParent.exists()) {
                dirParent.mkdirs()
            }
            file = File(dirParent, PathUtil.getVideoChildPath())
            videoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 兼容7.0以上
                FileProvider.getUriForFile(this, "$packageName.fileProvider", file!!)
            } else {
                Uri.fromFile(file)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
            startActivityForResult(intent, TAKE_VIDEO)
            MyApplication.getInstance().isCamera = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 坑，拍完照制定路径保存，只能通过这种方法通知媒体库更新，其他集中简单方便的插入方式要么不成功，要么和其他APP存在冲突，导致在
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> {
                MyApplication.getInstance().isCamera = false
                if (resultCode == RESULT_OK) {
                    mMediaScannerConnection = MediaScannerConnection(applicationContext, object : MediaScannerConnectionClient {
                        override fun onMediaScannerConnected() {
                            try {
                                if (file != null) {
                                    mMediaScannerConnection!!.scanFile(file!!.absolutePath, ".jpg")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onScanCompleted(path: String, uri: Uri) {
                            val mediaItem = LoadMediaUtils.loadSingleMedia(this@SelectClipActivity, uri)
                            mediaItem.setDuration(ConstantMediaSize.IMAGE_DURATION.toLong())
                            if (bucketId == mediaItem.bucketId) {
                                val mediaFolder = viewModel.mediaFolder
                                mediaFolder?.addLatestMedia(mediaItem)
                                fragment!!.updateData()
                            }
                            mediaFolderSelectPopup?.addAllCount()

                            runOnUiThread {
                                val locate: IntArray = intArrayOf(ScreenUtils.getScreenWidth(this@SelectClipActivity) / 2, ScreenUtils.getScreenHeight(this@SelectClipActivity) / 2)
                                //刚拍的照片使用拍摄时间
                                fragment?.recountSections()
                                addView(mediaItem, locate)
                            }
                            photoUri = null
                        }
                    })
                    mMediaScannerConnection!!.connect()
                }
            }
            TAKE_VIDEO -> {
                MyApplication.getInstance().isCamera = false
                if (resultCode == RESULT_OK) {
                    mMediaScannerConnection = MediaScannerConnection(applicationContext, object : MediaScannerConnectionClient {
                        override fun onMediaScannerConnected() {
                            try {
                                if (file != null) {
                                    mMediaScannerConnection!!.scanFile(file!!.absolutePath, ".mp4")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onScanCompleted(path: String, uri: Uri) {
                            Dlog.d("TAKE_VIDEO", "vido===$uri")
                            val mediaItem = LoadMediaUtils.loadSingleVideoMedia(this@SelectClipActivity, uri)
                            if (mediaItem != null) {
                                Dlog.d("TAKE_VIDEO", "vido===$uri, info====$mediaItem")
                            }
                            if (bucketId == mediaItem!!.bucketId) {
                                val mediaFolder = viewModel.mediaFolder
                                mediaFolder?.addLatestMedia(mediaItem)
                                fragment!!.updateData()
                            }
                            mediaFolderSelectPopup?.addAllCount()

                            runOnUiThread {
                                val locate = intArrayOf(ScreenUtils.getScreenWidth(this@SelectClipActivity) / 2, ScreenUtils.getScreenHeight(this@SelectClipActivity) / 2)
                                addView(mediaItem, locate)
                            }
                            videoUri = null
                        }
                    })
                    mMediaScannerConnection!!.connect()
                }
            }
            ContactUtils.REQUEST_SELECT_TO_EDIT -> if (!isAddClip) {
                mediaListCopy = MediaDataRepository.getInstance().dataOperateCopy
            }
            ContactUtils.REQUEST_SELECT_SINGLE_ITEM -> if (resultCode == RESULT_OK) {
                //异或，表示与之前的状态不一样，即选中状态被改变
                val selected = data!!.getBooleanExtra(PreviewSingleMediaItemActivity.MEDIA_ITEM_SELECTED, false)
//                if (!selected xor (previewClickCoordinate != null)) {
//                    延迟到onResume之后再进行操作，避免onResume中setData覆盖修改
                lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    fun run() {
//                            onMediaItemSelectingChanged(mPreviewItem, previewClickCoordinate)
//                            lifecycle.removeObserver(this)

                        fragment!!.updateData()
                    }
                })
//                } else {
//                    lifecycle.addObserver(object : LifecycleObserver {
//                        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//                        fun run() {
//                            //刷新裁剪数据
//                            //不在adapter中，手动刷新界面
//                            if (!mBottomContentRecyclerAdapter!!.flash(mPreviewItem)) {
//                                updateMediaItemIndex(mPreviewItem.getId(), mPreviewItem.mediaType)
//                            }
//                            lifecycle.removeObserver(this)
//                        }
//                    })
//                }
            }
        }
    }

    // 另一个App删除的文件后，回来还能搜索文件，但是显示为白块，应该是插入数据库里面数据没有删除掉，导致路径存在，但实际文件已经不存在了！！！
    private var mMediaScannerConnection: MediaScannerConnection? = null
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (PermissionUtil.REQUEST_CAMERA == requestCode) {
            if (videoOrPhoto == 0) {
                openCamera()
            } else if (videoOrPhoto == 1) {
                openVideoCamera()
            }
        } else {
            super.onPermissionsGranted(requestCode, perms)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (PermissionUtil.REQUEST_CAMERA == requestCode) {
            showCameraTip()
        } else {
            super.onPermissionsDenied(requestCode, perms)
        }
    }

    private fun showCameraTip() {
        val params = DialogParams.obtainDialogParams(this)
        params.msg = getString(R.string.camera_permission_ask_again)
        AppSettingsDialog.Builder(this).setDialogParams(params).setRequestCode(REQUEST_CAMERA_PREMISSION).build().show()
    }

    /**
     * 保存本地草稿
     */
    private fun saveLocalLoading() {
        lifecycleScope.launch {
            showLoading("")
            checkFFmpegBackround()
            withContext(Dispatchers.IO) {
                withTimeout(6000L) {
                    MediaDataRepository.getInstance().save2Local()
                    //取消当前预处理的任务
                    ThreadPoolManager.getThreadPool().shutDown()
                    ThreadPoolManager.getThreadPool().release()

                }
            }
            withContext(Dispatchers.Default) {
                MediaDataRepository.getInstance().recyleBitmaps()
                MediaDataRepository.getInstance().onDestroyCurrentThread()
                LogUtils.v("test", "datalist.size:inner..." + MediaDataRepository.getInstance().dataOperate.size);
            }
            LogUtils.v("test", "datalist.size:" + MediaDataRepository.getInstance().dataOperate.size);
            endLoading()
            T.showShort(this@SelectClipActivity, getString(R.string.draft_save))
            finish()
        }
    }

    /**
     * 如果视频比较长，大于10mn则终止音频的抽取
     */
    private fun checkFFmpegBackround() {
        if (ObjectUtils.isEmpty(MediaDataRepository.getInstance().dataOperate)) {
            return
        }
        for (m: MediaItem in MediaDataRepository.getInstance().dataOperate) {
            if (m is VideoMediaItem) {
                if (!ObjectUtils.isEmpty(m.extractTaskId) && m.getFinalDuration() > 600000) {
                    FfmpegBackgroundHelper.getInstance().removeTask(m.extractTaskId)
                }
            }
        }
    }

    override fun checkMediaDataGC(savedInstanceState: Bundle?) {}

    /**
     * 获取素材在选中片段中的顺序位置
     *
     * @param mediaItem 元素
     * @return null 表示不在选择集合中, 返回下标位置
     */
    override fun getMediaItemSelectedIndex(mediaItem: MediaEntity): Int? {
        var result: Int? = -1
        val mediaType = if (mediaItem.getType() == MediaEntity.TYPE_VIDEO) MediaType.VIDEO else MediaType.PHOTO
        result = getMediaItemSelectedIndex(mediaType, mediaItem.getId())
        if (result == null && isAddClip) {
            for ((i, item: MediaItem) in mBottomContentRecyclerAdapter!!.datas.withIndex()) {
                if (PathStringComparator.isPathStringEquals(item.path, mediaItem.getPath())) {
                    return i
                }
            }
        }
        return if ((Integer.valueOf(-1) == result)) {
            null
        } else result
    }

    override fun getMediaItemCutDuration(mediaItem: MediaEntity): DurationInterval? {
        val index: Int = getMediaItemSelectedIndex(mediaItem) ?: return null
        return if (index < 0 || index >= mBottomContentRecyclerAdapter!!.datas.size) {
            null
        } else mBottomContentRecyclerAdapter!!.datas[index].videoCutInterval
    }

    override fun getMediaItemCutPath(mediaEntity: MediaEntity): String? {
        val index: Int? = getMediaItemSelectedIndex(mediaEntity)
        return if (index == null) null else mBottomContentRecyclerAdapter!!.datas[index].trimPath
    }

    /**
     * 获取素材在选中片段中的顺序位置
     *
     * @param mediaType
     * @param id
     * @return null 表示不在选择集合中, 返回下标位置
     */
    override fun getMediaItemSelectedIndex(mediaType: MediaType, id: Int): Int? {
        var id = id
        if (mediaType == MediaType.VIDEO) {
            id = id or Int.MIN_VALUE
        }
        return mBottomContentRecyclerAdapter!!.idToIndex[id]
    }

    /**
     * 更新fragment中mediaItem的顺序和裁剪时间
     *
     * @param mediaItem
     */
    override fun updateMediaItemIndex(mediaItem: MediaItem) {
        updateMediaItemIndex(mediaItem.id, mediaItem.mediaType)
    }

    override fun updateCopiedMediaItemSelected(path: String) {
        fragment!!.updateCopiedSelectedItemIndex(path)
    }

    /**
     * 更新fragment中mediaItem的顺序和裁剪时间
     *
     * @param mediaId   mediaId
     * @param mediaType mediaType
     */
    override fun updateMediaItemIndex(mediaId: Int, mediaType: MediaType) {
        //更新对应fragment中的数字显示
        if (fragment != null) {
            if (fragment is PhotoFragment) {
                (fragment as PhotoFragment).updateSelectedItemIndex(mediaId)
            }
            if (fragment is SelectMediaFragment) {
                if (mediaType == MediaType.PHOTO) {
                    if ((fragment as SelectMediaFragment).photoFragment != null) {
                        (fragment as SelectMediaFragment).photoFragment.updateSelectedItemIndex(mediaId)
                    }
                    if ((fragment as SelectMediaFragment).imageMaterialFragment != null) {
                        (fragment as SelectMediaFragment).imageMaterialFragment.updateSelectedItemIndex(mediaId)
                    }
                }
                if (mediaType == MediaType.VIDEO) {
                    if ((fragment as SelectMediaFragment).videoFragment != null) {
                        (fragment as SelectMediaFragment).videoFragment.updateSelectedItemIndex(mediaId)
                    }
                }
                if ((fragment as SelectMediaFragment).dirFragment != null) {
                    (fragment as SelectMediaFragment).dirFragment.updateSelectedItemIndex(mediaId, mediaType)
                }
            }
        }
    }

    /**
     * 设置文件夹提示显示
     *
     * @param visible
     */
    fun setFolderVisible(visible: Boolean) {
        if (visible) {
            binding.llFolder.root.visibility = View.VISIBLE
        } else {
            binding.llFolder.root.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val TAG = "SelectClipActivity"
        private const val TAKE_PHOTO = 100
        private const val TAKE_VIDEO = 200
        const val ACTION_SELECT = "SELECT_ACTION"
        const val ACTION_REMOVE = "ACTION_REMOVE"

        /**
         * 分组模式
         */
        const val MODE_LIST = 0

        /**
         * 表格模式
         */
        const val MODE_GRID = 1

        /**
         * 静态方法
         * 发送消息
         *
         * @param activity
         * @param intent
         */
        fun sendSelectMediaBroadCast(activity: SelectClipActivity, intent: Intent?) {
            LocalBroadcastManager.getInstance(activity).sendBroadcast((intent)!!)
        }

        @JvmField
        var vmOwner: ViewModelStoreOwner? = null;

    }
}