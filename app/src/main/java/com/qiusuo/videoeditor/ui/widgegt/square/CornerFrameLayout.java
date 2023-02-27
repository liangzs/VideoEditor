package com.qiusuo.videoeditor.ui.widgegt.square;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiusuo.videoeditor.R;

import java.util.Arrays;



public class CornerFrameLayout extends FrameLayout {
	//四个圆角的半径 top-left, top-right, bottom-right, bottom-left
	private float[] mRadiusArray = new float[8];
	private Path path = new Path();

	public CornerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornerFrameLayout);
			if (a.hasValue(R.styleable.CornerFrameLayout_corner_radiu)) {
				float radius = a.getDimension(R.styleable.CornerFrameLayout_corner_radiu, 0);
				Arrays.fill(mRadiusArray, radius);
			} else {
				float radiusTopLeft = a.getDimension(R.styleable.CornerFrameLayout_corner_radiusTopLeft, 0);
				float radiusTopRight = a.getDimension(R.styleable.CornerFrameLayout_corner_radiusTopRight, 0);
				float radiusBottomLeft = a.getDimension(R.styleable.CornerFrameLayout_corner_radiusBottomLeft, 0);
				float radiusBottomRight = a.getDimension(R.styleable.CornerFrameLayout_corner_radiusBottomRight, 0);
				mRadiusArray[0] = radiusTopLeft;
				mRadiusArray[1] = radiusTopLeft;
				mRadiusArray[2] = radiusTopRight;
				mRadiusArray[3] = radiusTopRight;
				mRadiusArray[4] = radiusBottomRight;
				mRadiusArray[5] = radiusBottomRight;
				mRadiusArray[6] = radiusBottomLeft;
				mRadiusArray[7] = radiusBottomLeft;
			}
			a.recycle();
		}

		setLayerType(LAYER_TYPE_SOFTWARE, null);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.clipPath(path);
		super.dispatchDraw(canvas);
		canvas.restore();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		path.reset();
		RectF rect = new RectF(0, 0, w, h);
		path.addRoundRect(rect, mRadiusArray, Path.Direction.CW);
	}


}
