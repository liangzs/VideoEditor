package com.qiusuo.videoeditor.module.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.module.select.PictureSelectorSupporterActivity
import com.qiusuo.videoeditor.module.select.config.PictureSelectionConfig
import com.qiusuo.videoeditor.module.select.config.SelectMimeType
import com.qiusuo.videoeditor.module.select.style.PictureSelectorStyle
import com.qiusuo.videoeditor.ui.adapter.HomeFunAdapter
import com.qiusuo.videoeditor.ui.widgegt.RecyclerItemDecoration

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()
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
        PictureSelectionConfig.selectorStyle=PictureSelectorStyle()
        // 进入相册
//        val selectionModel: PictureSelectionModel = PictureSelector.create(context)
//            .openGallery(SelectMimeType.TYPE_IMAGE)
//            .setSelectorUIStyle( PictureSelectorStyle())
//            .setImageEngine(GlideEngine.createGlideEngine())
//            .setSelectLimitTipsListener(MeOnSelectLimitTipsListener())
//            .setPreviewInterceptListener(MeOnPreviewInterceptListener())
//            .setSelectionMode( SelectModeConfig.MULTIPLE)
//            .setLanguage(LanguageConfig.ENGLISH`)
//            .setQuerySortOrder( MediaStore.MedvbiaColumns.DATE_MODIFIED + " ASC" )
//            .isDisplayTimeAxis(true)
//            .isPageStrategy(true)
//            .isOriginalControl(true)
//            .isDisplayCamera(true)
//            .isOpenClickSound(true)
//            .isFastSlidingSelect(true) //.setOutputCameraImageFileName("luck.jpeg")
//            //.setOutputCameraVideoFileName("luck.mp4")
//            .isWithSelectVideoImage(true)
//            .isPreviewFullScreenMode(true)
//            .isPreviewZoomEffect(true)
//            .setRecyclerAnimationMode(animationMode)
//            .isGif(true)
//            .setSelectFilterListener(object :OnSelectFilterListener{
//                override fun onSelectFilter(media: LocalMedia?): Boolean {
//                    MediaDataRepository.getInstance().addMediaItem(media,false,false)
//                    return false
//                }
//            })


        val intent = Intent(activity, PictureSelectorSupporterActivity::class.java)
        launcher.launch(intent)


        //设置选中照片的集合页
//            .setSelectedData(mAdapter.getData())
//        selectionModel.forResult(launcher);
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





}
