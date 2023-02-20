package com.qiusuo.videoeditor.ui.widgegt;

import android.content.Context;
import android.graphics.PointF;
import android.widget.ImageView;

/**
 * Created by wh on 2019/4/30.
 */
public class MoveImageView extends ImageView {
	
	public MoveImageView(Context context) {
		super(context);
	}
	
	public void setMPointF(PointF pointF) {
		setX(pointF.x);
		setY(pointF.y);
	}
}
