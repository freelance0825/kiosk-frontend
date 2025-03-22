package com.fmv.healthkiosk.feature.tests.data.mapper;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class TestResultMapper {

    public List<TestResult> mapToTestResults(List<TestItem> testItems) {
        List<TestResult> testResults = new ArrayList<>();

        for (TestItem item : testItems) {
            String[] values = item.getTestResult().split(",");
            String extension = item.getTestExtension();

            switch (item.getId()) {
                case "height":
                case "weight":
                case "temperature":
                    if (values.length == 1) {
                        float value = Float.parseFloat(values[0]);
                        String status = getStatusForSingleValue(item.getId(), value, extension);
                        int statusFlag = getStatusFlag(status);
                        testResults.add(new TestResult(item.getName(), value, extension, status, statusFlag));
                    }
                    break;
                case "oximeter":
                    if (values.length == 2) {
                        float spo2 = Float.parseFloat(values[0]);
                        float prbpm = Float.parseFloat(values[1]);

                        String spo2Status = getStatusForSpo2(spo2);
                        String prbpmStatus = getStatusForPrbpm(prbpm);

                        testResults.add(new TestResult("SpO2", spo2, "%", spo2Status, getStatusFlag(spo2Status)));
                        testResults.add(new TestResult("PRbpm", prbpm, "bpm", prbpmStatus, getStatusFlag(prbpmStatus)));
                    }
                    break;
                case "blood_pressure":
                    if (values.length == 3) {
                        float pulse = Float.parseFloat(values[0]);
                        float systolic = Float.parseFloat(values[1]);
                        float diastolic = Float.parseFloat(values[2]);

                        String pulseStatus = getStatusForPulse(pulse);
                        String bpStatus = getStatusForBloodPressure(systolic, diastolic);

                        testResults.add(new TestResult("Pulse", pulse, "pulse/min", pulseStatus, getStatusFlag(pulseStatus)));
                        testResults.add(new TestResult("Systolic", systolic, "mmHg", bpStatus, getStatusFlag(bpStatus)));
                        testResults.add(new TestResult("Diastolic", diastolic, "mmHg", bpStatus, getStatusFlag(bpStatus)));
                    }
                    break;
                default:
                    if (values.length == 1) {
                        float value = Float.parseFloat(values[0]);
                        testResults.add(new TestResult(item.getName(), value, extension, "Unknown Status", -1));
                    }
                    break;
            }
        }

        return testResults;
    }

    private String getStatusForSingleValue(String type, float value, String extension) {
        switch (type) {
            case "height":
                return getStatusForHeight(value);
            case "weight":
                return getStatusForWeight(value, extension);
            case "temperature":
                return getStatusForTemperature(value, extension);
            default:
                return "Unknown";
        }
    }

    private String getStatusForHeight(float heightCm) {
        if (heightCm < 150) return "Low";
        if (heightCm < 180) return "Normal";
        return "High";
    }

    private String getStatusForWeight(float weight, String extension) {
        if (extension.equalsIgnoreCase("lbs")) {
            // Konversi lbs ke kg (1 lbs = 0.453592 kg)
            weight = weight * 0.453592f;
        }

        if (weight < 50) return "Low";
        if (weight < 80) return "Normal";
        return "High";
    }

    private String getStatusForTemperature(float temp, String extension) {
        if (extension.equalsIgnoreCase("°F")) {
            // Konversi °F ke °C (°C = (°F - 32) * 5/9)
            temp = (temp - 32) * 5 / 9;
        }

        if (temp < 36) return "Low";
        if (temp <= 37.5) return "Normal";
        return "High";
    }

    private String getStatusForSpo2(float spo2) {
        return spo2 < 90 ? "Low" : spo2 <= 100 ? "Normal" : "High";
    }

    private String getStatusForPrbpm(float prbpm) {
        return prbpm < 60 ? "Low" : prbpm <= 100 ? "Normal" : "High";
    }

    private String getStatusForPulse(float pulse) {
        return pulse < 60 ? "Low" : pulse <= 100 ? "Normal" : "High";
    }

    private String getStatusForBloodPressure(float systolic, float diastolic) {
        if (systolic < 90 || diastolic < 60) return "Low";
        if (systolic <= 120 && diastolic <= 80) return "Normal";
        return "High";
    }

    private int getStatusFlag(String status) {
        switch (status) {
            case "Low":
                return 0;
            case "Normal":
                return 1;
            case "High":
                return 2;
            default:
                return -1; // Untuk status yang tidak diketahui
        }
    }
}
