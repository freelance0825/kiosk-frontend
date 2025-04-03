package com.fmv.healthkiosk.ui.home.test.widgets.bloodpressure;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class BloodPressureTestViewModel extends BaseViewModel {

    public MutableLiveData<Integer> pulseData = new MutableLiveData<>(0);
    public MutableLiveData<Integer> systolicData = new MutableLiveData<>(0);
    public MutableLiveData<Integer> diastolicData = new MutableLiveData<>(0);

    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public BloodPressureTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            int targetTemp = 100;
            int currentTemp = pulseData.getValue() != null ? pulseData.getValue() : 0;

            if (Math.abs(currentTemp - targetTemp) > 1F) {
                int step = 1;
                int newTemp = currentTemp + (targetTemp > currentTemp ? step : -step);

                int stepSystolic = 5;
                int newSystolic = currentTemp + (targetTemp > currentTemp ? stepSystolic : -stepSystolic);

                int stepDiastolic = 5;
                int newDiastolic = currentTemp + (targetTemp > currentTemp ? stepDiastolic : -stepDiastolic);

                pulseData.setValue(newTemp);
                systolicData.setValue(newSystolic);
                diastolicData.setValue(newDiastolic);

                handler.postDelayed(this, 100);
            } else {
                pulseData.setValue(targetTemp);
                systolicData.setValue(targetTemp);
                diastolicData.setValue(targetTemp);
            }
        }
    };


    public void startUpdatingOximeter() {
        stopUpdatingOximeter();
        handler.post(updateRunnable);
    }

    public void stopUpdatingOximeter() {
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    public void resetOximeter() {
        stopUpdatingOximeter();
        pulseData.setValue(0);
        systolicData.setValue(0);
        diastolicData.setValue(0);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
