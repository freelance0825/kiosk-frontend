package com.fmv.healthkiosk.feature.auth.data.repo;

import com.fmv.healthkiosk.feature.auth.data.source.local.AuthDataStore;
import com.fmv.healthkiosk.feature.auth.data.source.remote.AuthService;
import com.fmv.healthkiosk.feature.auth.data.source.remote.model.LoginRequest;
import com.fmv.healthkiosk.feature.auth.data.source.remote.model.RegisterRequest;
import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public class AuthRepositoryImpl implements AuthRepository {

    private final AuthService authService;
    private final AuthDataStore authDataStore;

    public AuthRepositoryImpl(AuthService authService, AuthDataStore authDataStore) {
        this.authService = authService;
        this.authDataStore = authDataStore;
    }

    @Override
    public Single<String> login(String phoneNumber) {
        return authService.loginPhoneNumber(new LoginRequest(phoneNumber, "", ""))
                .flatMap(authResponse -> Completable.concatArray(
                        authDataStore.setPhoneNumber(phoneNumber),
                        authDataStore.setUsername(authResponse.getName()),
                        authDataStore.setDateOfBirth(authResponse.getDateOfBirth()),
                        authDataStore.setGender(authResponse.getGender()),
                        authDataStore.setAge(Integer.parseInt(authResponse.getAge()))
                ).toSingleDefault("Login successful!"));
    }

    @Override
    public Single<String> registerUser(String name, String gender, String age, String dob, String phoneNumber) {
        RegisterRequest request = new RegisterRequest("image");
        return authService.register(name, "address", gender, age, dob, phoneNumber, request)
                .flatMap(authResponse -> Completable.concatArray(
                        authDataStore.setPhoneNumber(phoneNumber),
                        authDataStore.setUsername(authResponse.getName()),
                        authDataStore.setDateOfBirth(authResponse.getDateOfBirth()),
                        authDataStore.setGender(authResponse.getGender()),
                        authDataStore.setAge(Integer.parseInt(authResponse.getAge()))
                ).toSingleDefault("Register successful!"));
    }

    @Override
    public Observable<String> getUsername() {
        return authDataStore.getUsername();
    }

    @Override
    public Observable<String> getDateOfBirth() {
        return authDataStore.getDateOfBirth();
    }

    @Override
    public Observable<String> getGender() {
        return authDataStore.getGender();
    }

    @Override
    public Observable<String> getPhoneNumber() {
        return authDataStore.getPhoneNumber();
    }

    @Override
    public Observable<Integer> getAge() {
        return authDataStore.getAge();
    }

    @Override
    public Observable<Boolean> isLoggedIn() {
        return authDataStore.isLoggedIn();
    }

    @Override
    public Completable setIsLoggedIn(boolean isLoggedIn) {
        return authDataStore.setLoggedIn(isLoggedIn);
    }

    @Override
    public Completable logout() {
        return authDataStore.clearData();
    }
}
