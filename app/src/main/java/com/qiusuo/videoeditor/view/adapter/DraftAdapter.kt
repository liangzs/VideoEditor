package com.qiusuo.videoeditor.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiusuo.videoeditor.common.room.Project
import com.qiusuo.videoeditor.databinding.ItemDraftLayoutBinding

class DraftAdapter(val list: List<Project>) : RecyclerView.Adapter<DraftAdapter.DraftHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftHolder {
        return DraftHolder(ItemDraftLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DraftHolder, position: Int) {
        holder.onbind(list.get(position))
    }

    inner class DraftHolder(val binding: ItemDraftLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onbind(project: Project) {
        }
    }
}