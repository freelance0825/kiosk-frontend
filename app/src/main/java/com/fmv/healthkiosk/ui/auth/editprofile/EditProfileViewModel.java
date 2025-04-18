package com.fmv.healthkiosk.ui.auth.editprofile;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.EditProfileUseCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    final MutableLiveData<String> userAge = new MutableLiveData<>();
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

        // Load and observe dateOfBirth, then compute age immediately
        disposables.add(editProfileUseCase.getDateOfBirth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dob -> {
                            dateOfBirth.setValue(dob);
                            updateAge(dob); // Initialize userAge based on stored DOB
                        },
                        t -> {
                            dateOfBirth.setValue("N/A");
                            userAge.setValue("0");
                        }
                ));
    }

    public void update(Integer userId, String name, String gender, String phoneNumber, String email, String dob) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(editProfileUseCase.execute(userId, name, gender, phoneNumber, email, dob, userAge.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> isLoading.setValue(false))
                .subscribe(updateSuccessMessage::setValue, throwable -> errorMessage.setValue(throwable.getMessage())
                ));
    }

    public void updateAge(String dob) {
        if (dob == null || dob.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date birthDate = sdf.parse(dob);
            if (birthDate == null) return;

            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(birthDate);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

            if (age < 0) {
                age = 0;
            }

            userAge.setValue(String.valueOf(age));
        } catch (ParseException e) {
            Log.e("RegisterViewModel", "updateAge: " + e.getMessage());
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}