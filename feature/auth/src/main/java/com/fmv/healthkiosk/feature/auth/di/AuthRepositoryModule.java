package com.fmv.healthkiosk.feature.auth.di;

import android.content.Context;

import com.fmv.healthkiosk.feature.auth.data.repo.AuthRepositoryImpl;
import com.fmv.healthkiosk.feature.auth.data.source.local.AuthDataStore;
import com.fmv.healthkiosk.feature.auth.data.source.remote.AuthService;
import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AuthRepositoryModule {

    @Provides
    @Singleton
    AuthRepository provideAuthRepository(@ApplicationContext Context context, AuthService authService, AuthDataStore authDataStore) {
        return new AuthRepositoryImpl(context, authService, authDataStore);
    }
}
