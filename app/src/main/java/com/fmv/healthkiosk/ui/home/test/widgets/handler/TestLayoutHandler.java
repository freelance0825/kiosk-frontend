package com.fmv.healthkiosk.ui.home.test.widgets.handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.ui.home.test.widgets.bloodpressure.BloodPressureTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.bloodsugar.BloodSugarTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.cholesterol.CholesterolTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.ecg.EcgTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.height.HeightTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.oximeter.OximeterTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.stethoscope.StethoscopeTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.temperature.TemperatureTestFragment;
import com.fmv.healthkiosk.ui.home.test.widgets.weight.WeightTestFragment;

public class TestLayoutHandler {

    private final FragmentManager fragmentManager;
    private final int containerId;

    public TestLayoutHandler(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    public void updateTestLayout(TestItem testItem) {
        Fragment fragment;

        switch (testItem.getId()) {
            case "height":
                fragment = new HeightTestFragment();
                break;
            case "weight":
                fragment = new WeightTestFragment();
                break;
            case "temperature":
                fragment = new TemperatureTestFragment();
                break;
            case "oximeter":
                fragment = new OximeterTestFragment();
                break;
            case "blood_pressure":
                fragment = new BloodPressureTestFragment();
                break;
            case "stethoscope":
                fragment = new StethoscopeTestFragment();
                break;
            case "ecg":
                fragment = new EcgTestFragment();
                break;
            case "cholesterol":
                fragment = new CholesterolTestFragment();
                break;
            case "blood_sugar":
                fragment = new BloodSugarTestFragment();
                break;
            default:
                fragment = null;
        }

        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(containerId, fragment, testItem.getId())
                    .commit();
        }
    }
}
