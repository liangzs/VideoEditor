package com.qiusuo.videoeditor.ui.widgegt.guide.util;


import android.content.res.ColorStateList;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

public class ViewUtil {
    public ViewUtil() {
    }

    public static void setViewBg(View view, Drawable bg) {
        if (VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(bg);
        } else {
            view.setBackground(bg);
        }

    }

    public static void setViewBackgroundTint(View view, int tintColor) {
        Drawable background = view.getBackground();
        if (background != null) {
            background = DrawableCompat.wrap(background);
            DrawableCompat.setTint(background, tintColor);
            setViewBg(view, background);
        }

    }

    public static void setViewBackgroundTintList(View view, ColorStateList colorStateList) {
        Drawable background = view.getBackground();
        if (background != null) {
            background = DrawableCompat.wrap(background);
            DrawableCompat.setTintList(background, colorStateList);
            setViewBg(view, background);
        }

    }

    public static void generateViewBgSelector(View view) {
        Drawable drawable = view.getBackground();
        if (drawable != null) {
            Drawable.ConstantState state = drawable.getConstantState();
            if (state != null) {
                Drawable pressedDrawable = state.newDrawable().mutate();
                pressedDrawable.setAlpha(128);
                setViewBg(view, ViewState.createPressedSelector(drawable, pressedDrawable));
            }

        }
    }

    public static void generateImageViewSelector(ImageView view) {
        Drawable drawable = view.getDrawable();
        if (drawable != null) {
            Drawable.ConstantState state = drawable.getConstantState();
            if (state != null) {
                Drawable pressedDrawable = state.newDrawable().mutate();
                pressedDrawable.setAlpha(128);
                view.setImageDrawable(ViewState.createPressedSelector(drawable, pressedDrawable));
            }

        }
    }

    public static void setViewSelected(View view, boolean selected) {
        setViewSelected(view, selected, (IExclude)null);
    }

    public static void setViewSelected(View view, boolean selected, IExclude<View> include) {
        if (include == null || !include.isExcluded(view)) {
            view.setSelected(selected);
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;

            for(int i = 0; i < viewGroup.getChildCount(); ++i) {
                View childView = viewGroup.getChildAt(i);
                if (include == null || !include.isExcluded(childView)) {
                    setViewSelected(childView, selected, include);
                }
            }
        }

    }

    public static void setViewEnabled(View view, boolean enabled) {
        setViewEnabled(view, enabled, (IExclude)null);
    }

    public static void setViewEnabled(View view, boolean enabled, IExclude<View> include) {
        if (include == null || !include.isExcluded(view)) {
            view.setEnabled(enabled);
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;

            for(int i = 0; i < viewGroup.getChildCount(); ++i) {
                View childView = viewGroup.getChildAt(i);
                if (include == null || !include.isExcluded(childView)) {
                    setViewEnabled(childView, enabled, include);
                }
            }
        }

    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == 0;
    }

    public static boolean isGone(View view) {
        return view.getVisibility() == 8;
    }

    public static void setGone(View view, boolean gone) {
        view.setVisibility(gone ? 8 : 0);
    }

    public static void setInvisible(View view, boolean invisible) {
        view.setVisibility(invisible ? 4 : 0);
    }

    public static void setColorFilter(ImageView view, int color) {
        view.setColorFilter(new LightingColorFilter(color, 1));
    }

    public static void postOnPreDraw(@NonNull final View view, @NonNull final Runnable runnable) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.removeOnPreDrawListener(this);
                    }

                    runnable.run();
                    return false;
                }
            });
        }

    }

    public static void postOnGlobalLayout(@NonNull final View view, @NonNull final Runnable runnable) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                    }

                    runnable.run();
                }
            });
        }

    }

    public interface IExclude<T> {
        boolean isExcluded(T var1);
    }
}