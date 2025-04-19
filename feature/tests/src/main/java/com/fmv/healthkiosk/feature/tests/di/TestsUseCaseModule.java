package com.fmv.healthkiosk.feature.tests.di;

import com.fmv.healthkiosk.feature.tests.domain.interactor.GetUserTestHistoryInteractor;
import com.fmv.healthkiosk.feature.tests.domain.interactor.MedicalPackageInteractor;
import com.fmv.healthkiosk.feature.tests.domain.interactor.PostTestHistoryInteractor;
import com.fmv.healthkiosk.feature.tests.domain.interactor.TestResultInteractor;
import com.fmv.healthkiosk.feature.tests.domain.interactor.TestsPresetInteractor;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.GetUserTestHistoryUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.PostTestHistoryUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestResultUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class TestsUseCaseModule {

    @Provides
    MedicalPackageUseCase provideMedicalPackageUseCase(TestsRepository testsRepository) {
        return new MedicalPackageInteractor(testsRepository);
    }

    @Provides
    TestsPresetUseCase provideTestsPresetUseCase(TestsRepository testsRepository) {
        return new TestsPresetInteractor(testsRepository);
    }

    @Provides
    TestResultUseCase provideTestResultUseCase(TestsRepository testsRepository) {
        return new TestResultInteractor(testsRepository);
    }

    @Provides
    PostTestHistoryUseCase providePostTestHistoryUseCase(TestsRepository testsRepository) {
        return new PostTestHistoryInteractor(testsRepository);
    }

    @Provides
    GetUserTestHistoryUseCase provideGetUserTestHistoryUseCase(TestsRepository testsRepository) {
        return new GetUserTestHistoryInteractor(testsRepository);
    }
}
