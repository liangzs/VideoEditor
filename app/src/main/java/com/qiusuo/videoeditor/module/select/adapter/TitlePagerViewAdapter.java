package com.qiusuo.videoeditor.module.select.adapter;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.List;

/**
 * Created by wh on 2019/4/27.
 * TODO: FragmentStatePagerAdapter要切换到ViewPager2，状态保存
 */
public class TitlePagerViewAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList = null;
    private String[] titles;

    public TitlePagerViewAdapter(FragmentManager mFragmentManager, List<Fragment> fragmentList, String[] titles) {
        super(mFragmentManager);
        this.titles = titles;
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        LogUtils.v("test", "getItem:" + i);
        Fragment fragment = null;
        if (i < mFragmentList.size()) {
            fragment = mFragmentList.get(i);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }


    /*
     * 解决在切换时会销毁fragment问题
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // super.destroyItem(container, position, object);
//         super.destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && position < titles.length)
            return titles[position];
        return null;
    }

    /**
     * 重新加载
     * @return
     */
    @Override
    public Parcelable saveState() {
        return null;
    }



}
