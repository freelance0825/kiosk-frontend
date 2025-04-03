package com.fmv.healthkiosk.core.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class HeightScaleView extends View {
    private static final int DEFAULT_MAX_VALUE = 1000;
    private static final int LINE_SPACING = 40;
    private static final int LONG_LINE_HEIGHT = 40;
    private static final int SHORT_LINE_HEIGHT = 20;
    private static final int ANIMATION_DURATION = 300;

    private int maxValue = DEFAULT_MAX_VALUE;
    private float selectedValue = 100F; // Default selected value
    private float offsetY = 0;

    private static final int TEXT_PADDING = 100; // Padding dari ruler ke teks

    private static final float MAX_TEXT_SIZE = 80f; // Ukuran font terbesar
    private static final float MIN_TEXT_SIZE = 40f; // Ukuran font terkecil
    private static final float SCALE_DISTANCE = 200f; // Jarak efek scaling

    private Paint linePaint, textPaint, indicatorPaint, backgroundPaint, overlayPaint;

    private ValueAnimator animator;

    public HeightScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(5);
        linePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.RIGHT);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(Color.RED);
        indicatorPaint.setStrokeWidth(8);
        indicatorPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint();

        overlayPaint = new Paint();
        overlayPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // Terapkan gradient ke background
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, height,
                new int[]{Color.TRANSPARENT, Color.parseColor("#03F8B2"), Color.parseColor("#395FC2"), Color.TRANSPARENT},
                new float[]{0f, 0.4f, 0.6f, 1f}, // Posisi transisi warna
                Shader.TileMode.CLAMP
        );
        backgroundPaint.setShader(gradient);

        canvas.drawRect(centerX - 80, 0, centerX + 80, height, backgroundPaint);

        // Ukuran rectangle di tengah
        float rectWidth = 200;
        float rectHeight = 30;
        float rectLeft = centerX - rectWidth / 2;
        float rectTop = centerY - rectHeight / 2;
        float rectRight = centerX + rectWidth / 2;
        float rectBottom = centerY + rectHeight / 2;
        float cornerRadius = rectHeight / 2; // Membuat sudut kiri melengkung

// Gradient dari kiri (#D9D9D9) ke kanan (transparent)
        LinearGradient rectGradient = new LinearGradient(
                rectLeft, 0, rectRight, 0,
                new int[]{Color.parseColor("#D9D9D9"), Color.TRANSPARENT},
                new float[]{0f, 0.8f}, // Posisi transisi warna
                Shader.TileMode.CLAMP
        );
        overlayPaint.setShader(rectGradient);
//        overlayPaint.setShader(null);
//        overlayPaint.setColor(Color.parseColor("#D9D9D9"));

        // Calculate the offset to center the selected value
        float startY = centerY + offsetY;

        for (int i = 0; i <= maxValue; i++) {
            float y = startY - (i * LINE_SPACING);

            if (y < 0) break; // Stop if reaching the top
            if (y > height) continue; // Skip lines below the view

            float distanceFromCenter = Math.abs(y - centerY);
            float scaleFactor = Math.max(0, 1 - (distanceFromCenter / SCALE_DISTANCE)); // 1 di tengah, 0 di luar batas
            float textSize = MIN_TEXT_SIZE + (MAX_TEXT_SIZE - MIN_TEXT_SIZE) * scaleFactor; // Interpolasi ukuran teks

            textPaint.setTextSize(textSize);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            linePaint.setStrokeWidth(5 * scaleFactor); // Interpolasi lebar garis
            indicatorPaint.setStrokeWidth(8 * scaleFactor); // Interpolasi lebar indikator
            indicatorPaint.setColor(Color.argb((int) (255 * scaleFactor), 255, 0, 0)); // Interpolasi warna indikator
            indicatorPaint.setAlpha((int) (255 * scaleFactor)); // Interpolasi transparansi indikator

            if (i % 5 == 0) {
                canvas.drawLine(centerX - LONG_LINE_HEIGHT, y, centerX + LONG_LINE_HEIGHT, y, linePaint);
                canvas.drawText(String.valueOf(i), centerX - LONG_LINE_HEIGHT - TEXT_PADDING, y + 10, textPaint);
            } else {
                canvas.drawLine(centerX - SHORT_LINE_HEIGHT, y, centerX + SHORT_LINE_HEIGHT, y, linePaint);
            }
        }

        // Draw selection indicator
        RectF rect = new RectF(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, overlayPaint);
//        canvas.drawCircle(rectLeft + cornerRadius, centerY, cornerRadius, overlayPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                int newSelectedValue = (int) ((getHeight() - newY) / LINE_SPACING);
                setSelectedValue(newSelectedValue);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setSelectedValue(float value) {
        if (value < 0) value = 0;
        if (value > maxValue) value = maxValue;

        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(offsetY, value * LINE_SPACING);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            offsetY = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();

        selectedValue = value;
    }

    public float getSelectedValue() {
        return selectedValue;
    }
}
