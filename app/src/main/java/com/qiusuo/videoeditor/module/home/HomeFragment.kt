package com.qiusuo.videoeditor.module.home

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.luck.picture.lib.PictureSelectorPreviewFragment
import com.luck.picture.lib.basic.FragmentInjectManager
import com.luck.picture.lib.basic.PictureSelectionModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.config.SelectLimitType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnPreviewInterceptListener
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.loader.GlideEngine
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.utils.ToastUtils
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.view.adapter.HomeFunAdapter
import com.qiusuo.videoeditor.widgegt.RecyclerItemDecoration

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()

    companion object {
        const val SPAN_COUNT = 2;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView();
    }

    private fun initView() {
        var gridlayout=GridLayoutManager(context, SPAN_COUNT);
        gridlayout.orientation=GridLayoutManager.HORIZONTAL;
        gridlayout.isAutoMeasureEnabled=true
        viewBinding.rvMainFunc.setLayoutManager(gridlayout)
        viewBinding.rvMainFunc.addItemDecoration(RecyclerItemDecoration(
            activity.resources.getDimensionPixelOffset(R.dimen.item_padding), true, true))
        viewBinding.rvMainFunc.adapter = HomeFunAdapter { position: Int -> funItemClick(position) }

        viewBinding.flCreate.setOnClickListener {

        }
    }


    fun funItemClick(position: Int) {

    }


    override fun getPageName(): String {
        return PageName.HOME
    }

    fun openGallery(){
        // 进入相册

        // 进入相册
        val selectionModel: PictureSelectionModel = PictureSelector.create(context)
            .openGallery(SelectMimeType.TYPE_ALL)
            .setSelectorUIStyle( PictureSelectorStyle())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setSelectLimitTipsListener(MeOnSelectLimitTipsListener())
            .setPreviewInterceptListener(MeOnPreviewInterceptListener())
            .setSelectionMode( SelectModeConfig.MULTIPLE)
            .setLanguage(LanguageConfig.ENGLISH)
            .setQuerySortOrder( MediaStore.MediaColumns.DATE_MODIFIED + " ASC" )
            .isDisplayTimeAxis(true)
            .isPageStrategy(true)
            .isOriginalControl(true)
            .isDisplayCamera(true)
            .isOpenClickSound(true)
            .isFastSlidingSelect(true) //.setOutputCameraImageFileName("luck.jpeg")
            //.setOutputCameraVideoFileName("luck.mp4")
            .isWithSelectVideoImage(true)
            .isPreviewFullScreenMode(true)
            .isPreviewZoomEffect(true)
            .setRecyclerAnimationMode(animationMode)
            .isGif(true)
            .setSelectedData(mAdapter.getData())
        forSelectResult(selectionModel)
    }

   /**
    * 拦截自定义提示
    */
   inner class MeOnSelectLimitTipsListener : OnSelectLimitTipsListener
    {
        override fun onSelectLimitTips(context: Context?, config: PictureSelectionConfig?, limitType: Int): Boolean {
            if (limitType == SelectLimitType.SELECT_NOT_SUPPORT_SELECT_LIMIT) {
                ToastUtils.showToast(context, "暂不支持的选择类型")
                return true
            }
            return false
        }
    }


    /**
     * 自定义预览
     *
     * @return
     */
    private class MeOnPreviewInterceptListener : OnPreviewInterceptListener {
        override fun onPreview(context: Context, position: Int, totalNum: Int, page: Int, currentBucketId: Long, currentAlbumName: String, isShowCamera: Boolean, data: ArrayList<LocalMedia>, isBottomPreview: Boolean) {
            val previewFragment: PictureSelectorPreviewFragment = PictureSelectorPreviewFragment.newInstance()
            previewFragment.setInternalPreviewData(isBottomPreview, currentAlbumName, isShowCamera,
                position, totalNum, page, currentBucketId, data)
            FragmentInjectManager.injectFragment(context as FragmentActivity, PictureSelectorPreviewFragment.TAG, previewFragment)
        }
    }

}
