package com.fmv.healthkiosk.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class CustomRangeBarView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint tickPaint;
    private Paint textPaint;

    private int barHeight = 20;
    private int tickHeight = 20;
    private float textSize = 36;

    private int startTickValue = 100;
    private int endTickValue = 120;
    private String centerLabel = "Normal";

    // Progress start and end range (0.0 to 1.0)
    private float startRatio = 0.25f;
    private float endRatio = 0.75f;

    public CustomRangeBarView(Context context) {
        super(context);
        init();
    }

    public CustomRangeBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRangeBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.parseColor("#333333"));
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setColor(Color.WHITE);
        tickPaint.setStrokeWidth(4);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();

        // Padding from the top
        int paddingTop = 20;

        // Draw bar near the top
        int barY = paddingTop;
        canvas.drawRoundRect(0, barY, width, barY + barHeight, barHeight / 2, barHeight / 2, backgroundPaint);

        // Gradient shader for progress
        int startX = (int) (width * startRatio);
        int endX = (int) (width * endRatio);
        Shader shader = new LinearGradient(
                startX, 0, endX, 0,
                new int[]{Color.parseColor("#00FFD1"), Color.parseColor("#337DFF")},
                null,
                Shader.TileMode.CLAMP
        );
        progressPaint.setShader(shader);

        canvas.drawRoundRect(startX, barY, endX, barY + barHeight, barHeight / 2, barHeight / 2, progressPaint);

        // Tick lines under the bar
        int tickStartY = barY + barHeight + 20;
        int tickEndY = tickStartY + tickHeight;
        canvas.drawLine(startX, tickStartY, startX, tickEndY, tickPaint);
        canvas.drawLine(endX, tickStartY, endX, tickEndY, tickPaint);

        // Text labels under tick lines
        float textY = tickEndY + textSize + 10;
        canvas.drawText(String.valueOf(startTickValue), startX, textY, textPaint);
        canvas.drawText(String.valueOf(endTickValue), endX, textY, textPaint);

        float centerX = (startX + endX) / 2f;
        canvas.drawText(centerLabel, centerX, textY, textPaint);
    }


    // Optional setters for customization
    public void setTickValues(int start, int end) {
        this.startTickValue = start;
        this.endTickValue = end;
        invalidate();
    }

    public void setCenterLabel(String label) {
        this.centerLabel = label;
        invalidate();
    }

    public void setProgressRange(float startRatio, float endRatio) {
        this.startRatio = startRatio;
        this.endRatio = endRatio;
        invalidate();
    }


    public void setLabelTextSize(float sizeInPx) {
        this.textSize = sizeInPx;
        textPaint.setTextSize(sizeInPx);
        invalidate();
    }
}
