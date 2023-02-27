package com.qiusuo.videoeditor.ui.widgegt.square;

import android.view.View;

/**
 * 以宽为基准， 根据比例计算高
 * Created by Libing on 2017/11/22.
 */

public class SquareAdjustWidthFactory implements SquareFactory.ISquare {
	private float mRatio;

	public SquareAdjustWidthFactory(float ratio) {
		if (ratio <= 0) {
			ratio = 1;
		}
		this.mRatio = ratio;
	}

	@Override
	public int[] makeSquareSpec(int width, int height) {
		height = (int) (width * mRatio);
		return new int[]{View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
				View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)};
	}
}
