package com.qiusuo.videoeditor.ui.widgegt.guide.util;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

public class ViewState {
    public static final int[] DRAWABLE_STATE_DEFAULT = new int[0];
    public static final int[] DRAWABLE_STATE_PRESSED = new int[]{16842919, 16842910};
    public static final int[] DRAWABLE_STATE_SELECTED = new int[]{16842913, 16842910};
    public static final int[] DRAWABLE_STATE_CHECKED = new int[]{16842912, 16842910};
    public static final int[] DRAWABLE_STATE_UNCHECKED = new int[]{-16842912};
    public static final int[] DRAWABLE_STATE_FOCUSED = new int[]{16842908, 16842910};
    public static final int[] DRAWABLE_STATE_DISABLED = new int[]{-16842910};
    public static final int[] DRAWABLE_STATE_ENABLED = new int[]{16842910};
    public static final int DISABLED_COLOR = -8355712;

    public ViewState() {
    }

    public static Drawable createPressedSelector(Context context, int[] resource) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_PRESSED, AppCompatResources.getDrawable(context, resource[1]));
        drawable.addState(DRAWABLE_STATE_DEFAULT, AppCompatResources.getDrawable(context, resource[0]));
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createPressedSelector(int colorNormal, int colorPressed) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_PRESSED, new ColorDrawable(colorPressed));
        drawable.addState(DRAWABLE_STATE_DEFAULT, new ColorDrawable(colorNormal));
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createCornerPressedSelector(int colorNormal, int colorPressed, float corner) {
        StateListDrawable drawable = new StateListDrawable();
        GradientDrawable normal = new GradientDrawable();
        normal.setColor(colorNormal);
        normal.setCornerRadius(corner);
        GradientDrawable pressed = new GradientDrawable();
        pressed.setColor(colorPressed);
        pressed.setCornerRadius(corner);
        drawable.addState(DRAWABLE_STATE_PRESSED, pressed);
        drawable.addState(DRAWABLE_STATE_DEFAULT, normal);
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createPressedSelector(Drawable drawableNormal, Drawable drawablePressed) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_PRESSED, drawablePressed);
        drawable.addState(DRAWABLE_STATE_DEFAULT, drawableNormal);
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createSelectedSelector(Context context, int[] resource) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_SELECTED, AppCompatResources.getDrawable(context, resource[1]));
        drawable.addState(DRAWABLE_STATE_DEFAULT, AppCompatResources.getDrawable(context, resource[0]));
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createSelectedSelector(int colorNormal, int colorSelected) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_SELECTED, new ColorDrawable(colorSelected));
        drawable.addState(DRAWABLE_STATE_DEFAULT, new ColorDrawable(colorNormal));
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createSelectedSelector(@NonNull Drawable drawableNormal, @Nullable Drawable drawableSelected, @Nullable Drawable drawableDisable) {
        StateListDrawable drawable = new StateListDrawable();
        if (drawableDisable != null) {
            drawable.addState(DRAWABLE_STATE_DISABLED, drawableDisable);
        }

        if (drawableSelected != null) {
            drawable.addState(DRAWABLE_STATE_SELECTED, drawableSelected);
        }

        drawable.addState(DRAWABLE_STATE_DEFAULT, drawableNormal);
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createCheckedSelector(Context context, int[] resource) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_CHECKED, AppCompatResources.getDrawable(context, resource[0]));
        drawable.addState(DRAWABLE_STATE_UNCHECKED, AppCompatResources.getDrawable(context, resource[1]));
        drawable.setState(DRAWABLE_STATE_UNCHECKED);
        return drawable;
    }

    public static Drawable createDisabledSelector(Context context, int[] resource) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_DISABLED, AppCompatResources.getDrawable(context, resource[2]));
        drawable.addState(DRAWABLE_STATE_PRESSED, AppCompatResources.getDrawable(context, resource[1]));
        drawable.addState(DRAWABLE_STATE_DEFAULT, AppCompatResources.getDrawable(context, resource[0]));
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static Drawable createDisabledSelector2(Context context, int[] resource) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(DRAWABLE_STATE_DISABLED, AppCompatResources.getDrawable(context, resource[1]));
        drawable.addState(DRAWABLE_STATE_DEFAULT, AppCompatResources.getDrawable(context, resource[0]));
        drawable.setState(DRAWABLE_STATE_DEFAULT);
        return drawable;
    }

    public static ColorStateList createSelectedColorStateList(int normalColor, int selectedColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_SELECTED, DRAWABLE_STATE_DEFAULT}, new int[]{selectedColor, normalColor});
    }

    public static ColorStateList createSelectedColorStateList(int normalColor, int selectedColor, int disableColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_DISABLED, DRAWABLE_STATE_SELECTED, DRAWABLE_STATE_ENABLED}, new int[]{disableColor, selectedColor, normalColor});
    }

    public static ColorStateList createFocusedColorStateList(int normalColor, int focusedColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_FOCUSED, DRAWABLE_STATE_ENABLED}, new int[]{focusedColor, normalColor});
    }

    public static ColorStateList createFocusedColorStateList(int normalColor, int focusedColor, int disableColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_DISABLED, DRAWABLE_STATE_FOCUSED, DRAWABLE_STATE_ENABLED}, new int[]{disableColor, focusedColor, normalColor});
    }

    public static ColorStateList createPressedColorStateList(int normalColor, int pressedColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_PRESSED, DRAWABLE_STATE_DEFAULT}, new int[]{pressedColor, normalColor});
    }

    public static ColorStateList createPressedColorStateList(int normalColor, int pressedColor, int disableColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_DISABLED, DRAWABLE_STATE_PRESSED, DRAWABLE_STATE_ENABLED}, new int[]{disableColor, pressedColor, normalColor});
    }

    public static ColorStateList createCheckedColorStateList(int normalColor, int checkedColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_CHECKED, DRAWABLE_STATE_DEFAULT}, new int[]{checkedColor, normalColor});
    }

    public static ColorStateList createCheckedColorStateList(int normalColor, int checkedColor, int disableColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_DISABLED, DRAWABLE_STATE_CHECKED, DRAWABLE_STATE_ENABLED}, new int[]{disableColor, checkedColor, normalColor});
    }

    public static ColorStateList createDisableColorStateList(int normalColor, int disableColor) {
        return new ColorStateList(new int[][]{DRAWABLE_STATE_DISABLED, DRAWABLE_STATE_ENABLED}, new int[]{disableColor, normalColor});
    }
}