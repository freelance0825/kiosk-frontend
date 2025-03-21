package com.fmv.healthkiosk.ui.home.test.widgets.weight;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.lefu.ppbase.PPBodyBaseModel;
import com.lefu.ppbase.PPDeviceModel;
import com.lefu.ppbase.util.PPUtil;
import com.lefu.ppbase.vo.PPBodyFatInScaleVo;
import com.lefu.ppbase.vo.PPScaleState;
import com.peng.ppscale.PPBluetoothKit;
import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPDataChangeListener;
import com.peng.ppscale.business.state.PPBleWorkState;
import com.peng.ppscale.device.PeripheralIce.PPBlutoothPeripheralIceController;
import com.peng.ppscale.search.PPSearchManager;

import java.lang.reflect.Field;

import javax.inject.Inject;

public class LeFuModule {
    private static final String TAG = "LeFuModule";
    private Context context;
    private LeFuListener listener;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface LeFuListener {
        void onDeviceFound(PPDeviceModel device);
        void onBluetoothStateChanged(PPBleWorkState state);
        void onWeightDataReceived(String weight);
        void onLockDataReceived(String weight);
        void onError(String message);
    }

    @Inject
    public LeFuModule(Context context, LeFuListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void scanDevice() {
        Log.e(TAG, "== Simulated scanDevice started ==");
        handler.postDelayed(() -> {
            PPDeviceModel mockDevice = new PPDeviceModel("aa", "aa");
            mockDevice.setDeviceName("CF597_GNLine");
            listener.onDeviceFound(mockDevice);
            Log.e(TAG, "== Simulated device found: " + mockDevice.getDeviceName());
        }, 1000);
    }

    public void stopSearch() {
        Log.e(TAG, "== Simulated stopSearch()");
    }

    public void startMeasuring() {
        Log.e(TAG, "== Simulated startMeasuring() ==");
        simulateWeightUpdates();
    }

    public void controllerDisconnect() {
        handler.removeCallbacksAndMessages(null);
        Log.e(TAG, "== Simulated controllerDisconnect()");
    }

    public void initSDK(String appKey, String secretKey, String configPath) {
        Log.e(TAG, "== Simulated initSDK() with appKey: " + appKey);
        listener.onError("SUCCESS to open Bluetooth.");
    }

    private void simulateWeightUpdates() {
        handler.postDelayed(new Runnable() {
            float currentWeight = 0F;
            final float targetWeight = 80F;
            final float step = 0.5F;

            @Override
            public void run() {
                if (Math.abs(currentWeight - targetWeight) > step) {
                    currentWeight += step;
                    listener.onWeightDataReceived(String.valueOf(currentWeight));
                    handler.postDelayed(this, 100);
                } else {
                    listener.onLockDataReceived(String.valueOf(currentWeight));
                }
            }
        }, 1000);
    }
}

// Original
//public class LeFuModule {
//    private static final String TAG = "LeFuModule";
//    private PPSearchManager ppScale;
//    private PPDeviceModel deviceModel;
//    private PPBlutoothPeripheralIceController controller;
//    private Context context;
//    private LeFuListener listener;
//
//    public interface LeFuListener {
//        void onDeviceFound(PPDeviceModel device);
//
//        void onBluetoothStateChanged(PPBleWorkState state);
//
//        void onWeightDataReceived(String weight);
//
//        void onLockDataReceived(String weight);
//
//        void onError(String message);
//    }
//
//    @Inject
//    public LeFuModule(Context context, LeFuListener listener) {
//        this.context = context;
//        this.listener = listener;
//
//        ppScale = new PPSearchManager();
//    }
//
//    public void scanDevice() {
//        Log.e(TAG, "== BluetoothClient null: " + (PPBluetoothKit.INSTANCE.getBluetoothClient() == null));
//        Log.e(TAG, "== scanDevice ppScale null: " + (ppScale == null));
//        Log.e(TAG, "== scanDevice ppScale isSearching: " + (ppScale != null && ppScale.isSearching()));
//        Log.e(TAG, "== scanDevice listener null: " + (listener == null));
//
//        if (ppScale == null) {
//            Log.e(TAG, "ppScale is null, reinitializing...");
//            ppScale = new PPSearchManager();
//        }
//
//        if (listener == null) {
//            Log.e(TAG, "Listener is null, aborting scan.");
//            return;
//        }
//
//        // Daftarkan listener sebelum mulai scan
//        ppScale.registerBluetoothStateListener(new PPBleStateInterface() {
//            @Override
//            public void monitorBluetoothWorkState(PPBleWorkState ppBleWorkState, PPDeviceModel deviceModel) {
//                listener.onBluetoothStateChanged(ppBleWorkState);
//            }
//        });
//
//        ppScale.startSearchDeviceList(300000, (ppDeviceModel, s) -> {
//            if ("CF597_GNLine".equals(ppDeviceModel.getDeviceName())) {
//                deviceModel = ppDeviceModel;
//                listener.onDeviceFound(ppDeviceModel);
//            }
//        }, new PPBleStateInterface() {
//            @Override
//            public void monitorBluetoothWorkState(PPBleWorkState ppBleWorkState, PPDeviceModel deviceModel) {
//                listener.onBluetoothStateChanged(ppBleWorkState);
//            }
//        });
//    }
//
//
//    public void stopSearch() {
//        ppScale.stopSearch();
//    }
//
//    public void startMeasuring() {
//        if (controller == null) {
//            controller = PPBlutoothPeripheralIceInstance.getInstance().getController();
//        }
//
//        controller.registDataChangeListener(new PPDataChangeListener() {
//            @Override
//            public void monitorProcessData(PPBodyBaseModel bodyBaseModel, PPDeviceModel deviceModel) {
//                String weightStr = PPUtil.getWeightValueD(
//                        bodyBaseModel.unit, (double) bodyBaseModel.getPpWeightKg(),
//                        deviceModel.deviceAccuracyType.getType()
//                );
//                listener.onWeightDataReceived(weightStr);
//            }
//
//            @Override
//            public void monitorLockData(PPBodyBaseModel bodyBaseModel, PPDeviceModel deviceModel) {
//                String weightStr = PPUtil.getWeightValueD(
//                        bodyBaseModel.unit, (double) bodyBaseModel.getPpWeightKg(),
//                        deviceModel.deviceAccuracyType.getType()
//                );
//                listener.onLockDataReceived(weightStr);
//            }
//
//            @Override
//            public void monitorScaleState(PPScaleState ppScaleState) {
//            }
//
//            @Override
//            public void monitorDataFail(PPBodyBaseModel ppBodyBaseModel, PPDeviceModel ppDeviceModel) {
//                listener.onError("Data measurement failed");
//            }
//
//            @Override
//            public void onImpedanceFatting() {
//            }
//
//            @Override
//            public void monitorLockDataByCalculateInScale(PPBodyFatInScaleVo ppBodyFatInScaleVo) {
//            }
//
//            @Override
//            public void monitorOverWeight() {
//            }
//
//            @Override
//            public void onDeviceShutdown() {
//            }
//        });
//
//        controller.startConnect(deviceModel, new PPBleStateInterface() {
//            @Override
//            public void monitorBluetoothWorkState(PPBleWorkState ppBleWorkState, PPDeviceModel deviceModel) {
//                listener.onBluetoothStateChanged(ppBleWorkState);
//            }
//        });
//    }
//
//    public void controllerDisconnect() {
//        controller.disConnect();
//    }
//
//    private void onBluetoothStateChanged(PPBleWorkState ppBleWorkState, PPDeviceModel deviceModel) {
//        listener.onBluetoothStateChanged(ppBleWorkState);
//    }
//
//    public void initSDK(String appKey, String secretKey, String configPath) {
//        PPBluetoothKit.INSTANCE.setDebug(true);
//        String dummyConfig = "[{\"deviceName\": \"CF597_GNLine\", \"deviceType\": 12}]";
//
//        PPBluetoothKit.INSTANCE.initSdk(context, appKey, secretKey, dummyConfig);
//
//        boolean bluetoothOpened = PPBluetoothKit.INSTANCE.openBluetooth();
//        if (!bluetoothOpened) {
//            listener.onError("Failed to open Bluetooth.");
//        } else {
//            listener.onError("SUCCESS to open Bluetooth.");
//        }
//    }
//}
