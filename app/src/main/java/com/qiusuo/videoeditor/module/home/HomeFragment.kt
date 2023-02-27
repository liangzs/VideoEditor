package com.qiusuo.videoeditor.module.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.constant.PageName
import com.qiusuo.videoeditor.databinding.FragmentHomeBinding
import com.qiusuo.videoeditor.ui.adapter.HomeFunAdapter
import com.qiusuo.videoeditor.ui.widgegt.RecyclerItemDecoration

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val viewModel: HomeViewModel by viewModels()
    lateinit var launcher: ActivityResultLauncher<Intent>

    companion object {
        const val SPAN_COUNT = 2;
        const val REQUEST_CODE = 100;
        const val CALLBACK_CODE = 200;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = createActivityResultLauncher()
        initView();
    }

    override fun initView() {
        var gridlayout = GridLayoutManager(context, SPAN_COUNT);
        gridlayout.orientation = GridLayoutManager.HORIZONTAL;
        gridlayout.isAutoMeasureEnabled = true
        viewBinding.rvMainFunc.setLayoutManager(gridlayout)
//        viewBinding.rvMainFunc.addItemDecoration(
//            RecyclerItemDecoration(
//                activity.resources.getDimensionPixelOffset(R.dimen.item_padding), true, true
//            )
//        )
        viewBinding.rvMainFunc.adapter = HomeFunAdapter { position: Int -> funItemClick(position) }

        viewBinding.flCreate.setOnClickListener {

        }

        //tab 我的作品和最新主題
        viewBinding.viewPager.adapter=object :FragmentStateAdapter(this){
            override fun getItemCount(): Int =2

            override fun createFragment(position: Int): Fragment {
               if(position==0){
                   return
               }
            }
        }
    }


    fun funItemClick(position: Int) {
        openGallery()
    }


    override fun getPageName(): String {
        return PageName.HOME
    }

    fun openGallery() {


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
