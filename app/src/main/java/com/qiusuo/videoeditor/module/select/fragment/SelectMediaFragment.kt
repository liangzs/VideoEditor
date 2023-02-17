package com.qiusuo.videoeditor.module.select.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.databinding.SelecteMediaFragmentLayoutBinding
import com.qiusuo.videoeditor.module.select.LoadMediaViewModel
import com.qiusuo.videoeditor.ui.widgegt.guide.util.ViewUtils

/**
 * Created by DELL on 2019/5/5.
 */
class SelectMediaFragment : BaseFragment<SelecteMediaFragmentLayoutBinding>(SelecteMediaFragmentLayoutBinding::inflate), SelectFragment {

    var dirFragment: SelectMediaFromDirFragment? = null
        private set
    var photoFragment: PhotoFragment? = null
        private set
    var videoFragment: VideoFragment? = null
        private set

    /**
     * 媒体模式。0-混合，1-图片，2-视频
     */
    private var mediaMode = 0

    /**
     * fragment集合
     */
    var listFragment: MutableList<Fragment>? = null

    override fun initView() {
        mediaMode = requireArguments().getInt(MODE_TAG, -1)
        createFragment()
    }

    /**
     * 主题和无主题显示不同
     */
    private fun createFragment() {
        listFragment = ArrayList()
        val titles = arrayOfNulls<String>(3)
        if (mediaMode == LoadMediaViewModel.MIX) {
            dirFragment = SelectMediaFromDirFragment.newInstance(-1)
            dirFragment!!.showSelect()
            //            imageMaterialFragment = new ImageMaterialFragment();
            photoFragment = PhotoFragment()
            photoFragment!!.showSelect()
            videoFragment = VideoFragment()
            videoFragment!!.showSelect()
            listFragment!!.add(dirFragment!!)
            listFragment!!.add(photoFragment!!)
            listFragment!!.add(videoFragment!!)
            //            listFragment.add(imageMaterialFragment);
            titles[0] = resources.getString(R.string.all)
            titles[1] = resources.getString(R.string.photo)
            titles[2] = resources.getString(R.string.video)
            //            titles[3] = getString(R.string.material);
            viewBinding.viewPager.setOffscreenPageLimit(4)
        } else if (mediaMode == LoadMediaViewModel.PHOTO) {
            titles[0] = resources.getString(R.string.all)
            //            titles[1] = getResources().getString(R.string.material);
            titles[0] = resources.getString(R.string.photo)
            dirFragment = SelectMediaFromDirFragment.newInstance(-1)
            dirFragment!!.showSelect()
            listFragment!!.add(dirFragment)
            photoFragment = PhotoFragment()
            photoFragment!!.showSelect()
            listFragment!!.add(photoFragment!!)
            viewBinding.viewPager.setOffscreenPageLimit(2)
        } else {
            dirFragment = SelectMediaFromDirFragment.newInstance(-1)
            dirFragment!!.showSelect()
            videoFragment = VideoFragment()
            videoFragment!!.showSelect()
            titles[0] = resources.getString(R.string.all)
            titles[1] = resources.getString(R.string.video)
            listFragment!!.add(dirFragment!!)
            listFragment!!.add(videoFragment!!)
            viewBinding.viewPager.setOffscreenPageLimit(2)
        }
        val titlePagerViewAdapter = TitlePagerViewAdapter(childFragmentManager, listFragment, titles)
        viewBinding.viewPager.setAdapter(titlePagerViewAdapter)
        viewBinding.tablayout.setupWithViewPager(viewBinding.viewPager)
        viewBinding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //AppBus.get().post(SelectFragmentEvent(tab.position))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        ViewUtils.disableTabLayoutToast(viewBinding.tablayout)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun updateData() {
        if (dirFragment != null) {
            dirFragment!!.updateData()
        }
        if (photoFragment != null) {
            photoFragment!!.updateData()
        }
        if (videoFragment != null) {
            videoFragment!!.updateData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 切
     *
     * @param position
     */
    override fun switchAdapter(position: Int) {
        if (dirFragment != null) {
            dirFragment!!.switchAdapter(position)
        }
        if (videoFragment != null) {
            videoFragment!!.switchAdapter(position)
        }
        if (photoFragment != null) {
            photoFragment!!.switchAdapter(position)
        }
    }

    /**
     * 显示日期全选
     */
    var showSelect = false
    override fun showSelect() {
        showSelect = true
    }

    override fun updateDateCount(date: String, mediaType: Int, increase: Boolean) {
        if (dirFragment != null) {
            dirFragment!!.updateDateCount(date, mediaType, increase)
        }
        if (videoFragment != null && mediaType == MediaEntity.TYPE_VIDEO) {
            videoFragment!!.updateDateCount(date, mediaType, increase)
        }
        if (photoFragment != null && mediaType == MediaEntity.TYPE_IMAGE) {
            photoFragment!!.updateDateCount(date, mediaType, increase)
        }
    }

    override fun clearSelectCount() {
        if (dirFragment != null) {
            dirFragment!!.clearSelectCount()
        }
        if (videoFragment != null) {
            videoFragment!!.clearSelectCount()
        }
        if (photoFragment != null) {
            photoFragment!!.clearSelectCount()
        }
    }

    override fun updateSections() {
        if (dirFragment != null) {
            dirFragment!!.updateSections()
        }
        if (photoFragment != null) {
            photoFragment!!.updateSections()
        }
        if (videoFragment != null) {
            videoFragment!!.updateSections()
        }
    }

    override fun updateSelect() {
        if (dirFragment != null) {
            dirFragment!!.updateSelect()
        }
        if (videoFragment != null) {
            videoFragment!!.updateSelect()
        }
        if (photoFragment != null) {
            photoFragment!!.updateSelect()
        }
    }

    override fun updateCopiedSelectedItemIndex(path: String) {
        if (dirFragment != null) {
            dirFragment!!.updateCopiedSelectedItemIndex(path)
        }
        if (videoFragment != null) {
            videoFragment!!.updateCopiedSelectedItemIndex(path)
        }
        if (photoFragment != null) {
            photoFragment!!.updateCopiedSelectedItemIndex(path)
        }
    }

    override fun recountSections() {
        if (dirFragment != null) {
            dirFragment!!.recountSections()
        }
        if (videoFragment != null) {
            videoFragment!!.recountSections()
        }
        if (photoFragment != null) {
            photoFragment!!.recountSections()
        }
    }


    companion object {
        /**
         * 媒体模式标签
         */
        private const val MODE_TAG = "mediaMode"
        fun newInstance(mediaMode: Int): SelectMediaFragment {
            LogUtils.v("test", "newInstance..")
            val fragment = SelectMediaFragment()
            val bundle = Bundle()
            bundle.putInt(MODE_TAG, mediaMode)
            fragment.arguments = bundle
            return fragment
        }
    }
}