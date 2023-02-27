package com.qiusuo.videoeditor.ui.widgegt.square;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.qiusuo.videoeditor.R;

/**
 * SquareFactory
 * Created by Libing on 2017/11/22.
 */
public class SquareFactory {
	public static final int FACTORY_ADJUST_WIDTH = 0;
	public static final int FACTORY_ADJUST_HEIGHT = 1;
	public static final int FACTORY_WITH_MIN = 2;

	public static ISquare create(Context context, AttributeSet attrs) {
		int factory;
		float ratio;
		if (attrs != null) {
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout);
			factory = array.getInt(R.styleable.SquareLayout_squareFactory, SquareFactory.FACTORY_ADJUST_WIDTH);
			ratio = array.getFloat(R.styleable.SquareLayout_squareRatio, 1F);
			array.recycle();
		} else {
			factory = SquareFactory.FACTORY_ADJUST_WIDTH;
			ratio = 1F;
		}
		return SquareFactory.create(factory, ratio);
	}

	public static ISquare create(int factory, float ratio) {
		ISquare squareFactory ;
		if (factory == FACTORY_ADJUST_HEIGHT) {
			squareFactory = new SquareAdjustHeightFactory(ratio);
		} else if (factory == FACTORY_WITH_MIN) {
			squareFactory = new SquareWithMinFactory(ratio);
		} else {
			squareFactory = new SquareAdjustWidthFactory(ratio);
		}
		return squareFactory;
	}

	public interface ISquare {
		int[] makeSquareSpec(int width, int height);
	}
}
