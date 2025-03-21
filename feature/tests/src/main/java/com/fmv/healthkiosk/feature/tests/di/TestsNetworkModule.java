package com.fmv.healthkiosk.feature.tests.di;

import com.fmv.healthkiosk.feature.tests.data.source.remote.TestsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class TestsNetworkModule {

    @Provides
    @Singleton
    public static TestsService provideTestsService(Retrofit retrofit) {
        return retrofit.create(TestsService.class);
    }
}

