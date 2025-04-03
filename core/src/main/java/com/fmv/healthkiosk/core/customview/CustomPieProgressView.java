package com.fmv.healthkiosk.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import android.animation.ValueAnimator;

public class CustomPieProgressView extends View {
    private Paint progressPaint, backgroundPaint;
    private RectF rectF;
    private int progress = 0; // Progress saat ini (0 - 100)
    private int animatedProgress = 0; // Progress yang dianimasikan
    private int startAngle = -90; // Mulai dari atas
    private final int strokeWidth = 40; // Lebar garis progress
    private final int padding = strokeWidth / 2; // Padding agar tidak terpotong

    public CustomPieProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth - 20);
        progressPaint.setStrokeCap(Paint.Cap.ROUND); // Ujung rounded
        progressPaint.setAntiAlias(true);

        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectF.set(padding, padding, w - padding, h - padding);

        backgroundPaint.setShader(new LinearGradient(
                0, 0, w, 0,
                new int[]{0x6606FFB8, 0x664085E1, 0x6604ECAA}, // Warna dengan alpha 40%
                null,
                Shader.TileMode.CLAMP
        ));

        progressPaint.setShader(new LinearGradient(
                0, 0, w, 0,
                new int[]{0xFF06FFB8, 0xFF4085E1, 0xFF04ECAA}, // Warna solid
                null,
                Shader.TileMode.CLAMP
        ));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(rectF, 0, 360, false, backgroundPaint);

        float sweepAngle = (animatedProgress / 100f) * 360;
        canvas.drawArc(rectF, startAngle, sweepAngle, false, progressPaint);
    }

    public void setProgress(int progress) {
        progress = Math.min(100, Math.max(0, progress)); // Pastikan dalam range 0-100

        ValueAnimator animator = ValueAnimator.ofInt(animatedProgress, progress);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            animatedProgress = (int) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();

        this.progress = progress;
    }
}
