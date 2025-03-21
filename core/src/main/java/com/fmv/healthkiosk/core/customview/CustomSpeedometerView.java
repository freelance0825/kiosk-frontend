package com.fmv.healthkiosk.core.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomSpeedometerView extends View {
    private float value = 0;
    private float animatedValue = 0;
    private Paint arcPaint, textPaint;
    private RectF arcBounds, innerArcBounds;
    private ValueAnimator animator;

    // Variabel untuk teks start dan end
    private String startLabel = "0";
    private String endLabel = "100";
    private boolean showLabels = true;
    private int textColor = Color.parseColor("#F5F5F5");
    private float textSize = 40;

    private Paint progressPaint, backgroundPaint;

    private final int strokeWidth = 40; // Lebar garis progress

    private int animatedProgress = 0; // Progress yang dianimasikan

    private int progress = 0; // Progress saat ini (0 - 100)


    public CustomSpeedometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(20);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        arcBounds = new RectF();
        innerArcBounds = new RectF();

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth - 20);
        progressPaint.setStrokeCap(Paint.Cap.ROUND); // Ujung rounded
        progressPaint.setAntiAlias(true);

    }

    public void setValue(float newValue) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        if (newValue < 0F || newValue > 100F) return;

        animator = ValueAnimator.ofFloat(animatedValue, newValue);
        animator.setDuration(500);
        animator.addUpdateListener(animation -> {
            animatedValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    public void setStartLabel(String label) {
        this.startLabel = label;
        invalidate();
    }

    public void setEndLabel(String label) {
        this.endLabel = label;
        invalidate();
    }

    public void setShowLabels(boolean show) {
        this.showLabels = show;
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        textPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float size) {
        this.textSize = size;
        textPaint.setTextSize(size);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = 30;

//        rectF.set(padding, padding, w - padding, h - padding);

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

        arcBounds.set(padding, padding, w - padding, h - padding);
        innerArcBounds.set(padding + 20, padding + 20, w - padding - 20, h - padding - 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2f;
        float centerY = arcBounds.centerY();
        float radius = Math.min(width, height) / 2.5f;

        // **Gradasi warna untuk arc speedometer**
        LinearGradient gradient = new LinearGradient(
                arcBounds.left, arcBounds.top, arcBounds.right, arcBounds.top,
                new int[]{0xFF06FFB8, 0xFF5BEAFF, 0xFFEDAB33},
                null,
                Shader.TileMode.CLAMP
        );

        // **Inner arc (wider and 50% transparent)**
        Paint innerArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerArcPaint.setStyle(Paint.Style.STROKE);
        innerArcPaint.setStrokeWidth(arcPaint.getStrokeWidth() * 2);
        innerArcPaint.setShader(gradient);
        innerArcPaint.setAlpha(128);

        // **Outer arc (original)**
        arcPaint.setShader(gradient);

        // **Draw inner arc first**
        canvas.drawArc(arcBounds, 160, 220, false, backgroundPaint);

        // **Draw main arc**
        float sweepAngle = (animatedProgress / 100f) * 220;
        canvas.drawArc(arcBounds, 160, sweepAngle, false, progressPaint);

        // **Tampilkan teks jika tidak disembunyikan**
        if (showLabels) {
            canvas.drawText(startLabel, arcBounds.left + 40, arcBounds.bottom - 100, textPaint);
            canvas.drawText(endLabel, arcBounds.right - 40, arcBounds.bottom - 100, textPaint);
        }
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
