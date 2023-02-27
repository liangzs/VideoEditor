package com.qiusuo.videoeditor.ui.widgegt.square;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * SquareCornerFrameLayout
 * Created by Libing on 2017/11/29.
 */

public class SquareCornerFrameLayout extends CornerFrameLayout {
	private SquareFactory.ISquare mSquare;

	public SquareCornerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		mSquare = SquareFactory.create(context, attrs);
	}

	public void setSquare(SquareFactory.ISquare square) {
		this.mSquare = square;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mSquare != null) {
			setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
			int[] specArray = mSquare.makeSquareSpec(getMeasuredWidth(), getMeasuredHeight());
			super.onMeasure(specArray[0], specArray[1]);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
