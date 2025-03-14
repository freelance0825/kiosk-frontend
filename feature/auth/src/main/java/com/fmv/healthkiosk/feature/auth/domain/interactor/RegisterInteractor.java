package com.fmv.healthkiosk.feature.auth.domain.interactor;

import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.RegisterUseCase;

import io.reactivex.Single;

public class RegisterInteractor implements RegisterUseCase {
    private final AuthRepository authRepository;

    public RegisterInteractor(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Single<String> execute(String name, String address, String gender, String age, String dob, String phoneNumber) {
        return authRepository.registerUser(name, address, gender, age, dob, phoneNumber);
    }
}
