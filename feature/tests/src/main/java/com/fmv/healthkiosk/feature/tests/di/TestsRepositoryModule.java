package com.fmv.healthkiosk.feature.tests.di;

import android.content.Context;

import com.fmv.healthkiosk.feature.tests.data.repo.TestsRepositoryImpl;
import com.fmv.healthkiosk.feature.tests.data.source.remote.TestsService;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class TestsRepositoryModule {

    @Provides
    @Singleton
    TestsRepository provideAuthRepository(@ApplicationContext Context context, TestsService testsService) {
        return new TestsRepositoryImpl(context, testsService);
    }
}
