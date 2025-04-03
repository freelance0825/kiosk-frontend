package com.fmv.healthkiosk.ui.home.test.widgets.temperature;

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
public class TemperatureTestViewModel extends BaseViewModel {

    public MutableLiveData<String> selectedTemperatureType = new MutableLiveData<>("°F");
    public MutableLiveData<Float> temperatureData = new MutableLiveData<>(0F);
    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TemperatureTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            float targetTemp = "°F".equals(selectedTemperatureType.getValue()) ? 98F : 37F;
            float currentTemp = temperatureData.getValue() != null ? temperatureData.getValue() : 0F;

            if (Math.abs(currentTemp - targetTemp) > 0.1F) { // Perubahan kecil lebih realistis
                float step = 0.1F;
                float newTemp = currentTemp + (targetTemp > currentTemp ? step : -step);

                temperatureData.setValue(Float.parseFloat(String.format(Locale.US, "%.1f", newTemp)));

                handler.postDelayed(this, 100);
            } else {
                temperatureData.setValue(targetTemp);
            }
        }
    };


    public void startUpdatingTemperature() {
        stopUpdatingTemperature();
        handler.post(updateRunnable);
    }

    public void stopUpdatingTemperature() {
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    public void resetTemperature() {
        stopUpdatingTemperature();
        temperatureData.setValue(0F);
    }

    public String getCurrentTemperatureCategory() {
        float temp = temperatureData.getValue() != null ? temperatureData.getValue() : 0F;
        boolean isCelsius = "°C".equals(selectedTemperatureType.getValue());

        float normalMin = isCelsius ? 36F : 96.8F;
        float normalMax = isCelsius ? 37.5F : 99.5F;
        float mediumMax = isCelsius ? 39F : 102.2F;
        float highMax = isCelsius ? 42F : 107.6F;

        if (temp >= highMax) return "Highest Point";
        if (temp >= mediumMax) return "High";
        if (temp >= normalMax) return "Medium";
        return "Normal";
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
