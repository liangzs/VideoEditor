package com.qiusuo.videoeditor.module.select.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.qiusuo.videoeditor.common.bean.MediaEntity;
import com.qiusuo.videoeditor.common.bean.MediaItemPicker;
import com.qiusuo.videoeditor.common.constant.RequestCode;
import com.qiusuo.videoeditor.ui.widgegt.selection.HeaderViewHolder;
import com.qiusuo.videoeditor.ui.widgegt.selection.SimpleSectionedAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by wh on 2019/4/29.
 */
public class MediaRecyclerAdapter extends SimpleSectionedAdapter<MediaAdapterProxy.MyViewHolder> {

    protected MediaAdapterProxy proxy;

    /**
     * 显示日期全选按钮
     */
    protected boolean showSelect;

    public MediaRecyclerAdapter(Context context, List<String> dates, Map<String, List<MediaEntity>> hashMap, List<MediaEntity> nativeData, boolean isAddClip) {
        proxy = new MediaAdapterProxy(context, dates, hashMap, nativeData);
        proxy.setAddClip(isAddClip);
    }

    public MediaRecyclerAdapter(MediaAdapterProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * 因为map中的List<MediaEntity>>会不断变化
     *
     * @param dates
     * @param hashMap
     */
    public void setData(List<String> dates, Map<String, List<MediaEntity>> hashMap, List<MediaEntity> nativeData) {
        if (proxy.setData(dates, hashMap, nativeData)) {
            notifyDataSetChanged();
        }
    }

    public void setData(List<String> dates, Map<String, List<MediaEntity>> hashMap, List<MediaEntity> nativeData, boolean refresh) {
        if (proxy.setData(dates, hashMap, nativeData) && refresh) {
            notifyDataSetChanged();
        }
    }


    public MediaEntity getMediaItem(int section, int posion) {
        return proxy.getMediaItem(section, posion);
    }

    public void setOnItemClickListener(MediaAdapterProxy.onItemClickListener onItemClickListener) {
        proxy.setOnItemClickListener(onItemClickListener);
    }



    public void clear() {
        proxy.clear();
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        return proxy.getSectionHeaderTitle(section);
    }

    @Override
    public int getSectionCount() {
        return proxy.getSectionCount();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return proxy.getItemCountForSection(section);
    }

    @Override
    protected MediaAdapterProxy.MyViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return proxy.onCreateItemViewHolder(parent, viewType);
    }

    @Override
    protected void onBindItemViewHolder(MediaAdapterProxy.MyViewHolder holder, final int section, final int position) {
        proxy.onBindItemViewHolder(holder, section, position);

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.contains(RequestCode.SELECT)) {
            if (holder instanceof MediaAdapterProxy.MyViewHolder) {
                proxy.updateSelected((MediaAdapterProxy.MyViewHolder) holder);
            } else {
                super.onBindViewHolder(holder, position);
            }
            if (payloads.size() == 1) {
                return;
            }
        }
        if (payloads.contains(RequestCode.SECTIONS)) {
            if (holder instanceof HeaderViewHolder) {
                super.onBindViewHolder(holder, position);
            }
            if (payloads.size() == 1) {
                return;
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected int getLayoutResource() {
        return proxy.getLayoutResource();
    }

    @Override
    protected int getTitleTextID() {
        return proxy.getTitleTextID();
    }

    /**
     * 选中当前时间戳
     *
     * @param show
     */
    public void showSelectAll(boolean show) {
        showSelect = show;
    }

    @Override
    protected boolean showSelect() {
        return showSelect;
    }

    @Override
    protected void selectAll(String selectData, boolean toSelect) {
        if (proxy.getOnItemClickListener() != null) {
            proxy.getOnItemClickListener().selectAll(selectData, toSelect);
        }
    }


    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        super.onBindSectionHeaderViewHolder(holder, section);
        if (proxy.mContext instanceof MediaItemPicker) {
            holder.getSelectAll().setSelected(proxy.isSectionChecked(section, getSectionForPosition()));
        }
    }

    /**
     * 依据已有数据更新选中状态
     */
    public void updateSelecting(HeaderViewHolder holder) {
        int adapterPosition = holder.getLayoutPosition();
        if (adapterPosition != -1) {
            int sectionPosition = getSelectionPosition(adapterPosition);
            String title = getSectionHeaderTitle(sectionPosition);
            holder.getSelectAll().setSelected(proxy.selectCount.get(title) == proxy.mHashMap.get(title).size());
        } else  {
            LogUtils.e("HeaderViewHolder", "updateSelecting failed, position is illegal");
        }
    }
}
