package com.qiusuo.videoeditor.ui.widgegt.square;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 圆形layout
 * Created by Libing on 2017/11/14.
 */

public class CircleFrameLayout extends FrameLayout {
	private Path path = new Path();

	public CircleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
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
		int radius = Math.min(w, h) / 2;
		path.reset();
		path.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
	}

}