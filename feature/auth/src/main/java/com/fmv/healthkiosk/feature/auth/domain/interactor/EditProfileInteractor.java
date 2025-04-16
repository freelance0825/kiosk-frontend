package com.fmv.healthkiosk.feature.auth.domain.interactor;

import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;
import com.fmv.healthkiosk.feature.auth.domain.usecase.EditProfileUseCase;

import io.reactivex.Observable;
import io.reactivex.Single;

public class EditProfileInteractor implements EditProfileUseCase {

    private final AuthRepository authRepository;

    public EditProfileInteractor(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Single<String> execute(Integer userId,String name, String gender, String phoneNumber, String email, String dob, String age) {
        return authRepository.updateUser(userId, name, gender, phoneNumber, email, dob, age);
    }

    @Override
    public Observable<String> getUsername() {
        return authRepository.getUsername();
    }

    @Override
    public Observable<String> getGender() {return authRepository.getGender();
    }

    @Override
    public Observable<String> getPhoneNumber() {
        return authRepository.getPhoneNumber();
    }

    @Override
    public Observable<String> getEmail() {return authRepository.getEmail();
    }

    @Override
    public Observable<String> getDateOfBirth() {return authRepository.getDateOfBirth();}

    @Override
    public Observable<Integer> getUserId() { return authRepository.getUserId(); }

}
