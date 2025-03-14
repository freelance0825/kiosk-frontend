package com.fmv.healthkiosk.feature.auth.domain.interactor;

import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AccountInteractor implements AccountUseCase {
    private final AuthRepository authRepository;

    public AccountInteractor(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Single<String> getUsername() {
        return authRepository.getUsername();
    }

    @Override
    public Completable setUsername(String username) {
        return authRepository.setUsername(username);
    }

    @Override
    public Single<Boolean> isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    @Override
    public Completable logout() {
        return authRepository.logout();
    }

    @Override
    public Completable clearUserData() {
        return authRepository.clearUserData();
    }
}
