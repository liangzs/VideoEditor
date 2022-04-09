package com.qiusuo.videoeditor.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.databinding.ItemHomeFunBinding

class HomeFunAdapter(val itemClick: (Int) -> Unit) : RecyclerView.Adapter<HomeFunAdapter.HomeFunHolder>() {

    val listIcon: IntArray;
    val listName: IntArray

    init {
        listIcon = intArrayOf(R.drawable.icon_game, R.drawable.icon_cat,
            R.drawable.icon_girl, R.drawable.icon_history, R.drawable.icon_game,
            R.drawable.icon_cat, R.drawable.icon_girl)
        listName = intArrayOf(R.string.app_name, R.string.app_name, R.string.app_name,
            R.string.app_name,R.string.app_name, R.string.app_name, R.string.app_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFunHolder {
        return HomeFunHolder(ItemHomeFunBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeFunHolder, position: Int) {
        holder.binding.ivMainFun.setImageResource(listIcon[position])
        holder.binding.tvMainFun.setText(listName[position])
        holder.binding.root.setOnClickListener() { itemClick(position) }
    }

    override fun getItemCount(): Int {
        return listIcon.size
    }


    inner class HomeFunHolder(var binding: ItemHomeFunBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}