package com.fmv.healthkiosk.feature.auth.domain.usecase;

import io.reactivex.Single;

public interface LoginUseCase {
    Single<String> execute(String phoneNumber);
}

