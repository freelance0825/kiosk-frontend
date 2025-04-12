package com.fmv.healthkiosk.ui.home.test.widgets.bloodsugar;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class BloodSugarTestViewModel extends BaseViewModel {

    public MutableLiveData<String> selectedTemperatureType = new MutableLiveData<>("°F");
    public MutableLiveData<Float> bloodSugarData = new MutableLiveData<>(0F);
    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public BloodSugarTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            float targetTemp = "°F".equals(selectedTemperatureType.getValue()) ? 98F : 37F;
            float currentTemp = bloodSugarData.getValue() != null ? bloodSugarData.getValue() : 0F;

            if (Math.abs(currentTemp - targetTemp) > 0.1F) { // Perubahan kecil lebih realistis
                float step = 1F;
                float newTemp = currentTemp + (targetTemp > currentTemp ? step : -step);

                bloodSugarData.setValue(Float.parseFloat(String.format(Locale.US, "%.1f", newTemp)));

                handler.postDelayed(this, 100);
            } else {
                bloodSugarData.setValue(targetTemp);
            }
        }
    };


    public void startUpdatingBloodSugar() {
        stopUpdatingBloodSugar();
        handler.post(updateRunnable);
    }

    public void stopUpdatingBloodSugar() {
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    public void resetBloodSugar() {
        stopUpdatingBloodSugar();
        bloodSugarData.setValue(0F);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
