package com.qiusuo.videoeditor.module.edit

import android.os.Bundle
import com.ijoysoft.mediasdk.module.entity.DoodleItem
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.playControl.MediaConfig
import com.ijoysoft.mediasdk.view.MediaPreviewView
import com.qiusuo.videoeditor.base.BaseActivity
import com.qiusuo.videoeditor.common.data.MediaDataRepository
import com.qiusuo.videoeditor.databinding.ActivityEditorBinding

class EditorActivity : BaseActivity<ActivityEditorBinding>(ActivityEditorBinding::inflate) {
    lateinit var mediaPreviewView: MediaPreviewView ;
    lateinit var mediaList:List<MediaItem>;
    lateinit var mDoodleItems:List<DoodleItem>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initData() {
        mediaList = MediaDataRepository.getInstance().getDataOperate()
        mDoodleItems = MediaDataRepository.getInstance().getAllDoodle()
//        val isfade: Boolean = SharedPreferencesUtil
//            .getBoolean(ContactUtils.TAG_MUSIC_FADE + MediaDataRepository.getInstance().getProjectID(), false)
//        mCurRatioType = RatioType.getRatioType(SharedPreferencesUtil
//            .getInt(ContactUtils.EDIT_RATIO_SELECT + MediaDataRepository.getInstance().getProjectID(), 0))
//        val mediaConfig = MediaConfig.Builder().setFading(isfade).setRatioType(mCurRatioType).build()
//        if (mCurRatioType == RatioType.NONE) {
//            mediaConfig.ratioType = MediaDataRepository.getInstance().calcPreviewRatio()
//        }
//        val color: Int = SharedPreferencesUtil.getInt(
//            ContactUtils.EDIT_BACKGROUND_COLOR_VALUE + MediaDataRepository.getInstance().getProjectID(), 0)
//        if (color != 0) {
//            mediaConfig.rgba = "#" + Integer.toHexString(color)
//        }
        mediaPreviewView.setDataSource(mediaList, mDoodleItems, true, MediaConfig.Builder().build(), true, true)
    }
}