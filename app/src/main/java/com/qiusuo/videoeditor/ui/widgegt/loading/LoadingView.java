package com.qiusuo.videoeditor.ui.widgegt.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.util.DensityUtils;


/**
 * Created by DELL on 2019/9/16.
 */
public class LoadingView extends View {
    private int width, height;
    private int padding;
    private int circleRadius;
    private Paint mPaint;
    private int circleColor;
    private int progress;
    private RectF mRect = new RectF();

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, defStyleAttr);
    }

    private void initView(Context context, int defStyleAttr) {
        circleColor = context.getResources().getColor(R.color.tabIndicate_color);
        padding = DensityUtils.dp2px(context, 4);
        mPaint = new Paint();
        mPaint.setColor(circleColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        mRect.set(padding, padding, width - padding, height - padding);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 || height == 0) {
            return;
        }

        canvas.drawArc(mRect, progress, 180, false, mPaint);
        progress += 5;
        if (progress > 360) {
            progress = 0;
        }

        invalidate();
    }
}
