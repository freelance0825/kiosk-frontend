package com.fmv.healthkiosk.feature.auth.di;

import com.fmv.healthkiosk.feature.auth.data.repo.AuthRepositoryImpl;
import com.fmv.healthkiosk.feature.auth.data.source.local.AuthDataStore;
import com.fmv.healthkiosk.feature.auth.data.source.remote.AuthService;
import com.fmv.healthkiosk.feature.auth.domain.interactor.AccountInteractor;
import com.fmv.healthkiosk.feature.auth.domain.interactor.LoginInteractor;
import com.fmv.healthkiosk.feature.auth.domain.interactor.RegisterInteractor;
import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.auth.domain.usecase.LoginUseCase;
import com.fmv.healthkiosk.feature.auth.domain.usecase.RegisterUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AuthRepositoryModule {

    @Provides
    @Singleton
    AuthRepository provideAuthRepository(AuthService authService, AuthDataStore authDataStore) {
        return new AuthRepositoryImpl(authService, authDataStore);
    }
}
