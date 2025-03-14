package com.fmv.healthkiosk.feature.auth.domain.usecase;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface AccountUseCase {
    Observable<Boolean> isLoggedIn();

    Observable<String> getUsername();

    Observable<String> getDateOfBirth();

    Observable<String> getGender();

    Observable<String> getPhoneNumber();

    Observable<Integer> getAge();

    Completable logout();
    Completable setIsLoggedIn(boolean isLoggedIn);
}
