package com.fmv.healthkiosk.ui.home.test.widgets.bluetooth;

import android.os.Handler;
import android.os.Looper;

import java.util.Random;


public class OximeterManager {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private OximeterListener listener;
    private boolean isRunning = false;

    public interface OximeterListener {
        void onDeviceConnected(String message);
        void onDataReceived(String hexData);
        void onError(String error);
    }

    public OximeterManager(OximeterListener listener) {
        this.listener = listener;
    }

    public void connectDevice() {
        isRunning = true;
        listener.onDeviceConnected("Simulated device connected.");
        startSimulatingData();
    }

    public void disconnectDevice() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        listener.onDeviceConnected("Simulated device disconnected.");
    }

    private void startSimulatingData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                int oxygen = 95 + random.nextInt(6);  // Simulated SpO2: 95-100%
                int pulse = 60 + random.nextInt(21);  // Simulated Pulse: 60-80 BPM
                String hexData = String.format("%02X,%02X", oxygen, pulse);

                listener.onDataReceived(hexData);

                handler.postDelayed(this, 1000);  // Send new data every second
            }
        }, 1000);
    }
}


//public class OximeterManager {
//    private static final String TAG = "OximeterManager";
//    private UsbSerialManager usbSerialManager;
//    private OximeterListener listener;
//
//    public interface OximeterListener {
//        void onDeviceConnected(String message);
//        void onDataReceived(String hexData);
//        void onError(String error);
//    }
//
//    public OximeterManager(Context context, OximeterListener listener) {
//        this.listener = listener;
//        usbSerialManager = new UsbSerialManager(context, new UsbSerialManager.OnDataReceivedListener() {
//            @Override
//            public void onDataReceived(String hexData) {
//                listener.onDataReceived(hexData);
//            }
//
//            @Override
//            public void onError(String error) {
//                listener.onError(error);
//            }
//        });
//    }
//
//    public void connectDevice(int productId, int vendorId) {
//        boolean success = usbSerialManager.openDevice(productId, vendorId);
//        if (success) {
//            listener.onDeviceConnected("Device initialized successfully.");
//        } else {
//            listener.onError("Failed to open device.");
//        }
//    }
//
//    public void disconnectDevice() {
//        usbSerialManager.closeDevice();
//        listener.onDeviceConnected("Device closed.");
//    }
//}
