package com.qiusuo.videoeditor.ui.widgegt.square;

import android.view.View;

import com.qiusuo.videoeditor.base.MyApplication;
import com.qiusuo.videoeditor.util.DensityUtils;

/**
 * 以宽和高的最低值为基准， 根据比例计算宽高
 * Created by Libing on 2017/11/22.
 */

public class SquareWithMinFactory implements SquareFactory.ISquare {
	private float mRatio;

	SquareWithMinFactory(float ratio) {
		if (ratio <= 0) {
			ratio = 1;
		}
		this.mRatio = ratio;
	}

	@Override
	public int[] makeSquareSpec(int width, int height) {
		int minWidthAndHeight = Math.min(width, height);
		int dp = (int) DensityUtils.px2dp(MyApplication.instance, minWidthAndHeight);
		int size;
		if (dp < 240) {
			size = (int) (minWidthAndHeight * 0.95F);
		} else if (dp < 300) {
			size = (int) (minWidthAndHeight * 0.8F);
		} else {
			size = (int) (minWidthAndHeight * mRatio);
		}


		return new int[]{View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY),
				View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)};
	}
}
