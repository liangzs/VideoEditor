package com.qiusuo.videoeditor.ui.widgegt.square;

import android.view.View;

/**
 * SquareFixedFactory
 * Created by Libing on 2017/11/25.
 */

public class SquareFixedFactory implements SquareFactory.ISquare {
	private int mWidth, mHeight;

	public SquareFixedFactory(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
	}

	@Override
	public int[] makeSquareSpec(int width, int height) {
		return new int[]{
				View.MeasureSpec.makeMeasureSpec(mWidth, View.MeasureSpec.EXACTLY),
				View.MeasureSpec.makeMeasureSpec(mHeight, View.MeasureSpec.EXACTLY)
		};
	}
}
