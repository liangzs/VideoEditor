package com.qiusuo.videoeditor.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiusuo.videoeditor.databinding.ThemeItemBinding

class ThemeAdapter :RecyclerView.Adapter<ThemeAdapter.ThemeHodler>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHodler {
        return ThemeHodler(ThemeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
      return 10
    }

    override fun onBindViewHolder(holder: ThemeHodler, position: Int) {
        holder.onbind()
    }


    class ThemeHodler(val bind: ThemeItemBinding) : RecyclerView.ViewHolder(bind.root) {
        fun onbind(){

        }
    }
}