package com.fmv.healthkiosk.ui.home.test.widgets.weight;

import com.peng.ppscale.device.PeripheralIce.PPBlutoothPeripheralIceController;

public class PPBlutoothPeripheralIceInstance {
    private final PPBlutoothPeripheralIceController controller;

    private PPBlutoothPeripheralIceInstance() {
        this.controller = new PPBlutoothPeripheralIceController();
    }

    public static PPBlutoothPeripheralIceInstance getInstance() {
        return Factory.instance;
    }

    public PPBlutoothPeripheralIceController getController() {
        return this.controller;
    }

    private static class Factory {
        private static final PPBlutoothPeripheralIceInstance instance = new PPBlutoothPeripheralIceInstance();

        private Factory() {
        }
    }
}
