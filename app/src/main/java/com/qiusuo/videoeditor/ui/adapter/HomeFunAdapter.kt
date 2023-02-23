package com.qiusuo.videoeditor.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.qiusuo.videoeditor.R
import com.qiusuo.videoeditor.databinding.ItemHomeFunBinding
import com.qiusuo.videoeditor.ui.widgegt.guide.util.ScreenUtils

class HomeFunAdapter(val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<HomeFunAdapter.HomeFunHolder>() {

    val listIcon: IntArray;
    val listName: IntArray

    init {
        listIcon = intArrayOf(
            R.drawable.vector_t_onkey, R.drawable.vector_t_gif,
            R.drawable.vector_t_photo_edit, R.drawable.vector_t_mosaic, R.drawable.vector_t_trim,
            R.drawable.vector_t_compress, R.drawable.vector_t_camera, R.drawable.vector_t_video2mp3
        )
        listName = intArrayOf(
            R.string.to_onkey,
            R.string.to_gif,
            R.string.to_photo_edit,
            R.string.to_mosaic,
            R.string.to_trim,
            R.string.to_compress,
            R.string.to_camera,
            R.string.to_video2mp3
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFunHolder {
        return HomeFunHolder(
            ItemHomeFunBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeFunHolder, position: Int) {
        holder.binding.root.layoutParams.width = (ConstantMediaSize.screenWidth / 4f).toInt()
        holder.binding.ivMainFun.setImageResource(listIcon[position])
        holder.binding.tvMainFun.setText(listName[position])
        holder.binding.root.setOnClickListener() { itemClick(position) }
    }

    override fun getItemCount(): Int {
        return listIcon.size
    }


    inner class HomeFunHolder(var binding: ItemHomeFunBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}