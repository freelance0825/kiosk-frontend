package com.fmv.healthkiosk.ui.home.test.widgets.ecg;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EcgTestViewModel extends BaseViewModel {

    final MutableLiveData<Float> ecgDataI = new MutableLiveData<>();
    final MutableLiveData<Float> ecgDataII = new MutableLiveData<>();
    final MutableLiveData<Float> ecgDataIII = new MutableLiveData<>();

    final MutableLiveData<Integer> bpm = new MutableLiveData<>(75);
    final MutableLiveData<String> recordingTime = new MutableLiveData<>("00:00");

    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private boolean isRunning = false;
    private Handler handler;
    private Runnable ecgRunnable;

    @Inject
    public EcgTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        handler = new Handler(Looper.getMainLooper());
    }

    public void startECGSimulation() {
        if (isRunning) return;
        isRunning = true;

        long startTime = System.currentTimeMillis(); // Record the start time
        final long[] lastBpmUpdateTime = {startTime}; // Track last BPM update

        ecgRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                // Update each ECG individually (every 40ms)
                ecgDataI.setValue(200 + new Random().nextFloat() * 200);
                ecgDataII.setValue(200 + new Random().nextFloat() * 200);
                ecgDataIII.setValue(200 + new Random().nextFloat() * 200);

                // Update BPM only once per second
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastBpmUpdateTime[0] >= 1000) { // Every 1000ms (1 sec)
                    bpm.setValue(70 + new Random().nextInt(10)); // Random BPM 70-80
                    lastBpmUpdateTime[0] = currentTime;
                }

                // Correctly update recording time based on real elapsed time
                long elapsedMillis = currentTime - startTime;
                int minutes = (int) (elapsedMillis / 60000);
                int sec = (int) ((elapsedMillis / 1000) % 60);
                recordingTime.setValue(String.format(Locale.getDefault(), "%02d:%02d", minutes, sec));

                handler.postDelayed(this, 100);
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
        ecgDataI.setValue(0f);
        ecgDataII.setValue(0f);
        ecgDataIII.setValue(0f);
        bpm.setValue(0);
        recordingTime.setValue("00:00");
    }
}
