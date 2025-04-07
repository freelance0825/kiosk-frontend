package com.fmv.healthkiosk.feature.auth.di;

import com.fmv.healthkiosk.feature.auth.domain.interactor.AccountInteractor;
import com.fmv.healthkiosk.feature.auth.domain.interactor.EditProfileInteractor;
import com.fmv.healthkiosk.feature.auth.domain.interactor.LoginInteractor;
import com.fmv.healthkiosk.feature.auth.domain.interactor.RegisterInteractor;
import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.auth.domain.usecase.EditProfileUseCase;
import com.fmv.healthkiosk.feature.auth.domain.usecase.LoginUseCase;
import com.fmv.healthkiosk.feature.auth.domain.usecase.RegisterUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class AuthUseCaseModule {

    @Provides
    LoginUseCase provideLoginUseCase(AuthRepository authRepository) {
        return new LoginInteractor(authRepository);
    }

    @Provides
    RegisterUseCase provideRegisterUseCase(AuthRepository authRepository) {
        return new RegisterInteractor(authRepository);
    }

    @Provides
    AccountUseCase provideAccountUseCase(AuthRepository authRepository) {
        return new AccountInteractor(authRepository);
    }

    @Provides
    EditProfileUseCase provideUpdateUseCase(AuthRepository authRepository) {
        return new EditProfileInteractor(authRepository);
    }
}
