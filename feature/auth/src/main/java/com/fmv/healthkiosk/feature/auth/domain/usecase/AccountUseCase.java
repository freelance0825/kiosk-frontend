package com.fmv.healthkiosk.feature.auth.domain.usecase;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AccountUseCase {
    Single<String> getUsername();
    Completable setUsername(String username);
    Single<Boolean> isLoggedIn();
    Completable logout();
    Completable clearUserData();
}
