package com.fmv.healthkiosk.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.fmv.healthkiosk.core.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ECGView extends View {
    private Paint paintECG, paintGrid, paintText;
    private List<Float> ecgData;
    private float maxY = 200, minY = 0; // Rentang nilai ECG
    private float timeStep = 10f; // Jarak antar titik ECG dalam px
    private float yAxisWidth = 80f; // Lebar area angka di kiri
    private float stepY = 50; // Langkah untuk label Y (bisa diubah)

    public ECGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public void setStepY(float stepY) {
        this.stepY = stepY;
    }


    private void init() {
        setBackgroundColor(Color.TRANSPARENT);

        // Garis ECG
        paintECG = new Paint();
        paintECG.setColor(Color.parseColor("#58FFCF"));
        paintECG.setStrokeWidth(4);
        paintECG.setAntiAlias(true);

        // Garis Grid
        paintGrid = new Paint();
        paintGrid.setColor(Color.parseColor("#CC34414E"));
        paintGrid.setStrokeWidth(2);

        // Text Label (angka di kiri)
        paintText = new Paint();
        paintText.setColor(Color.parseColor("#CC34414E"));
        paintText.setTextSize(20);
        paintText.setAntiAlias(true);

        ecgData = new ArrayList<>();
    }

    public void addECGData(float value) {
        ecgData.add(value);

        // Batasi jumlah data agar tidak terlalu panjang
        if (ecgData.size() > (getWidth() - yAxisWidth) / timeStep) {
            ecgData.remove(0);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);
        drawECG(canvas);
        drawYAxisLabels(canvas);
    }

    private void drawGrid(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float gridSpacingX = 100;
        float gridSpacingY = height / ((maxY - minY) / stepY); // Sesuai stepY

        // Gambar garis vertikal (mulai setelah yAxisWidth)
        for (float x = yAxisWidth; x < width; x += gridSpacingX) {
            canvas.drawLine(x, 0, x, height, paintGrid);
        }

        // Gambar garis horizontal (termasuk atas & bawah)
        for (float y = 0; y <= height; y += gridSpacingY) {
            canvas.drawLine(yAxisWidth, y, width, y, paintGrid);
        }
    }

    private void drawECG(Canvas canvas) {
        if (ecgData.size() < 2) return;

        float x = yAxisWidth;
        for (int i = 1; i < ecgData.size(); i++) {
            float y1 = mapECGValue(ecgData.get(i - 1));
            float y2 = mapECGValue(ecgData.get(i));

            canvas.drawLine(x, y1, x + timeStep, y2, paintECG);
            x += timeStep;
        }
    }

    private void drawYAxisLabels(Canvas canvas) {
        float height = getHeight();
        float numSteps = (maxY - minY) / stepY; // Total label
        float spacingY = height / numSteps; // Jarak antar label

        for (int i = 0; i <= numSteps; i++) {
            float value = minY + (i * stepY); // Nilai label (0, 50, 100, dst.)
            float yPos = height - (i * spacingY); // Dari bawah ke atas
            canvas.drawText(String.valueOf((int) value), 10, yPos, paintText);
        }
    }

    private float mapECGValue(float value) {
        return getHeight() - ((value - minY) / (maxY - minY) * getHeight());
    }
}
