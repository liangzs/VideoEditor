package com.qiusuo.videoeditor.ui.widgegt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.qiusuo.videoeditor.R;


/**
 * @ author       lfj
 * @ date         2020/11/27
 * @ description
 */
public class DownloadProgressView extends View  {

//    private int state = Downloader.STATE_DEFAULT;

    private Paint backgroundPaint, progressBgPaint, progressPaint, textPaint;
    private float progressWidth;
    private float progress;
    private String message;
    private Rect textRect;

    public DownloadProgressView(Context context) {
        super(context, null);
    }

    public DownloadProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setColor(0x4D000000);

        progressBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStyle(Paint.Style.STROKE);
        progressBgPaint.setColor(Color.WHITE);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(getResources().getColor(R.color.theme_color));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);

        textRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        progressWidth = w / 20f;
        progressBgPaint.setStrokeWidth(progressWidth - 0.5f);
        progressPaint.setStrokeWidth(progressWidth);
        textPaint.setTextSize(w / 3f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (STATE_LOADED == state || Downloader.STATE_DEFAULT == state) {
//            return;
//        }
        float radius = getWidth() / 2f;
        drawBackground(canvas, radius);
        drawProgressBg(canvas, radius);
//        if (Downloader.STATE_WAITING == state) {
//            drawProgress(canvas, 0);
//            drawText(canvas, radius, 0);
//        } else if (Downloader.STATE_LOADING == state) {
//            drawProgress(canvas, progress);
//            drawText(canvas, radius, progress);
//        }
    }

    private void drawBackground(Canvas canvas, float radius) {
        canvas.drawCircle(radius, radius, radius - 0.5f, backgroundPaint);
    }

    private void drawProgressBg(Canvas canvas, float radius) {
        canvas.drawCircle(radius, radius, radius - progressWidth / 2f, progressBgPaint);
    }

    private void drawProgress(Canvas canvas, float progress) {
        canvas.drawArc(new RectF(progressWidth / 2f, progressWidth / 2f,
                        getWidth() - progressWidth / 2f, getHeight() - progressWidth / 2f),
                270, progress * 360, false, progressPaint);
    }

    private void drawText(Canvas canvas, float radius, float progress) {
        message = (int) (progress * 100) + "%";
        textPaint.getTextBounds(message, 0, message.length(), textRect);
        canvas.drawText(message, radius, radius + textRect.height() / 2f, textPaint);
    }

//    public int getState() {
//        return state;
//    }
//
//    @Override
//    public void setState(int state) {
////        if (this.state != state) {
//            this.state = state;
//            if (state == STATE_LOADED || state == Downloader.STATE_DEFAULT) {
//                setVisibility(View.GONE);
//            } else {
//                setVisibility(View.VISIBLE);
//            }
//            invalidate();
////        }
//    }
//
//
//    @Override
//    public void setProgress(float progress) {
//        this.progress = progress;
//        invalidate();
//    }

    public float getProgress() {
        return progress;
    }
}
