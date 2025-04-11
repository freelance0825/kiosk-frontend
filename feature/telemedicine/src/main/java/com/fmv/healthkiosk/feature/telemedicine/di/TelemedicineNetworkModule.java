package com.fmv.healthkiosk.feature.telemedicine.di;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.TelemedicineService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class TelemedicineNetworkModule {

    @Provides
    @Singleton
    public static TelemedicineService provideTelemedicineService(Retrofit retrofit) {
        return retrofit.create(TelemedicineService.class);
    }
}
