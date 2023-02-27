package com.qiusuo.videoeditor.module.home

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.qiusuo.videoeditor.base.BaseFragment
import com.qiusuo.videoeditor.common.bean.ThemeEntity
import com.qiusuo.videoeditor.databinding.ItemThemeBinding
import com.qiusuo.videoeditor.databinding.TabFragmentItemBinding
import com.qiusuo.videoeditor.util.T

class HomeTabThemeFragment :BaseFragment<TabFragmentItemBinding>(TabFragmentItemBinding::inflate) {

    var themes:List<ThemeEntity>?=null;
    lateinit var viewModel:HomeViewModel;

    override fun initView() {
        viewModel=ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val adapter=object:RecyclerView.Adapter<MyHolder>(){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
                val binding=ItemThemeBinding.inflate(layoutInflater,null,false)
                binding.root.setOnClickListener{
                    T.showShort(context!!,"click")
                }
                return MyHolder(binding)
            }

            override fun getItemCount(): Int {
               return themes?.size?:0;
            }

            override fun onBindViewHolder(holder: MyHolder, position: Int) {
            }
        }
        viewBinding.rvData.adapter=adapter

        viewModel.themeLiveData.observe(this){
            themes=it;
            adapter.notifyDataSetChanged()

        }
    }


 class MyHolder(binding: ItemThemeBinding):ViewHolder(binding.root)






}