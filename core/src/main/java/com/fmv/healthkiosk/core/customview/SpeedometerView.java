package com.fmv.healthkiosk.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import android.graphics.*;

import android.animation.ValueAnimator;

public class SpeedometerView extends View {
    private float value = 0;
    private float animatedValue = 0;
    private Paint arcPaint, needlePaint, textPaint;
    private RectF arcBounds, innerArcBounds;
    private ValueAnimator animator;

    // Variabel untuk teks start dan end
    private String startLabel = "0";
    private String endLabel = "100";
    private boolean showLabels = true;
    private int textColor = Color.parseColor("#F5F5F5");
    private float textSize = 40;

    public SpeedometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(20);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        needlePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        arcBounds = new RectF();
        innerArcBounds = new RectF();
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
        canvas.drawArc(innerArcBounds, 160, 220, false, innerArcPaint);

        // **Draw main arc**
        canvas.drawArc(arcBounds, 160, 220, false, arcPaint);

        // **Compute needle angle**
        float angle = 160 + (animatedValue / 100) * 220;

        // **Needle dimensions**
        float needleLength = radius * 1.2f;
        float needleWidth = 32;
        float cornerRadius = needleWidth / 2f;

        // **Needle endpoint**
        float needleEndX = (float) (centerX + needleLength * Math.cos(Math.toRadians(angle)));
        float needleEndY = (float) (centerY + needleLength * Math.sin(Math.toRadians(angle)));

        // **Needle gradient**
        Paint needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        needlePaint.setShader(new LinearGradient(
                centerX, centerY,
                needleEndX, needleEndY,
                new int[]{Color.TRANSPARENT, Color.TRANSPARENT, 0xFFD9D9D9},
                null,
                Shader.TileMode.CLAMP
        ));

        // **Needle path**
        float dx = (float) (needleWidth / 2 * Math.sin(Math.toRadians(angle)));
        float dy = (float) (needleWidth / 2 * Math.cos(Math.toRadians(angle)));

        Path needlePath = new Path();
        needlePath.moveTo(centerX - dx, centerY + dy);
        needlePath.lineTo(centerX + dx, centerY - dy);
        needlePath.lineTo(needleEndX + dx, needleEndY - dy);
        needlePath.lineTo(needleEndX - dx, needleEndY + dy);
        needlePath.close();

        // **Rounded tip for needle**
        needlePath.addCircle(needleEndX, needleEndY, cornerRadius, Path.Direction.CW);

        // **Draw needle**
        canvas.drawPath(needlePath, needlePaint);

        // **Tampilkan teks jika tidak disembunyikan**
        if (showLabels) {
            canvas.drawText(startLabel, arcBounds.left + 40, arcBounds.bottom - 100, textPaint);
            canvas.drawText(endLabel, arcBounds.right - 40, arcBounds.bottom - 100, textPaint);
        }
    }
}
