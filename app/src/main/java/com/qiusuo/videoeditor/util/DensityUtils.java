package com.qiusuo.videoeditor.util;

import android.content.Context;
import android.graphics.Paint;
import android.util.TypedValue;

/**
 * Created by DELL on 2019/4/25.
 */
public class DensityUtils {
	private DensityUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static int dp2px(Context context, float dpVal) {
		return (int)TypedValue.applyDimension(1, dpVal, context.getResources().getDisplayMetrics());
	}
	
	public static int sp2px(Context context, float spVal) {
		return (int)TypedValue.applyDimension(2, spVal, context.getResources().getDisplayMetrics());
	}
	
	public static float px2dp(Context context, float pxVal) {
		float scale = context.getResources().getDisplayMetrics().density;
		return pxVal / scale;
	}
	
	public static float px2sp(Context context, float pxVal) {
		return pxVal / context.getResources().getDisplayMetrics().scaledDensity;
	}
	
	public static float getTextBaseLine(Paint paint, float centerY) {
		Paint.FontMetrics fm = paint.getFontMetrics();
		return centerY - fm.descent + (fm.bottom - fm.top) / 2.0F;
	}
	
	public static int getTextHeight(Paint paint) {
		Paint.FontMetrics fm = paint.getFontMetrics();
		return (int)Math.ceil((double)(fm.descent - fm.ascent));
	}
}
