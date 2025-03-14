package com.fmv.healthkiosk.feature.auth.domain.repo;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AuthRepository {
    Single<String> login(String phoneNumber);
    Single<String> registerUser(String name, String address, String gender, String age, String dob, String phoneNumber);
    Completable logout();
    Single<String> getUsername();
    Completable setUsername(String username);
    Single<Boolean> isLoggedIn();
    Completable clearUserData();
}