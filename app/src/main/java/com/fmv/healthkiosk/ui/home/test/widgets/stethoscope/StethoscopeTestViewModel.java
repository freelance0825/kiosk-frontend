package com.fmv.healthkiosk.ui.home.test.widgets.stethoscope;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.ui.home.test.widgets.bluetooth.OximeterManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StethoscopeTestViewModel extends BaseViewModel {

    private final MutableLiveData<Float> ecgData1 = new MutableLiveData<>();
    private final MutableLiveData<Float> ecgData2 = new MutableLiveData<>();
    private final MutableLiveData<Float> ecgData3 = new MutableLiveData<>();
    private final MutableLiveData<Float> ecgData4 = new MutableLiveData<>();
    private final MutableLiveData<Integer> bpm = new MutableLiveData<>(75);
    private final MutableLiveData<String> recordingTime = new MutableLiveData<>("00:00");

    private boolean isRunning = false;
    private Handler handler;
    private Runnable ecgRunnable;
    private int sampleIndex = 0;
    private List<Float> ecgSamples;

    @Inject
    public StethoscopeTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        handler = new Handler(Looper.getMainLooper());
        ecgSamples = generateECGSamples(500);
    }

    /** Generate a sinusoidal ECG-like wave */
    private List<Float> generateECGSamples(int samples) {
        List<Float> data = new ArrayList<>();
        for (int i = 0; i < samples; i++) {
            double t = i * 0.02;
            float value = (float) (Math.sin(2 * Math.PI * 1.0 * t) * 300);
            data.add(value);
        }
        return data;
    }

    /** Start ECG simulation */
    public void startECGSimulation() {
        if (isRunning) return;
        isRunning = true;

        ecgRunnable = new Runnable() {
            int seconds = 0;
            @Override
            public void run() {
                if (!isRunning) return;

                // Update each ECG individually
                ecgData1.setValue(ecgSamples.get(sampleIndex % ecgSamples.size()));
                ecgData2.setValue(ecgSamples.get((sampleIndex + 50) % ecgSamples.size()));
                ecgData3.setValue(ecgSamples.get((sampleIndex + 100) % ecgSamples.size()));
                ecgData4.setValue(ecgSamples.get((sampleIndex + 150) % ecgSamples.size()));

                sampleIndex++;

                bpm.setValue(70 + new Random().nextInt(10)); // Random BPM 70-80

                // Update recording time
                int minutes = seconds / 60;
                int sec = seconds % 60;
                recordingTime.setValue(String.format(Locale.getDefault(), "%02d:%02d", minutes, sec));
                seconds++;

                handler.postDelayed(this, 40); // Update every 40ms for smooth ECG
            }
        };

        handler.post(ecgRunnable);
    }

    public void stopECGSimulation() {
        isRunning = false;
        handler.removeCallbacks(ecgRunnable);
    }

    public void resetECG() {
        isRunning = false;
        handler.removeCallbacks(ecgRunnable);
        ecgData1.setValue(0f);
        ecgData2.setValue(0f);
        ecgData3.setValue(0f);
        ecgData4.setValue(0f);
        bpm.setValue(75);
        recordingTime.setValue("00:00");
    }

    public LiveData<Float> getEcgData1() { return ecgData1; }
    public LiveData<Float> getEcgData2() { return ecgData2; }
    public LiveData<Float> getEcgData3() { return ecgData3; }
    public LiveData<Float> getEcgData4() { return ecgData4; }
    public LiveData<Integer> getBpm() { return bpm; }
    public LiveData<String> getRecordingTime() { return recordingTime; }
}
