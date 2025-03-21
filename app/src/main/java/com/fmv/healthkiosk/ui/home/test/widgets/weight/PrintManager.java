package com.fmv.healthkiosk.ui.home.test.widgets.weight;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.printsdk.PrintSerializable;

import java.util.HashMap;

public class PrintManager {
    private static final String TAG = "PrintManager";
    private PrintSerializable mPrinter;
    private Context context;
    private PrintListener listener;

    public interface PrintListener {
        void onPrintSuccess(String message);
        void onPrintError(String error);
    }

    public PrintManager(Context context, PrintListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void printText(String text) {
        if (mPrinter != null) {
            mPrinter.printText(text);
            listener.onPrintSuccess("Text printed successfully.");
        } else {
            listener.onPrintError("Printer is not initialized.");
        }
    }

    public void wrapLines(int line) {
        if (mPrinter != null) {
            mPrinter.wrapLines(line);
        } else {
            listener.onPrintError("Printer is not initialized.");
        }
    }

    public void setAlign(int value) {
        if (mPrinter != null) {
            mPrinter.setAlign(value);
        } else {
            listener.onPrintError("Printer is not initialized.");
        }
    }

    public void printSelf() {
        if (mPrinter != null) {
            mPrinter.printSelf();
            mPrinter.printText("\n\n\n\n");
        } else {
            listener.onPrintError("Printer is not initialized.");
        }
    }

    public void initDevice() {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if (manager == null) {
            listener.onPrintError("USB Manager not available.");
            return;
        }

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            if (device.getProductId() == 649 && device.getVendorId() == 10473) {
                mPrinter = new PrintSerializable();
                mPrinter.open(manager, device);
                mPrinter.init();
                listener.onPrintSuccess("Printer initialized successfully.");
                return;
            }
        }
        listener.onPrintError("No printer found.");
    }
}
