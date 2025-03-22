package com.fmv.healthkiosk.ui.home.test.widgets.oximeter;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.ui.home.test.widgets.bluetooth.OximeterManager;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OximeterTestViewModel extends BaseViewModel implements OximeterManager.OximeterListener {

    public MutableLiveData<Integer> oxygenData = new MutableLiveData<>(0);
    public MutableLiveData<Integer> pulseData = new MutableLiveData<>(0);
    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private final OximeterManager oximeterSimulator;

    @Inject
    public OximeterTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        oximeterSimulator = new OximeterManager(this);
    }

    public void startSimulatedOximeter() {
        oximeterSimulator.connectDevice();
    }

    public void stopSimulatedOximeter() {
        oximeterSimulator.disconnectDevice();
    }

    public void resetOximeter() {
        stopSimulatedOximeter();
        oxygenData.setValue(0);
        pulseData.setValue(0);
    }

    @Override
    public void onDeviceConnected(String message) {
        Log.e("FTEST", "onDeviceConnected: Connected to Oximeter via USB");
    }

    @Override
    public void onDataReceived(String hexData) {
        try {
            String[] values = hexData.split(",");
            int oxygen = Integer.parseInt(values[0], 16);
            int pulse = Integer.parseInt(values[1], 16);

            oxygenData.postValue(oxygen);
            pulseData.postValue(pulse);
        } catch (Exception e) {
            oxygenData.postValue(0);
            pulseData.postValue(0);
        }
    }

    @Override
    public void onError(String error) {
        Log.e("FTEST", "onError: " + error );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopSimulatedOximeter();
    }
}


//@HiltViewModel
//public class OximeterTestViewModel extends BaseViewModel {
//
//    public MutableLiveData<Integer> oxygenData = new MutableLiveData<>(0);
//    public MutableLiveData<Integer> pulseData = new MutableLiveData<>(0);
//    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);
//
//    private final Handler handler = new Handler(Looper.getMainLooper());
//    private final CompositeDisposable disposables = new CompositeDisposable();
//
//    @Inject
//    public OximeterTestViewModel(SavedStateHandle savedStateHandle) {
//        super(savedStateHandle);
//    }
//
//    private final Runnable updateRunnable = new Runnable() {
//        @Override
//        public void run() {
//            int targetTemp = 100;
//            int currentTemp = oxygenData.getValue() != null ? oxygenData.getValue() : 0;
//
//            if (Math.abs(currentTemp - targetTemp) > 1F) {
//                int step = 1;
//                int newTemp = currentTemp + (targetTemp > currentTemp ? step : -step);
//
//                int stepPulse = 5;
//                int newPulse = currentTemp + (targetTemp > currentTemp ? stepPulse : -stepPulse);
//
//                oxygenData.setValue(newTemp);
//                pulseData.setValue(newPulse);
//
//                handler.postDelayed(this, 100);
//            } else {
//                oxygenData.setValue(targetTemp);
//                pulseData.setValue(targetTemp);
//            }
//        }
//    };
//
//
//    public void startUpdatingOximeter() {
//        stopUpdatingOximeter();
//        handler.post(updateRunnable);
//    }
//
//    public void stopUpdatingOximeter() {
//        if (handler != null && updateRunnable != null) {
//            handler.removeCallbacks(updateRunnable);
//        }
//    }
//
//    public void resetOximeter() {
//        stopUpdatingOximeter();
//        oxygenData.setValue(0);
//        pulseData.setValue(0);
//    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        disposables.clear();
//    }
//}
