package com.qiusuo.videoeditor.ui.widgegt.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DELL on 2019/9/19.
 */
public class CustomLoadingView extends View {
	private  int width,height;
	private Paint mPaint;
	private  String[] colors ={"#33FC5730","#4DFC5730","#66FC5730","#80FC5730","#99FC5730","#B3FC5730","#CCFC5730","#E6FC5730"};
	public CustomLoadingView(Context context) {
		super(context);
		init(context);
	}
	
	public CustomLoadingView(Context context,AttributeSet attrs) {
		super(context, attrs);
		init(context);
		
	}
	
	public CustomLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
		
	}
	
	
	private void init(Context context){
		  mPaint= new Paint();
		  mPaint.setAntiAlias(true);
		  mPaint.setStrokeWidth(4);
		  mPaint.setColor(Color.parseColor("#FFFC5730"));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width=getMeasuredWidth();
		height=getMeasuredHeight();
	}

	/**
	 * 临时变量
	 */
	RectF rectF = new RectF();

	@Override
	protected void onDraw(Canvas canvas) {
		float radius=getWidth()/12;
		for (int i = 0; i < 8; i++) {
			canvas.save();
			Point point=circleAt(width,height,width/2.5f-radius,i*(Math.PI/4));
			canvas.translate(point.x, point.y);
			canvas.rotate(i*45);
			rectF.set(-radius,-radius/1.5f,1.5f*radius,radius/1.5f);
			mPaint.setColor(Color.parseColor(colors[i]));
			canvas.drawRoundRect(rectF,5,5,mPaint);
			canvas.restore();
		}
		invalidate();
	}
	
	
	
	
	
	Point circleAt(int width,int height,float radius,double angle){
		float x= (float) (width/2+radius*(Math.cos(angle)));
		float y= (float) (height/2+radius*(Math.sin(angle)));
		return new Point(x,y);
	}
	
	final class Point{
		public float x;
		public float y;
		
		public Point(float x, float y){
			this.x=x;
			this.y=y;
		}
	}
}
