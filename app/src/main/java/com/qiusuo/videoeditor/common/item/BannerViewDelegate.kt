package com.nan.xarch.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiusuo.videoeditor.base.BaseItemViewDelegate

class BannerViewDelegate : BaseItemViewDelegate<BannerViewData, BannerViewDelegate.ViewHolder>() {

    private val bannerAdapter = BannerAdapter()

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        context: Context,
        parent: ViewGroup
    ): ViewHolder {
        val holder = ViewHolder(ItemBannerBinding.inflate(inflater, parent, false))
        holder.viewBinding.banner.setAdapter(bannerAdapter)
        holder.viewBinding.banner.addBannerLifecycleObserver(holder.getActivity())
        holder.viewBinding.banner.indicator = CircleIndicator(holder.getContext())
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, item: BannerViewData) {
        super.onBindViewHolder(holder, item)
        bannerAdapter.setDatas(item.value.images)
    }

    class ViewHolder(val viewBinding: ItemBannerBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    class BannerAdapter : BannerImageAdapter<String>(null) {
        override fun onBindView(
            holder: BannerImageHolder,
            image: String,
            position: Int,
            size: Int
        ) {
            holder.imageView.setImageUrl(image)
        }
    }
}