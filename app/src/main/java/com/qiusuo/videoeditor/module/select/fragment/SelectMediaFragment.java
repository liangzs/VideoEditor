package com.qiusuo.videoeditor.module.select.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.qiusuo.videoeditor.base.BaseFragment;
import com.qiusuo.videoeditor.databinding.SelecteMediaFragmentLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2019/5/5.
 */
public class SelectMediaFragment extends BaseFragment<SelecteMediaFragmentLayoutBinding> implements SelectFragment {
    @NonNull
    @Override
    public SelecteMediaFragmentLayoutBinding createRootBinding(@NonNull LayoutInflater inflater) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        return SelecteMediaFragmentLayoutBinding.inflate(inflater);
    }


    private SelectMediaFromDirFragment dirFragment;
    private PhotoFragment mPhotoFragment;
    private VideoFragment mVideoFragment;
    private ImageMaterialFragment imageMaterialFragment;


    /**
     * 媒体模式标签
     */
    private static final String MODE_TAG = "mediaMode";


    /**
     * 媒体模式。0-混合，1-图片，2-视频
     */
    private int mediaMode;


    /**
     * fragment集合
     */
    List<Fragment> listFragment;


    public static SelectMediaFragment newInstance(int mediaMode) {
        LogUtils.v("test", "newInstance..");
        SelectMediaFragment fragment = new SelectMediaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MODE_TAG, mediaMode);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onBindView(View root, LayoutInflater inflater, Bundle savedInstanceState) {
        mediaMode = getArguments().getInt(MODE_TAG, -1);
        createFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    /**
     * 主题和无主题显示不同
     */
    private void createFragment() {
        listFragment = new ArrayList<>();
        String[] titles = new String[3];
        if (mediaMode == LoadMediaViewModel.MIX) {
            dirFragment = SelectMediaFromDirFragment.newInstance(-1);
            dirFragment.showSelect();
//            imageMaterialFragment = new ImageMaterialFragment();
            mPhotoFragment = new PhotoFragment();
            mPhotoFragment.showSelect();
            mVideoFragment = new VideoFragment();
            mVideoFragment.showSelect();
            listFragment.add(dirFragment);
            listFragment.add(mPhotoFragment);
            listFragment.add(mVideoFragment);
//            listFragment.add(imageMaterialFragment);
            titles[0] = getResources().getString(R.string.all);
            titles[1] = getResources().getString(R.string.photo);
            titles[2] = getResources().getString(R.string.video);
//            titles[3] = getString(R.string.material);
            binding.viewPager.setOffscreenPageLimit(4);
        } else if (mediaMode == LoadMediaViewModel.PHOTO) {
            titles[0] = getResources().getString(R.string.all);
//            titles[1] = getResources().getString(R.string.material);
            titles[0] = getResources().getString(R.string.photo);
            dirFragment = SelectMediaFromDirFragment.newInstance(-1);
            dirFragment.showSelect();
            listFragment.add(dirFragment);
            mPhotoFragment = new PhotoFragment();
            mPhotoFragment.showSelect();
            listFragment.add(mPhotoFragment);
//            imageMaterialFragment = new ImageMaterialFragment();
//            listFragment.add(imageMaterialFragment);
            binding.viewPager.setOffscreenPageLimit(2);
        } else {
            dirFragment = SelectMediaFromDirFragment.newInstance(-1);
            dirFragment.showSelect();
            mVideoFragment = new VideoFragment();
            mVideoFragment.showSelect();
            titles[0] = getResources().getString(R.string.all);
            titles[1] = getResources().getString(R.string.video);
            listFragment.add(dirFragment);
            listFragment.add(mVideoFragment);
            binding.viewPager.setOffscreenPageLimit(2);
        }

        TitlePagerViewAdapter titlePagerViewAdapter = new TitlePagerViewAdapter(getChildFragmentManager(), listFragment, titles);
        binding.viewPager.setAdapter(titlePagerViewAdapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                AppBus.get().post(new SelectFragmentEvent(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewUtils.INSTANCE.disableTabLayoutToast(binding.tablayout);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void updateData() {
        if (dirFragment != null) {
            dirFragment.updateData();
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.updateData();
        }
        if (mVideoFragment != null) {
            mVideoFragment.updateData();
        }
    }


    public PhotoFragment getPhotoFragment() {
        return mPhotoFragment;
    }

    public SelectMediaFromDirFragment getDirFragment() {
        return dirFragment;
    }

    public VideoFragment getVideoFragment() {
        return mVideoFragment;
    }

    public ImageMaterialFragment getImageMaterialFragment() {
        return imageMaterialFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 切
     *
     * @param position
     */
    @Override
    public void switchAdapter(int position) {
        if (dirFragment != null) {
            dirFragment.switchAdapter(position);
        }
        if (mVideoFragment != null) {
            mVideoFragment.switchAdapter(position);
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.switchAdapter(position);
        }
    }

    /**
     * 显示日期全选
     */
    boolean showSelect = false;

    @Override
    public void showSelect() {
        showSelect = true;
    }

    @Override
    public void updateDateCount(String date, int mediaType, boolean increase) {
        if (dirFragment != null) {
            dirFragment.updateDateCount(date, mediaType, increase);
        }
        if (mVideoFragment != null && mediaType == MediaEntity.TYPE_VIDEO) {
            mVideoFragment.updateDateCount(date, mediaType, increase);
        }
        if (mPhotoFragment != null && mediaType == MediaEntity.TYPE_IMAGE) {
            mPhotoFragment.updateDateCount(date, mediaType, increase);
        }
    }

    @Override
    public void clearSelectCount() {
        if (dirFragment != null) {
            dirFragment.clearSelectCount();
        }
        if (mVideoFragment != null) {
            mVideoFragment.clearSelectCount();
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.clearSelectCount();
        }
    }

    @Override
    public void updateSections() {
        if (dirFragment != null) {
            dirFragment.updateSections();
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.updateSections();
        }
        if (mVideoFragment != null) {
            mVideoFragment.updateSections();
        }
    }

    @Override
    public void updateSelect() {
        if (dirFragment != null) {
            dirFragment.updateSelect();
        }
        if (mVideoFragment != null) {
            mVideoFragment.updateSelect();
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.updateSelect();
        }
    }

    @Override
    public void updateCopiedSelectedItemIndex(String path) {
        if (dirFragment != null) {
            dirFragment.updateCopiedSelectedItemIndex(path);
        }
        if (mVideoFragment != null) {
            mVideoFragment.updateCopiedSelectedItemIndex(path);
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.updateCopiedSelectedItemIndex(path);
        }
    }

    @Override
    public void recountSections() {
        if (dirFragment != null) {
            dirFragment.recountSections();
        }
        if (mVideoFragment != null) {
            mVideoFragment.recountSections();
        }
        if (mPhotoFragment != null) {
            mPhotoFragment.recountSections();
        }
    }
}
