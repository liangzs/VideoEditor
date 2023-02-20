package com.qiusuo.videoeditor.ui.widgegt.pop;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.mediacodec.PhoneAdatarList;
import com.ijoysoft.videoeditor.R;
import com.ijoysoft.videoeditor.base.BaseActivity;
import com.lb.library.CollectionUtil;
import com.lb.library.DensityUtils;
import com.lb.library.DrawableHelper;
import com.lb.library.ScreenUtils;
import com.lb.library.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 标题栏菜单
 */
public class BaseTitleMenu implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    protected View mAuthor;
    private static final List<BaseTitleMenu> mMenus = new ArrayList<>();
    protected PopupWindow mPopupWindow;
    protected BaseActivity mActivity;

    /**
     * 按钮集合
     */
    protected List<PopupItem> popupItems;


    /**
     * 点击后自动消失
     */
    protected boolean dismissAfterClick = true;

    /**
     * 按钮构造函数
     *
     * @param items PopupItem
     */
    public BaseTitleMenu(BaseActivity activity, List<PopupItem> items, @NonNull SelectCallback callback) {
        this.mActivity = activity;
        mPopupWindow = new PopupWindow(mActivity);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(this);
        add(this);
        this.popupItems = items;
        this.callback = callback;
    }

    /**
     * 字符id 构造函数
     *
     * @param stringIds 字符id
     */
    public BaseTitleMenu(BaseActivity activity, int[] stringIds, @NonNull SelectCallback callback) {
        this.mActivity = activity;
        mPopupWindow = new PopupWindow(mActivity);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(this);
        add(this);
        this.popupItems = new ArrayList();
        for (int id : stringIds) {
            popupItems.add(PopupItem.create(id));
        }
        this.callback = callback;
    }


    public synchronized static void add(BaseTitleMenu menu) {
        if (!mMenus.contains(menu)) {
            mMenus.add(menu);
        }
    }


    @Override
    public void onDismiss() {
        CollectionUtil.removeElement(mMenus, new CollectionUtil.Judge<BaseTitleMenu>() {
            @Override
            public boolean canRemove(BaseTitleMenu basePupouMenu) {
                return basePupouMenu == BaseTitleMenu.this;
            }
        });
    }

    public static void releasePopupWindow(final Activity activity) {
        CollectionUtil.removeElement(mMenus, new CollectionUtil.Judge<BaseTitleMenu>() {
            @Override
            public boolean canRemove(BaseTitleMenu menu) {
                if (menu != null && menu.mActivity == activity) {
                    try {
                        menu.mPopupWindow.setOnDismissListener(null);
                        menu.mPopupWindow.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    protected Drawable createRightArrow() {
        Drawable right = mActivity.getResources().getDrawable(R.drawable.ic_arrow_right_2);
        int drawableSize = DensityUtils.dp2px(mActivity, 10);
        right.setBounds(0, 0, drawableSize / 2, drawableSize);
        return right;
    }

    protected Drawable createRightCheck() {
        Drawable right = mActivity.getResources().getDrawable(R.drawable.ic_menu_checked_right);
        int drawableSize = DensityUtils.dp2px(mActivity, 15);
        right.setBounds(0, 0, drawableSize, drawableSize);
        return right;
    }


    public final void show(View author) {
        mAuthor = author;
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        ListView listView = (ListView) inflater.inflate(R.layout.popupwindow_list, null);
        listView.setAdapter(new ListAdapter(popupItems, inflater));
        listView.setOnItemClickListener(this);


        mPopupWindow.setContentView(listView);
        Drawable drawable = getBackgroundDrawable();

        //有奇怪的padding left 12px top:8px bottom 12px right:8px
        //加上奇怪的padding
        Rect padding = new Rect();
        drawable.getPadding(padding);
        mPopupWindow.setWidth(getWidth(popupItems) + padding.left + padding.right);
        mPopupWindow.setHeight(getHeight(popupItems));
        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setAnimationStyle(getWindowAnimationStyle());

        Locale locale = mAuthor.getResources().getConfiguration().locale;
        LogUtils.v("BasePopupMenu", "locale:" + locale);
        int gravity = getGravity();
        int[] location = getLocation(author);
        if (locale != null) {
            for (String str : PhoneAdatarList.ARAB_TAB) {
                if (locale.toString().contains(str)) {
                    gravity = Gravity.TOP | Gravity.START;
                    break;
                }
            }
        }
        mPopupWindow.showAtLocation(author, gravity, location[0], location[1]);
    }

    protected int getWindowAnimationStyle() {
        return R.style.fade_popwindow;
    }

    protected Drawable getBackgroundDrawable() {
        //有奇怪的padding left 12px top:8px bottom 12px right:8px
//        if (false) {
//            return AppCompatResources.getDrawable(mActivity, R.drawable.popup_menu_night_bg);
//        } else {
//            return AppCompatResources.getDrawable(mActivity, R.drawable.popup_menu_day_bg);
//        }

        return AppCompatResources.getDrawable(mActivity, R.mipmap.popup_menu_bg);

    }

    protected int getHeight(List<PopupItem> popupItems) {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }


    protected int getWidth(List<PopupItem> popupItems) {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(DensityUtils.sp2px(mActivity, 16));
        float maxWidth = 0;
        boolean containDrawable = false;
        for (PopupItem popupItem : popupItems) {
            //popupItem.isTitleIconMenu() 不在drawable里，不管
            containDrawable = containDrawable || popupItem.isTitleMenu() || popupItem.isCheckMenu() || popupItem.isSubMenu();
            maxWidth = Math.max(maxWidth, textPaint.measureText(mActivity.getString(popupItem.getTextId())));
        }

        if (containDrawable) {
            maxWidth += DensityUtils.dp2px(mActivity, 52 + 52);
            return Math.max(DensityUtils.dp2px(mActivity, 168), (int) maxWidth);
        } else {
            //加上两个14的padding等于28 加上LinearLayoutMarginEnd dp_10
            return (int) (maxWidth + DensityUtils.dp2px(mActivity, 28) + mActivity.getResources().getDimension(R.dimen.dp_10));
        }
    }

    protected int getGravity() {
        return Gravity.TOP | Gravity.END;
    }

    protected int[] getLocation(View author) {
        int[] location = new int[2];
        author.getLocationOnScreen(location);

        location[0] = 0;
        if (!ScreenUtils.isTablet(mActivity)) {
            location[1] = location[1] + author.getHeight() - 8;
        } else {
            location[1] = location[1] + author.getHeight() - DensityUtils.dp2px(mActivity, 5);
        }
        return location;
    }


    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListAdapter adapter = (ListAdapter) parent.getAdapter();
        PopupItem item = adapter.getItem(position);
        if (!item.isTitleMenu()) {
            onPopupItemClicked(position);
        }
    }


    protected void bindMenuText(TextView titleView, PopupItem popupItem) {
        titleView.setText(popupItem.getText(mActivity));
    }

    /**
     * 点击回调
     */
    private SelectCallback callback;

    protected void onPopupItemClicked(int position) {
        callback.select(position);
        if (dismissAfterClick) {
            mPopupWindow.dismiss();
        }
    }

    private class ListAdapter extends BaseAdapter {
        private List<PopupItem> items;
        private LayoutInflater inflater;

        ListAdapter(List<PopupItem> items, LayoutInflater inflater) {
            super();
            this.items = items;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public PopupItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popupwindow_list_title_item, parent, false);
            }
            text = convertView.findViewById(R.id.text);
            PopupItem popupItem = getItem(position);
            int textColor = 0;
            int rippleColor = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textColor = mActivity.getColor(R.color.black_alpha_80);
                rippleColor = mActivity.getColor(R.color.pressed_color);
            } else {
                textColor = mActivity.getResources().getColor(R.color.black_alpha_80);
                rippleColor = mActivity.getResources().getColor(R.color.pressed_color);
            }
            if (popupItem.isSubMenu()) {
                text.setCompoundDrawables(null, null, createRightArrow(), null);
                text.setTextColor(textColor);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                ViewUtil.setViewBg(text, DrawableHelper.createRectRipple(Color.TRANSPARENT, rippleColor));
            } else if (popupItem.isCheckMenu()) {
                text.setCompoundDrawables(null, null, createRightCheck(), null);
                text.setTextColor(textColor);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                ViewUtil.setViewBg(text, DrawableHelper.createRectRipple(Color.TRANSPARENT, rippleColor));
            } else if (popupItem.isTitleMenu()) {
                text.setCompoundDrawables(null, null, null, null);
                text.setTextColor(ColorUtils.setAlphaComponent(textColor, 0x99));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                ViewUtil.setViewBg(text, null);
            } else {
                text.setCompoundDrawables(null, null, null, null);
                text.setTextColor(textColor);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                ViewUtil.setViewBg(text, DrawableHelper.createRectRipple(Color.TRANSPARENT, rippleColor));
            }
            BaseTitleMenu.this.bindMenuText(text, popupItem);
//            text.setText(popupItem.getTextId());
            return convertView;
        }
    }


    public interface SelectCallback {
        void select(int position);
    }

    public List<PopupItem> getPopupItems() {
        return popupItems;
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
