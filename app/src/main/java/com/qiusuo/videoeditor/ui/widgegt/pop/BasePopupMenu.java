package com.qiusuo.videoeditor.ui.widgegt.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.util.DensityUtils;

import java.util.List;
import java.util.Locale;


/**
 * PopupWindow弹窗父类
 * Created by quinn on 2018/4/9.
 */
public class BasePopupMenu implements OnItemClickListener {
    protected AppCompatActivity mActivity;
    protected PopupWindow mPopupWindow;
    protected int gravity;
    protected boolean VISIBLE;
    private int selectIndex = -1;

    /**
     * 按钮适配器
     */
    MenuAdapter menuAdapter;


    /* public BasePopupMenu(AppCompatActivity activity) {
         new  BasePopupMenu(activity,R.drawable.dialog_background);
     }*/
    public BasePopupMenu(AppCompatActivity activity, int bgRes, int selectIndex, int width) {
        this.mActivity = activity;
        this.selectIndex = selectIndex;
        mPopupWindow = new PopupWindow(activity);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(bgRes));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setWidth(width);
        mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
//        mPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.fade_popwindow);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0F);
            }
        });
    }

    public void showAt(View author, int height, boolean VISIBLE) {
        this.VISIBLE = VISIBLE;
        show(author, mPopupWindow.getWidth() - author.getWidth(), height);
    }

    public void showAt(View author, boolean VISIBLE) {
        View view = createPopupViewByContent();
        mPopupWindow.setContentView(view);
        int[] loc = getOffsetXY(author, mPopupWindow.getWidth());
        mPopupWindow.showAtLocation(author, getGravity(), loc[0], loc[1]);
        /*if(!VISIBLE)
            setBackgroundAlpha(0.8F);*/
    }

    public void showAt(View author, int gravity) {
        View view = createPopupViewByContent();
        mPopupWindow.setContentView(view);
        int[] loc = getOffsetXY(author, mPopupWindow.getWidth());
        mPopupWindow.showAtLocation(author, gravity, author.getWidth() / 3, loc[1]);
        /*if(!VISIBLE)
            setBackgroundAlpha(0.8F);*/
    }

    public void showAtWithoutMargin(View author, int gravity) {
        View view = createPopupViewByContent();
        mPopupWindow.setContentView(view);
        Locale locale = view.getResources().getConfiguration().locale;
        LogUtils.v("BasePopupMenu","locale"+locale+","+"gravity:"+gravity);
        int width = mPopupWindow.getWidth();
        if (locale != null) {
            if ("ar".equals(locale.toString().trim()) || "fa".equals(locale.toString().trim())) {
                width = 0;
            }
        }
        int[] loc = getOffsetXY(author, width);
        mPopupWindow.showAtLocation(author, gravity, 0, loc[1]);
        /*if(!VISIBLE)
            setBackgroundAlpha(0.8F);*/
    }

    public void show(View author, int width, int height) {
        View view = createPopupViewByContent();
        mPopupWindow.setContentView(view);
        int[] loc = getOffsetXY(author, mPopupWindow.getWidth());
        mPopupWindow.showAtLocation(author, getGravity(), width == 0 ? loc[0] : width, height);

        if (!VISIBLE)
            setBackgroundAlpha(0.8F);
    }

    public void showUp(View author) {
        View view = createPopupViewByContent();
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getWidth();
        int height = view.getHeight();
        mPopupWindow.setContentView(view);
        Log.e("mytest", "" + mPopupWindow.getWidth());
        int[] loc = new int[2];
        author.getLocationOnScreen(loc);


        mPopupWindow.showAtLocation(author, Gravity.NO_GRAVITY, 0, loc[1] - height * 5);
        setBackgroundAlpha(0.8F);
    }

    public void showUpAt(View author) {
        View view = createPopupViewByContent();
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getWidth();
        int height = view.getHeight();
        mPopupWindow.setContentView(view);
        int[] loc = new int[2];
        author.getLocationOnScreen(loc);
        mPopupWindow.showAtLocation(author, getGravity(), (loc[0]) - width / 2, 100);
        setBackgroundAlpha(0.8F);
    }


    protected int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    protected int[] getOffsetXY(View author, int width) {
        int[] loc = new int[2];
        //View在屏幕上的位置
        author.getLocationOnScreen(loc);
        loc[0] = loc[0] + author.getWidth() - width/* - DensityUtils.dp2px(mActivity, 4)*/;
        loc[1] = loc[1] + author.getHeight();
        loc[0] = Math.max(loc[0], 0);
        loc[1] = Math.max(loc[1], 0);
        return loc;
    }

//    protected View createPopupView() {
//        ListView listview = new ListView(mActivity);
//        listview.setDivider(new ColorDrawable(Color.TRANSPARENT));
//        listview.setDividerHeight(0);
//        listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        listview.setCacheColorHint(Color.TRANSPARENT);
//        listview.setOnItemClickListener(this);
//        MenuAdapter menuAdapter = new MenuAdapter(mActivity, getItems());
//        listview.setAdapter(menuAdapter);
//        return listview;
//    }

    protected View createPopupViewByContent() {
        ListView listview = new ListView(mActivity);
        listview.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listview.setDividerHeight(0);
        listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listview.setCacheColorHint(Color.TRANSPARENT);
        listview.setOnItemClickListener(this);
        menuAdapter = new MenuAdapter(mActivity, getItems());
        //从新设置popupwindow的宽
        mPopupWindow.setWidth(measureContentWidth(menuAdapter));
        listview.setAdapter(menuAdapter);
        return listview;
    }

    /**
     * 根据内容计算最长的宽度
     *
     * @param listAdapter
     * @return
     */
    protected int measureContentWidth(BaseAdapter listAdapter) {
        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        final BaseAdapter adapter = listAdapter;
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(mActivity);
            }

            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);

            final int itemWidth = itemView.getMeasuredWidth();

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
        return maxWidth + 100;
    }

    protected List<Integer> getItems() {
        return null;
    }


    /**
     * 基数56、常见1.5X、2X、3X、6X、7X
     */
    protected int getPopupWidth() {
        return DensityUtils.dp2px(mActivity, 168);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPopupWindow.dismiss();
    }

    protected FragmentManager getActivityFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    private class MenuAdapter extends BaseAdapter {
        private List<Integer> strIds;
        private LayoutInflater inflater;

        public MenuAdapter(Context context, List<Integer> strIds) {
            this.strIds = strIds;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return strIds == null ? 0 : strIds.size();
        }

        @Override
        public Integer getItem(int position) {
            return strIds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popupwindow_list_item, parent, false);
            }
            TextView text = (TextView) convertView.findViewById(R.id.text);
            ImageView select = (ImageView) convertView.findViewById(R.id.item_menu_icon);
            if (text != null) {
                text.setText(mActivity.getResources().getString(getItem(position)));
            }
            if (select != null) {
                if (position == selectIndex) {
                    select.setVisibility(View.VISIBLE);
                } else {
                    select.setVisibility(View.INVISIBLE);
                }
            }
            return convertView;
        }
    }

    /**
     * 设置屏幕背景的透明度
     *
     * @param alpha 0.0-1.0
     */
    protected void setBackgroundAlpha(float alpha) {
        Window window = mActivity.getWindow();
        window.addFlags(LayoutParams.FLAG_DIM_BEHIND);
        LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

    public boolean isShowing() {
        if (mPopupWindow != null) {
            return mPopupWindow.isShowing();
        }
        return false;
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        menuAdapter.notifyDataSetChanged();
    }
}
