package com.fmv.healthkiosk.ui.auth.editprofile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.EditProfileUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class EditProfileViewModel extends BaseViewModel {
    private final EditProfileUseCase editProfileUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<String> updateSuccessMessage = new MutableLiveData<>();

    final MutableLiveData<Integer> userId = new MutableLiveData<>();

    final MutableLiveData<String> username = new MutableLiveData<>();
    final MutableLiveData<String> gender = new MutableLiveData<>();
    final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    final MutableLiveData<String> email = new MutableLiveData<>();
    final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public EditProfileViewModel(SavedStateHandle savedStateHandle, EditProfileUseCase editProfileUseCase) {
        super(savedStateHandle);

        this.editProfileUseCase = editProfileUseCase;
        observeProfileData();
    }

    private void observeProfileData() {
        disposables.add(editProfileUseCase.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userId::setValue, throwable -> userId.setValue(null)));


        disposables.add(editProfileUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(username::setValue, throwable -> username.setValue("Unknown")));

        disposables.add(editProfileUseCase.getDateOfBirth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateOfBirth::setValue, throwable -> dateOfBirth.setValue("N/A")));

        disposables.add(editProfileUseCase.getGender()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gender::setValue, throwable -> gender.setValue("Unknown")));

        disposables.add(editProfileUseCase.getPhoneNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(phoneNumber::setValue, throwable -> phoneNumber.setValue("Unknown")));

        disposables.add(editProfileUseCase.getEmail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(email::setValue, throwable -> phoneNumber.setValue("Unknown")));
    }


    public void update(Integer userId, String name, String gender, String phoneNumber, String email, String dob) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(editProfileUseCase.execute(userId, name, gender, phoneNumber, email, dob)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> isLoading.setValue(false))
                .subscribe(updateSuccessMessage::setValue, throwable -> errorMessage.setValue(throwable.getMessage())
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}