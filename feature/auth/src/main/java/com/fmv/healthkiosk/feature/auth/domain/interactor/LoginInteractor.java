package com.fmv.healthkiosk.feature.auth.domain.interactor;

import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.LoginUseCase;

import io.reactivex.Single;

public class LoginInteractor implements LoginUseCase {
    private final AuthRepository authRepository;

    public LoginInteractor(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Single<String> execute(String phoneNumber) {
        return authRepository.login(phoneNumber);
    }
}
