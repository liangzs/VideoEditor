package com.qiusuo.videoeditor.ui.widgegt.guide.util;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build.VERSION;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;
import java.util.Arrays;

public class DrawableHelper {
    public DrawableHelper() {
    }

    public static Drawable createRectRipple(int normalColor, int rippleColor) {
        if (VERSION.SDK_INT >= 21) {
            ColorDrawable content = new ColorDrawable(normalColor);
            ShapeDrawable ripple = new ShapeDrawable(new RectShape());
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), content, ripple);
        } else {
            return ViewState.createPressedSelector(normalColor, ColorUtils.setAlphaComponent(rippleColor, 26));
        }
    }

    public static Drawable createCornerRectRipple(int normalColor, int rippleColor, float corner) {
        if (VERSION.SDK_INT >= 21) {
            GradientDrawable content = new GradientDrawable();
            content.setCornerRadius(corner);
            content.setColor(normalColor);
            float[] outerRadii = new float[8];
            Arrays.fill(outerRadii, corner);
            RoundRectShape r = new RoundRectShape(outerRadii, (RectF)null, (float[])null);
            ShapeDrawable ripple = new ShapeDrawable(r);
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), content, ripple);
        } else {
            return ViewState.createCornerPressedSelector(normalColor, ColorUtils.setAlphaComponent(rippleColor, 26), corner);
        }
    }

    public static Drawable createCircleRectRipple(int normalColor, int rippleColor) {
        GradientDrawable content = new GradientDrawable();
        content.setShape(1);
        content.setColor(normalColor);
        if (VERSION.SDK_INT >= 21) {
            ShapeDrawable ripple = new ShapeDrawable(new OvalShape());
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), content, ripple);
        } else {
            GradientDrawable pressed = new GradientDrawable();
            pressed.setShape(1);
            pressed.setColor(ColorUtils.setAlphaComponent(rippleColor, 26));
            return ViewState.createPressedSelector(content, pressed);
        }
    }

    public static Drawable createCornerStrokeRipple(int corner, int strokeWidth, int normalColor, int rippleColor) {
        GradientDrawable content = new GradientDrawable();
        content.setStroke(strokeWidth, normalColor);
        content.setCornerRadius((float)corner);
        if (VERSION.SDK_INT >= 21) {
            float[] outerRadii = new float[8];
            Arrays.fill(outerRadii, (float)corner);
            RoundRectShape r = new RoundRectShape(outerRadii, (RectF)null, (float[])null);
            ShapeDrawable ripple = new ShapeDrawable(r);
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), content, ripple);
        } else {
            GradientDrawable pressed = new GradientDrawable();
            int alpha = Math.max(26, (int)((float)Color.alpha(normalColor) * 0.8F));
            pressed.setStroke(strokeWidth, ColorUtils.setAlphaComponent(normalColor, alpha));
            pressed.setCornerRadius((float)corner);
            return ViewState.createPressedSelector(content, pressed);
        }
    }

    public static Drawable createProgressDrawable(int backgroundColor, int secondProgressColor, int progressColor, int corner) {
        GradientDrawable background = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{backgroundColor, backgroundColor});
        background.setCornerRadius((float)corner);
        GradientDrawable second = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{secondProgressColor, secondProgressColor});
        second.setCornerRadius((float)corner);
        ClipDrawable secondProgressWrapper = new ClipDrawable(second, 3, 1);
        GradientDrawable progress = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{progressColor, progressColor});
        progress.setCornerRadius((float)corner);
        ClipDrawable progressWrapper = new ClipDrawable(progress, 3, 1);
        return createProgressDrawable(background, secondProgressWrapper, progressWrapper);
    }

    public static Drawable createProgressDrawable(int backgroundColor, int progressColor, int corner) {
        GradientDrawable bg = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{backgroundColor, backgroundColor});
        bg.setCornerRadius((float)corner);
        GradientDrawable progress = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{progressColor, progressColor});
        progress.setCornerRadius((float)corner);
        ClipDrawable progressWrapper = new ClipDrawable(progress, 3, 1);
        return createProgressDrawable(bg, progressWrapper);
    }

    public static Drawable createProgressDrawable(Context context, int backgroundResId, int progressResId) {
        return createProgressDrawable(AppCompatResources.getDrawable(context, backgroundResId), AppCompatResources.getDrawable(context, progressResId));
    }

    public static Drawable createProgressDrawable(Drawable background, Drawable progress) {
        LayerDrawable result = new LayerDrawable(new Drawable[]{background, progress});
        result.setId(0, 16908288);
        result.setId(1, 16908301);
        return result;
    }

    public static Drawable createProgressDrawable(Drawable background, Drawable secondProgress, Drawable progress) {
        LayerDrawable result = new LayerDrawable(new Drawable[]{background, secondProgress, progress});
        result.setId(0, 16908288);
        result.setId(1, 16908303);
        result.setId(2, 16908301);
        return result;
    }

    public static Drawable createGradientDrawable(float corner, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        return drawable;
    }
}
