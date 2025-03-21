package com.fmv.healthkiosk.ui.home.test.widgets.weight;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.lefu.ppbase.PPDeviceModel;
import com.peng.ppscale.business.state.PPBleWorkState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class WeightTestViewModel extends BaseViewModel implements LeFuModule.LeFuListener {

    public MutableLiveData<String> selectedWeightType = new MutableLiveData<>("KG");
    public MutableLiveData<Float> weightData = new MutableLiveData<>(0F);
    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final LeFuModule leFuModule;

    @Inject
    public WeightTestViewModel(SavedStateHandle savedStateHandle, Context context) {
        super(savedStateHandle);
        leFuModule = new LeFuModule(context, this);
    }

    public void startScanning() {
        leFuModule.scanDevice();
    }

    public void startMeasuring() {
        leFuModule.startMeasuring();
    }

    public void stopMeasuring() {
        leFuModule.controllerDisconnect();
    }

    public void resetWeight() {
        weightData.setValue(0F);
    }

    public String getCurrentWeightCategory() {
        float weight = weightData.getValue() != null ? weightData.getValue() : 0F;
        float normalMax = 90F, mediumMax = 150F, highMax = 250F;

        if (weight >= highMax) return "Extreme Obesity";
        if (weight >= mediumMax) return "High";
        if (weight >= normalMax) return "Medium";
        return "Normal";
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    @Override
    public void onDeviceFound(PPDeviceModel device) {
        Log.e("WeightTestViewModel", "Device found: " + device.getDeviceName());
    }

    @Override
    public void onBluetoothStateChanged(PPBleWorkState state) {
        Log.e("WeightTestViewModel", "Bluetooth state: " + state);
    }

    @Override
    public void onWeightDataReceived(String weight) {
        weightData.postValue(Float.parseFloat(weight));
    }

    @Override
    public void onLockDataReceived(String weight) {
        weightData.postValue(Float.parseFloat(weight));
    }

    @Override
    public void onError(String message) {
        Log.e("WeightTestViewModel", "Error: " + message);
    }
}


//@HiltViewModel
//public class WeightTestViewModel extends BaseViewModel {
//
//    public MutableLiveData<String> selectedWeightType = new MutableLiveData<>("KG");
//    public MutableLiveData<Float> weightData = new MutableLiveData<>(0F);
//    public MutableLiveData<Boolean> isHeaderShowed = new MutableLiveData<>(false);
//
//    private final Handler handler = new Handler(Looper.getMainLooper());
//    private final CompositeDisposable disposables = new CompositeDisposable();
//
//    @Inject
//    public WeightTestViewModel(SavedStateHandle savedStateHandle) {
//        super(savedStateHandle);
//    }
//
//    private final Runnable updateRunnable = new Runnable() {
//        @Override
//        public void run() {
//            boolean isKg = "KG".equals(selectedWeightType.getValue());
//            float targetWeight = isKg ? 80F : 180F; // 80 KG ≈ 180 LBS
//            float currentWeight = weightData.getValue() != null ? weightData.getValue() : 0F;
//
//            if (Math.abs(currentWeight - targetWeight) > 0.5F) {
//                float step = 0.5F;
//                weightData.setValue(currentWeight + (targetWeight > currentWeight ? step : -step));
//                handler.postDelayed(this, 100);
//            } else {
//                weightData.setValue(targetWeight);
//            }
//        }
//    };
//
//    public void startUpdatingWeight() {
//        stopUpdatingWeight();
//        handler.post(updateRunnable);
//    }
//
//    public void stopUpdatingWeight() {
//        if (handler != null && updateRunnable != null) {
//            handler.removeCallbacks(updateRunnable);
//        }
//    }
//
//    public void resetWeight() {
//        stopUpdatingWeight();
//        weightData.setValue(0F);
//    }
//
//    public String getCurrentWeightCategory() {
//        boolean isKg = "KG".equals(selectedWeightType.getValue());
//        float weight = weightData.getValue() != null ? weightData.getValue() : 0F;
//
//        float normalMax = isKg ? 90F : 200F;
//        float mediumMax = isKg ? 150F : 330F;
//        float highMax = isKg ? 250F : 550F;
//
//        if (weight >= highMax) return "Highest Point (Extreme Obesity)";
//        if (weight >= mediumMax) return "High";
//        if (weight >= normalMax) return "Medium";
//        return "Normal";
//    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        disposables.clear();
//    }
//}
