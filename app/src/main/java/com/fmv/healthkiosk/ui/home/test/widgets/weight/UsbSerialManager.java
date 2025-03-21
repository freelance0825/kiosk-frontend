package com.fmv.healthkiosk.ui.home.test.widgets.weight;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsbSerialManager {
    private static final String TAG = "UsbSerialManager";
    private UsbSerialPort port = null;
    private SerialInputOutputManager usbIoManager;
    private UsbManager usbManager;
    private Context context;
    private OnDataReceivedListener listener;

    public UsbSerialManager(Context context, OnDataReceivedListener listener) {
        this.context = context;
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.listener = listener;
    }

    public interface OnDataReceivedListener {
        void onDataReceived(String hexData);
        void onError(String error);
    }

    public boolean openDevice(int productId, int vendorId) {
        UsbSerialProber prober = UsbSerialProber.getDefaultProber();
        List<UsbSerialDriver> availableDrivers = prober.findAllDrivers(usbManager);

        if (availableDrivers.isEmpty()) {
            listener.onError("No USB devices found.");
            return false;
        }

        UsbSerialDriver driver = null;
        for (UsbSerialDriver usbSerialDriver : availableDrivers) {
            UsbDevice device = usbSerialDriver.getDevice();
            if (device.getProductId() == productId && device.getVendorId() == vendorId) {
                driver = usbSerialDriver;
                break;
            }
        }

        if (driver == null) {
            listener.onError("Matching USB device not found.");
            return false;
        }

        UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
        if (connection == null) {
            listener.onError("USB permission denied.");
            return false;
        }

        port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            usbIoManager = new SerialInputOutputManager(port, new SerialInputOutputManager.Listener() {
                @Override
                public void onNewData(byte[] data) {
                    listener.onDataReceived(bytesToHex(data));
                }

                @Override
                public void onRunError(Exception e) {
                    listener.onError("USB IO error: " + e.getMessage());
                }
            });
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> usbIoManager.start());


            return true;
        } catch (Exception e) {
            listener.onError("Error opening USB: " + e.getMessage());
            return false;
        }
    }

    public void closeDevice() {
        if (port != null) {
            try {
                port.close();
                port = null;
            } catch (IOException e) {
                listener.onError("Error closing device: " + e.getMessage());
            }
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}
