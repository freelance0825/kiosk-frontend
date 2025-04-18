package com.fmv.healthkiosk.feature.telemedicine.di;

import com.fmv.healthkiosk.feature.telemedicine.data.repo.TelemedicineRepositoryImpl;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.TelemedicineService;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class TelemedicineRepositoryModule {

    @Provides
    @Singleton
    TelemedicineRepository provideTelemedicineRepository(TelemedicineService telemedicineService) {
        return new TelemedicineRepositoryImpl(telemedicineService);
    }
}
