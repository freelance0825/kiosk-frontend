package com.fmv.healthkiosk.feature.tests.domain.interactor;

import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryRequest;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryResponse;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.PostTestHistoryUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class PostTestHistoryInteractor implements PostTestHistoryUseCase {
    private final TestsRepository testsRepository;

    @Inject
    public PostTestHistoryInteractor(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }


    @Override
    public Single<TestHistoryModel> postTestHistory(TestHistoryModel testHistoryModel) {
        return testsRepository.postTestHistory(testHistoryModel);
    }
}
