package com.fmv.healthkiosk.feature.tests.domain.interactor;

import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.GetUserTestHistoryUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetUserTestHistoryInteractor implements GetUserTestHistoryUseCase {
    private final TestsRepository testsRepository;

    @Inject
    public GetUserTestHistoryInteractor(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }

    @Override
    public Single<List<TestHistoryModel>> getUserTestHistory(int userId) {
        return testsRepository.getUserTestHistories(userId);
    }
}
