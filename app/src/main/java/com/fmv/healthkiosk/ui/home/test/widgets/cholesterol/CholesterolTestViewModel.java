package com.fmv.healthkiosk.ui.home.test.widgets.cholesterol;

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
public class CholesterolTestViewModel extends BaseViewModel {

    public MutableLiveData<Float> heightData = new MutableLiveData<>(0F);
    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public CholesterolTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            float targetTemp = 165;
            float currentTemp = heightData.getValue() != null ? heightData.getValue() : 0F;

            if (Math.abs(currentTemp - targetTemp) > 1F) {
                float step = 1F;
                float newTemp = currentTemp + (targetTemp > currentTemp ? step : -step);

                heightData.setValue(Float.parseFloat(String.format(Locale.US, "%.1f", newTemp)));

                handler.postDelayed(this, 100);
            } else {
                heightData.setValue(targetTemp);
            }
        }
    };


    public void startUpdatingHeight() {
        stopUpdatingHeight();
        handler.post(updateRunnable);
    }

    public void stopUpdatingHeight() {
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    public void resetHeight() {
        stopUpdatingHeight();
        heightData.setValue(0F);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
