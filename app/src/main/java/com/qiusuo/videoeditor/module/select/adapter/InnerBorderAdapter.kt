package com.ijoysoft.videoeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ijoysoft.download.DownloadListener
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.InnerBorder
import com.ijoysoft.videoeditor.R
import com.ijoysoft.videoeditor.activity.EditorActivity
import com.ijoysoft.videoeditor.base.BaseActivity
import com.ijoysoft.videoeditor.model.download.DownloadHelper
import com.ijoysoft.videoeditor.model.download.DownloadPath
import com.ijoysoft.videoeditor.utils.AndroidUtil
import com.ijoysoft.videoeditor.utils.ImageHelper
import com.ijoysoft.videoeditor.view.dialog.DownloadADDialog

/**
 * 转场系列适配器
 */
class InnerBorderAdapter(mActivity: BaseActivity) :
    DownloadableAdapter<InnerBorder, InnerBorderAdapter.InnerBorderHolder>(mActivity, true) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerBorderHolder {
        return InnerBorderHolder(layoutInflater.inflate(R.layout.item_innerborder, parent, false))
    }


    override fun onBindViewHolder(holder: InnerBorderHolder, position: Int, payloads: List<Any?>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.refreshCheckState()
        }
    }


    override val currentPosition
        get() = list.indexOf(ConstantMediaSize.innerBorder)

    /**
     * 下载广告弹窗
     */
    private var downDialog: DownloadADDialog? = null

    inner class InnerBorderHolder(itemView: View) : DownloadHolder(itemView),
        View.OnClickListener {


        /**
         * 一定要用get() = 要不然父类先初始化时获取不到子类的属性
         */
        override val previewViewId: Int
            get() = R.id.iv_innerborder_icon

        override val selectViewId: Int
            get() = R.id.iv_innerborder_select


        override val selected: Boolean
            get() = ConstantMediaSize.innerBorder == entity

        override val imageViewTag: Int
            get() = entity!!.id


        override val isLocal: Boolean
            get() = entity!!.size == 0


        override val size: Long
            get() = entity!!.size.toLong()


        override val previewPath: String
            get() = if (isLocal) {
                entity!!.getAbsolutePath(RatioType.NONE) ?: ""
            } else {
                entity!!.getPath(RatioType.NONE, DownloadPath.INNERBORDER_REQUEST)
            }

        override fun addAllUrl() {
            val urls = ArrayList<String>()
            entity!!.ratios.forEach {
                urls.add(entity!!.getPath(it, DownloadPath.INNERBORDER_REQUEST)!!)
            }
            this.urls.addAll(urls)
        }

        override fun addAllSavePath() {
            val saves = ArrayList<String>()
            entity!!.ratios.forEach {
                saves.add(entity!!.getPath(it, DownloadHelper.INNERBORDER_DOWNLOAD_PATH)!!)
            }
            this.savePaths.addAll(saves)
        }

        override fun setDownloadTag() {
            downloadTag = "innerBorder" + entity!!.id
        }

        override fun onReadyToAction() {
            setInnerBorder(list[if (hasPlaceholder) adapterPosition - 1 else adapterPosition]!!)
        }

        override fun placeholderAction() {
//            val intent = Intent(mActivity, MaterialActivity::class.java);
//            intent.putExtra("index", 1);
//            mActivity.startActivityForResult(
//                intent, ContactUtils.REQUEST_CHANGE_BORDER
//            )

            (mActivity as EditorActivity).innerBorderLauncher?.launch(Unit)
        }

        override fun uiNotifyAction() {}

        override fun onDownloadSuccess() {}

        /**
         * 重写加载预览图方法，针对无边框设置禁止符
         */
        override fun loadPreview(imageView: ImageView) {
            imageView.setBackgroundDrawable(null)
            imageView.setTag(R.id.iv_theme_icon, imageViewTag)
            if (entity == InnerBorder.NONE) {
//                previewView.setBackgroundResource(R.drawable.shape_theme_more_bg)
//                previewView.setImageResource(R.drawable.vector_vm_drawable_frame_none)
            } else {
                ImageHelper.loadImageByPath(
                    mActivity,
                    previewPath,
                    imageView,
                    R.id.iv_theme_icon,
                    imageViewTag,
                    isLocal
                )
            }
        }


        /**
         * 刷新选中状态
         * 未选中的使用白框包围
         */
        override fun refreshCheckState() {
            if (entity == null) {
                selectView.visibility = View.GONE
//                selectView.setImageResource(R.drawable.shape_theme_item_selected)
            } else {
                selectView.visibility = if (selected) View.VISIBLE else View.GONE
//                if (selected) {
//                    selectView.setImageResource(R.drawable.shape_theme_item_selected)
//                } else {
//                    selectView.setImageResource(R.drawable.shape_select_border)
//                }
            }
        }

        override fun onClick(v: View) {
            //复杂操作防止连续点击
            if (AndroidUtil.chekcFastClick()) {
                return
            }
            super.onClick(v)
        }


        override fun downloadAction(
            tag: String?,
            urls: List<String?>?,
            savePaths: List<String?>?,
            totalSize: Long,
            listener: DownloadListener?
        ) {


            if (downDialog == null) {
                downDialog = DownloadADDialog(mActivity, previewPath)
            }
            downDialog?.tag = tag;
            downDialog?.setGlideModel(previewPath)
            downDialog?.show()
            DownloadHelper.load(
                tag,
                urls,
                savePaths,
                totalSize,
                object : DownloadListener {
                    override fun onDownloadStart(s: String) {
                        downDialog?.onDownloadStart(s)
                        this@InnerBorderHolder.onDownloadStart(s)
                    }

                    override fun onDownloadEnd(s: String, i: Int) {
                        downDialog?.onDownloadEnd(s, i)
                        this@InnerBorderHolder.onDownloadEnd(s, i)
                    }

                    override fun onDownloadProgress(s: String, l: Long, l1: Long) {
                        downDialog?.onDownloadProgress(s, l, l1)
                        this@InnerBorderHolder.onDownloadProgress(s, l, l1)
                    }
                })
            return
        }
    }

    override fun clearSelectAction() {
        setInnerBorder(InnerBorder.NONE)
    }

    fun setInnerBorder(innerBorder: InnerBorder) {
        val eActivity = (mActivity as EditorActivity)
        eActivity.showLoading("")
        //是否重置播放状态
        eActivity.runOnUiThread {
            eActivity.pause()
        }
        //设置转场
        ConstantMediaSize.innerBorder = innerBorder
        eActivity.mediaPreviewView.queueEvent {
            //使用gl线程执行渲染相关操作
            eActivity.mediaPreviewView.changeInnerBorder(innerBorder)
            eActivity.runOnUiThread {
                //恢复播放
                eActivity.play()
                eActivity.endLoading()
                notifyCheckChanged()
                eActivity.ivThemeNone?.isSelected = innerBorder == InnerBorder.NONE
            }
        }
    }


    /**
     * 下载对象清楚引用
     */
    fun destory() {
        downDialog?.dismiss()
    }
}