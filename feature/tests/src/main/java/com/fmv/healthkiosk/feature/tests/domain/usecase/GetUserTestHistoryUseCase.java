package com.fmv.healthkiosk.feature.tests.domain.usecase;

import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;

import java.util.List;

import io.reactivex.Single;

public interface GetUserTestHistoryUseCase {
    Single<List<TestHistoryModel>> getUserTestHistory(int userId);
}
