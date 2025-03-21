package com.fmv.healthkiosk.feature.tests.domain.interactor;

import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class MedicalPackageInteractor implements MedicalPackageUseCase {
    private final TestsRepository testsRepository;

    @Inject
    public MedicalPackageInteractor(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }


    @Override
    public Single<List<MedicalPackage>> getMedicalPackages() {
        return testsRepository.getMedicalPackages();
    }
}
