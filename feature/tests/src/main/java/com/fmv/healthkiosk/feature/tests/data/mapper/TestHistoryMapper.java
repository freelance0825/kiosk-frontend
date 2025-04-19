package com.fmv.healthkiosk.feature.tests.data.mapper;

import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryRequest;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryResponse;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestsResultResponse;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.ArrayList;
import java.util.List;

public class TestHistoryMapper {

    public static List<TestHistoryModel> mapToModelList(List<TestHistoryResponse> responseList) {
        if (responseList == null) return null;

        List<TestHistoryModel> modelList = new ArrayList<>();
        for (TestHistoryResponse response : responseList) {
            modelList.add(mapToModel(response));
        }

        return modelList;
    }

    public static TestHistoryModel mapToModel(TestHistoryResponse response) {
        if (response == null) return null;

        TestHistoryModel model = new TestHistoryModel();
        model.setUserAddress(response.getUserAddress());
        model.setCreatedAt(response.getCreatedAt());
        model.setTests(mapTestResults(response.getTests()));
        model.setUserDob(response.getUserDob());
        model.setUserPhoneNumber(response.getUserPhoneNumber());
        model.setUserGender(response.getUserGender());
        model.setOriginalPackageName(response.getPackageName());
        model.setUserName(response.getUserName());
        model.setUserId(response.getUserId());
        model.setUserAge(response.getUserAge());

        return model;
    }

    private static List<TestsResultModel> mapTestResults(List<TestsResultResponse> responses) {
        List<TestsResultModel> results = new ArrayList<>();
        if (responses == null) return results;

        for (TestsResultResponse response : responses) {
            TestsResultModel model = new TestsResultModel();
            model.setId(response.getId());
            model.setName(response.getName());
            model.setOriginalRange(response.getRange());
            model.setResult(response.getResult());
            results.add(model);
        }

        return results;
    }

    public static TestHistoryRequest mapToRequest(TestHistoryModel model) {
        if (model == null) return null;

        TestHistoryRequest request = new TestHistoryRequest();
        request.setUserId(model.getUserId());
        request.setPackageName(model.getOriginalPackageName());
        request.setTests(mapTestsResultModelToResponse(model.getTests()));

        return request;
    }

    private static List<TestsResultResponse> mapTestsResultModelToResponse(List<TestsResultModel> models) {
        if (models == null) return null;

        List<TestsResultResponse> responseList = new ArrayList<>();
        for (TestsResultModel model : models) {
            responseList.add(mapSingleTestResult(model));
        }
        return responseList;
    }

    private static TestsResultResponse mapSingleTestResult(TestsResultModel model) {
        if (model == null) return null;

        TestsResultResponse response = new TestsResultResponse();
        response.setName(model.getName());
        response.setName(model.getName());
        response.setResult(model.getResult());
        response.setRange(model.getOriginalRange());
        response.setIsGeneralTest(model.getIsGeneralTest());

        return response;
    }
}
