package com.qiusuo.videoeditor.ui.widgegt.square;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * SquareImageView
 * Created by Libing on 2017/5/12.
 * @deprecated 使用谷歌官方出品的 {@link com.google.android.material.imageview.ShapeableImageView}
 */

public class SquareImageView extends AppCompatImageView {
	private SquareFactory.ISquare mSquare;

	public SquareImageView(@NonNull Context context) {
		super(context);
	}

	public SquareImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		mSquare = SquareFactory.create(context, attrs);
	}

	public void setSquare(SquareFactory.ISquare square) {
		this.mSquare = square;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		int[] specArray = mSquare.makeSquareSpec(getMeasuredWidth(), getMeasuredHeight());
		super.onMeasure(specArray[0], specArray[1]);
	}
}
