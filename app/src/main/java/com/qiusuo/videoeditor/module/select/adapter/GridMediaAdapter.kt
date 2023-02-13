package com.ijoysoft.videoeditor.adapter;

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiusuo.videoeditor.module.select.adapter.MediaAdapterProxy

class GridMediaAdapter(val proxy: MediaAdapterProxy): RecyclerView.Adapter<MediaAdapterProxy.MyViewHolder>() {

        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ) = proxy.onCreateItemViewHolder(parent, viewType)

        override fun onBindViewHolder(holder: MediaAdapterProxy.MyViewHolder, position: Int)
                = proxy.onBindItemViewHolder(holder, -1, position)

        override fun getItemCount() = proxy.nativeData.size
}