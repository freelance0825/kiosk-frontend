package com.fmv.healthkiosk.feature.tests.domain.repo;

import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryRequest;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryResponse;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.List;

import io.reactivex.Single;

public interface TestsRepository {
    Single<List<MedicalPackage>> getMedicalPackages();
    List<TestItem> getTestItems(String testsPreset);
    List<TestsResultModel> mapToTestResults(List<TestItem> testItems);
    Single<TestHistoryModel> postTestHistory(TestHistoryModel testHistoryModel);
    Single<List<TestHistoryModel>> getUserTestHistories(int userId);
}