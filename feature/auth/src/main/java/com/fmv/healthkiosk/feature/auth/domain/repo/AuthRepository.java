package com.fmv.healthkiosk.feature.auth.domain.repo;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface AuthRepository {
    Single<String> login(String phoneNumber);

    Single<String> registerUser(String name, String gender, String age, String dob, String phoneNumber, String email);

    Single<String> updateUser(Integer userId, String name, String gender, String phoneNumber, String email, String dob);

    Completable logout();

    Completable setIsLoggedIn(boolean isLoggedIn);

    Observable<String> getUsername();

    Observable<String> getDateOfBirth();

    Observable<String> getGender();

    Observable<String> getPhoneNumber();

    Observable<String> getEmail();

    Observable<Integer> getAge();

    Observable<Integer> getUserId();

    Observable<Boolean> isLoggedIn();
}