package com.qiusuo.videoeditor.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.qiusuo.videoeditor.module.home.HomeFragment
import java.lang.IndexOutOfBoundsException

const val HOME_THEME_INDEX = 0;
const val HOME_STUDIO_INDEX = 1;

class HomeFootAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    val mapFragments = mapOf<Int, () -> Fragment>(HOME_THEME_INDEX to { Fragment() },
        HOME_STUDIO_INDEX to { Fragment() })

    override fun getItemCount(): Int {
        return mapFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mapFragments.get(position)?.invoke() ?: throw IndexOutOfBoundsException()
    }
}