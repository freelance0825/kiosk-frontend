package com.fmv.healthkiosk.feature.auth.domain.usecase;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface EditProfileUseCase {

    Single<String> execute(Integer userId,String name, String gender, String phoneNumber, String email, String dob);

    Observable<String> getUsername();

    Observable<String> getGender();

    Observable<String> getPhoneNumber();

    Observable<String> getEmail();

    Observable<String> getDateOfBirth();

    Observable<Integer> getUserId();
}
