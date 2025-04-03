package com.fmv.healthkiosk.feature.tests.di;

import android.content.Context;

import com.fmv.healthkiosk.feature.tests.domain.interactor.MedicalPackageInteractor;
import com.fmv.healthkiosk.feature.tests.domain.interactor.TestsPresetInteractor;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ViewModelComponent.class)
public class ContextUseCaseModule {

    @Provides
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }
}
