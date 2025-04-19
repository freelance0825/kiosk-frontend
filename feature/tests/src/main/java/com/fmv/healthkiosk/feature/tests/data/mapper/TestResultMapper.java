package com.fmv.healthkiosk.feature.tests.data.mapper;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class TestResultMapper {

    public List<TestsResultModel> mapToTestsResults(List<TestItem> testItems) {
        List<TestsResultModel> testResults = new ArrayList<>();

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
                        TestsResultModel testResult = new TestsResultModel();
                        testResult.setName(item.getName());
                        testResult.setResult(value + " " + extension);
                        testResult.setRange(status);
                        testResult.setId(statusFlag);
                        testResult.setIsGeneralTest(item.isGeneral());
                        testResults.add(testResult);
                    }
                    break;
                case "oximeter":
                    if (values.length == 2) {
                        float spo2 = Float.parseFloat(values[0]);
                        float prbpm = Float.parseFloat(values[1]);

                        String spo2Status = getStatusForSpo2(spo2);
                        String prbpmStatus = getStatusForPrbpm(prbpm);

                        TestsResultModel spo2Result = new TestsResultModel();
                        spo2Result.setName("SpO2");
                        spo2Result.setResult(spo2 + " %");
                        spo2Result.setRange(spo2Status);
                        spo2Result.setId(getStatusFlag(spo2Status));
                        spo2Result.setIsGeneralTest(item.isGeneral());

                        TestsResultModel prbpmResult = new TestsResultModel();
                        prbpmResult.setName("PRbpm");
                        prbpmResult.setResult(prbpm + " bpm");
                        prbpmResult.setRange(prbpmStatus);
                        prbpmResult.setId(getStatusFlag(prbpmStatus));
                        prbpmResult.setIsGeneralTest(item.isGeneral());

                        testResults.add(spo2Result);
                        testResults.add(prbpmResult);
                    }
                    break;
                case "blood_pressure":
                    if (values.length == 3) {
                        float pulse = Float.parseFloat(values[0]);
                        float systolic = Float.parseFloat(values[1]);
                        float diastolic = Float.parseFloat(values[2]);

                        String pulseStatus = getStatusForPulse(pulse);
                        String bpStatus = getStatusForBloodPressure(systolic, diastolic);

                        TestsResultModel pulseResult = new TestsResultModel();
                        pulseResult.setName("Pulse");
                        pulseResult.setResult(pulse + " pulse/min");
                        pulseResult.setRange(pulseStatus);
                        pulseResult.setId(getStatusFlag(pulseStatus));
                        pulseResult.setIsGeneralTest(item.isGeneral());

                        TestsResultModel systolicResult = new TestsResultModel();
                        systolicResult.setName("Systolic");
                        systolicResult.setResult(systolic + " mmHg");
                        systolicResult.setRange(bpStatus);
                        systolicResult.setId(getStatusFlag(bpStatus));
                        systolicResult.setIsGeneralTest(item.isGeneral());

                        TestsResultModel diastolicResult = new TestsResultModel();
                        diastolicResult.setName("Diastolic");
                        diastolicResult.setResult(diastolic + " mmHg");
                        diastolicResult.setRange(bpStatus);
                        diastolicResult.setId(getStatusFlag(bpStatus));
                        diastolicResult.setIsGeneralTest(item.isGeneral());

                        testResults.add(pulseResult);
                        testResults.add(systolicResult);
                        testResults.add(diastolicResult);
                    }
                    break;
                default:
                    if (values.length == 1) {
                        float value = Float.parseFloat(values[0]);
                        TestsResultModel testResult = new TestsResultModel();
                        testResult.setName(item.getName());
                        testResult.setResult(value + " " + extension);
                        testResult.setRange("Unknown Status");
                        testResult.setId(-1);
                        testResult.setIsGeneralTest(item.isGeneral());
                        testResults.add(testResult);
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


//public class TestResultMapper {
//
//    public List<TestResult> mapToTestResults(List<TestItem> testItems) {
//        List<TestResult> testResults = new ArrayList<>();
//
//        for (TestItem item : testItems) {
//            String[] values = item.getTestResult().split(",");
//            String extension = item.getTestExtension();
//
//            switch (item.getId()) {
//                case "height":
//                case "weight":
//                case "temperature":
//                    if (values.length == 1) {
//                        float value = Float.parseFloat(values[0]);
//                        String status = getStatusForSingleValue(item.getId(), value, extension);
//                        int statusFlag = getStatusFlag(status);
//                        testResults.add(new TestResult(item.getName(), value, extension, status, statusFlag, item.isGeneral()));
//                    }
//                    break;
//                case "oximeter":
//                    if (values.length == 2) {
//                        float spo2 = Float.parseFloat(values[0]);
//                        float prbpm = Float.parseFloat(values[1]);
//
//                        String spo2Status = getStatusForSpo2(spo2);
//                        String prbpmStatus = getStatusForPrbpm(prbpm);
//
//                        testResults.add(new TestResult("SpO2", spo2, "%", spo2Status, getStatusFlag(spo2Status), item.isGeneral()));
//                        testResults.add(new TestResult("PRbpm", prbpm, "bpm", prbpmStatus, getStatusFlag(prbpmStatus), item.isGeneral()));
//                    }
//                    break;
//                case "blood_pressure":
//                    if (values.length == 3) {
//                        float pulse = Float.parseFloat(values[0]);
//                        float systolic = Float.parseFloat(values[1]);
//                        float diastolic = Float.parseFloat(values[2]);
//
//                        String pulseStatus = getStatusForPulse(pulse);
//                        String bpStatus = getStatusForBloodPressure(systolic, diastolic);
//
//                        testResults.add(new TestResult("Pulse", pulse, "pulse/min", pulseStatus, getStatusFlag(pulseStatus), item.isGeneral()));
//                        testResults.add(new TestResult("Systolic", systolic, "mmHg", bpStatus, getStatusFlag(bpStatus), item.isGeneral()));
//                        testResults.add(new TestResult("Diastolic", diastolic, "mmHg", bpStatus, getStatusFlag(bpStatus), item.isGeneral()));
//                    }
//                    break;
//                default:
//                    if (values.length == 1) {
//                        float value = Float.parseFloat(values[0]);
//                        testResults.add(new TestResult(item.getName(), value, extension, "Unknown Status", -1, item.isGeneral()));
//                    }
//                    break;
//            }
//        }
//
//        return testResults;
//    }
//
//    private String getStatusForSingleValue(String type, float value, String extension) {
//        switch (type) {
//            case "height":
//                return getStatusForHeight(value);
//            case "weight":
//                return getStatusForWeight(value, extension);
//            case "temperature":
//                return getStatusForTemperature(value, extension);
//            default:
//                return "Unknown";
//        }
//    }
//
//    private String getStatusForHeight(float heightCm) {
//        if (heightCm < 150) return "Low";
//        if (heightCm < 180) return "Normal";
//        return "High";
//    }
//
//    private String getStatusForWeight(float weight, String extension) {
//        if (extension.equalsIgnoreCase("lbs")) {
//            // Konversi lbs ke kg (1 lbs = 0.453592 kg)
//            weight = weight * 0.453592f;
//        }
//
//        if (weight < 50) return "Low";
//        if (weight < 80) return "Normal";
//        return "High";
//    }
//
//    private String getStatusForTemperature(float temp, String extension) {
//        if (extension.equalsIgnoreCase("°F")) {
//            // Konversi °F ke °C (°C = (°F - 32) * 5/9)
//            temp = (temp - 32) * 5 / 9;
//        }
//
//        if (temp < 36) return "Low";
//        if (temp <= 37.5) return "Normal";
//        return "High";
//    }
//
//    private String getStatusForSpo2(float spo2) {
//        return spo2 < 90 ? "Low" : spo2 <= 100 ? "Normal" : "High";
//    }
//
//    private String getStatusForPrbpm(float prbpm) {
//        return prbpm < 60 ? "Low" : prbpm <= 100 ? "Normal" : "High";
//    }
//
//    private String getStatusForPulse(float pulse) {
//        return pulse < 60 ? "Low" : pulse <= 100 ? "Normal" : "High";
//    }
//
//    private String getStatusForBloodPressure(float systolic, float diastolic) {
//        if (systolic < 90 || diastolic < 60) return "Low";
//        if (systolic <= 120 && diastolic <= 80) return "Normal";
//        return "High";
//    }
//
//    private int getStatusFlag(String status) {
//        switch (status) {
//            case "Low":
//                return 0;
//            case "Normal":
//                return 1;
//            case "High":
//                return 2;
//            default:
//                return -1; // Untuk status yang tidak diketahui
//        }
//    }
//}
