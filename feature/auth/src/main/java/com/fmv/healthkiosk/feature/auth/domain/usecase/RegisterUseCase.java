package com.fmv.healthkiosk.feature.auth.domain.usecase;

import io.reactivex.Single;

public interface RegisterUseCase {
    Single<String> execute(String name, String gender, String age, String dob, String phoneNumber, String email);
}
