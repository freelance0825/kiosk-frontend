package com.fmv.healthkiosk.feature.tests.domain.usecase;

import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryRequest;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryResponse;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;

import java.util.List;

import io.reactivex.Single;

public interface PostTestHistoryUseCase {
    Single<TestHistoryModel> postTestHistory(TestHistoryModel testHistoryModel);
}
