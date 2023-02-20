package com.qiusuo.videoeditor.ui.widgegt.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.qiusuo.videoeditor.base.BaseActivity;

/**
 * @ author       lfj
 * @ date         2020/12/7
 * @ description
 */
public abstract class BasePopupWindow extends PopupWindow {
    protected ViewGroup view;
    protected BaseActivity activity;
    private WindowManager.LayoutParams layoutParams;


    public BasePopupWindow(BaseActivity context) {
        this.activity = context;
        //设置窗口弹出时的阴影
        final float alpha = 0.5f;
        // 下面两个方法对于有些手机会导致surfaceview变黑
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * alpha));
        ViewGroupOverlay overlay = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 && isDarkBg()) {
            overlay = parent.getOverlay();
            overlay.add(dim);
        }

        FrameLayout phonyRoot = new FrameLayout(context);
        if (getLayoutId() <= 0) {
            view = bingView(phonyRoot);
        } else {
            view = (ViewGroup) LayoutInflater.from(context).inflate(getLayoutId(), phonyRoot, false);
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        setContentView(view);

        //设置宽高
        float scale = onForceWidthScale();
        if (scale != -1) {
            setWidth((int) (getDisplayMetrics(context).widthPixels * scale));
        } else {
            setWidth(lp.width);
        }
        setHeight(lp.height);

        setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow
        setTouchable(true);//设置PopupWindow可触摸，设置false后,将会阻止PopupWindow窗口里的所有点击事件
        setFocusable(true);//设置PopupWindow可聚焦,设置了可聚焦后,返回back键按下后,不会直接退出当前activity而是先退出当前的PopupWindow.
        setClippingEnabled(true);//设置PopupWindow不覆盖导航栏状态栏
        setBackgroundDrawable(new ColorDrawable(0));//设置背景
    }


    protected abstract int getLayoutId();

    protected ViewGroup bingView(ViewGroup viewGroup) {

        return null;
    }

    protected float onForceWidthScale() {
        return -1;
    }

    protected abstract void show(View view);

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    @Override
    public void dismiss() {
        //还原阴影
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        ViewGroupOverlay overlay = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 && isDarkBg()) {
            overlay = parent.getOverlay();
            overlay.clear();
        }
        super.dismiss();
    }

    protected  boolean isDarkBg(){
        return true;

    }


}
