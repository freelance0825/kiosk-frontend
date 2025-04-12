package com.fmv.healthkiosk.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CustomDonutChartView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;

    private float progress = 75f; // percentage (0-100)
    private float strokeWidth = 50f;

    public CustomDonutChartView(Context context) {
        super(context);
        init();
    }

    public CustomDonutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDonutChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.parseColor("#F1F2F3")); // light gray
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.parseColor("#3E66F3")); // blue
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = Math.min(getWidth(), getHeight());
        float padding = strokeWidth / 2f;
        RectF rect = new RectF(padding, padding, size - padding, size - padding);

        // Draw background arc (full circle)
        canvas.drawArc(rect, 0, 360, false, backgroundPaint);

        // Draw progress arc
        float sweepAngle = 360 * (progress / 100f);
        canvas.drawArc(rect, -90, sweepAngle, false, progressPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setStrokeWidth(float widthPx) {
        this.strokeWidth = widthPx;
        backgroundPaint.setStrokeWidth(widthPx);
        progressPaint.setStrokeWidth(widthPx);
        invalidate();
    }
}
