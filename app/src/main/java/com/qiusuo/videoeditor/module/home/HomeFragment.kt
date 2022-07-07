package com.qiusuo.videoeditor.module.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.luck.picture.lib.PictureSelectorPreviewFragment
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.FragmentInjectManager
import com.luck.picture.lib.basic.PictureSelectionModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.*
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnPreviewInterceptListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.loader.GlideEngine
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.utils.ToastUtils
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.ui.adapter.HomeFunAdapter
import com.qiusuo.videoeditor.ui.widgegt.RecyclerItemDecoration

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()
    val animationMode = AnimationType.DEFAULT_ANIMATION
    lateinit var launcher:ActivityResultLauncher<Intent>

    companion object {
        const val SPAN_COUNT = 2;
        const val REQUEST_CODE=100;
        const val CALLBACK_CODE=200;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher=createActivityResultLauncher()
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
        openGallery()
    }


    override fun getPageName(): String {
        return PageName.HOME
    }

    fun openGallery(){
        // 进入相册
        val selectionModel: PictureSelectionModel = PictureSelector.create(context)
            .openGallery(SelectMimeType.TYPE_IMAGE)
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
        //设置选中照片的集合页
//            .setSelectedData(mAdapter.getData())
        selectionModel.forResult(launcher);
    }


    private fun forSelectResult(model: PictureSelectionModel) {
         model.forResult(createActivityResultLauncher())
    }


    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
     fun createActivityResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    val resultCode = result?.resultCode
                }
            })
    }


    /**
     * 选择结果
     */
    private class MeOnResultCallbackListener : OnResultCallbackListener<LocalMedia?> {
        override fun onResult(result: java.util.ArrayList<LocalMedia?>) {
        }

        override fun onCancel() {
            Log.i(javaClass.name, "PictureSelector Cancel")
        }
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
