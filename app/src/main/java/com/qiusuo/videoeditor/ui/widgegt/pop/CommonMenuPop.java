package com.qiusuo.videoeditor.ui.widgegt.pop;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;


import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.util.DensityUtils;

import java.util.List;

/**
 * 本地音乐排序
 */
public class CommonMenuPop extends BasePopupMenu {
    private ItemClick itemClick;
    private int mWidth;

    List<Integer> items;

    public CommonMenuPop(AppCompatActivity activity, int defaultSelectIndex, List<Integer> itemsRes, ItemClick itemClick) {
        super(activity, R.mipmap.popup_menu_bg, defaultSelectIndex, DensityUtils.dp2px(activity, 160));
        this.itemClick = itemClick;
        this.items = itemsRes;
    }

    @Override
    protected List<Integer> getItems() {
        return items;
    }

    @Override
    protected int getPopupWidth() {
        return mWidth == 0 ? DensityUtils.dp2px(mActivity, 168) : mWidth;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if (itemClick != null) {
            itemClick.itemClick(position);
        }
    }

    public interface ItemClick {
        void itemClick(int position);
    }
}
