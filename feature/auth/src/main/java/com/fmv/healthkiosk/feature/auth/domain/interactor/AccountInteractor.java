package com.fmv.healthkiosk.feature.auth.domain.interactor;

import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class AccountInteractor implements AccountUseCase {
    private final AuthRepository authRepository;

    public AccountInteractor(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }


    @Override
    public Observable<Boolean> isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    @Override
    public Observable<String> getUsername() {
        return authRepository.getUsername();
    }

    @Override
    public Observable<String> getDateOfBirth() {
        return authRepository.getDateOfBirth();
    }

    @Override
    public Observable<String> getGender() {
        return authRepository.getGender();
    }

    @Override
    public Observable<String> getPhoneNumber() {
        return authRepository.getPhoneNumber();
    }

    @Override
    public Observable<Integer> getAge() {
        return authRepository.getAge();
    }

    @Override
    public Completable logout() {
        return authRepository.logout();
    }

    @Override
    public Completable setIsLoggedIn(boolean isLoggedIn) {
        return authRepository.setIsLoggedIn(isLoggedIn);
    }
}
