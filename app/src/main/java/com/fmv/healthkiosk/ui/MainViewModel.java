package com.fmv.healthkiosk.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends BaseViewModel {

    private final AccountUseCase accountUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    final MutableLiveData<String> username = new MutableLiveData<>();
    final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    final MutableLiveData<String> gender = new MutableLiveData<>();
    final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    final MutableLiveData<Integer> age = new MutableLiveData<>();

    final MutableLiveData<Boolean> isShowingDeviceInfo = new MutableLiveData<>(false);

    @Inject
    public MainViewModel(
            SavedStateHandle savedStateHandle,
            AccountUseCase accountUseCase
    ) {
        super(savedStateHandle);
        this.accountUseCase = accountUseCase;
        observeProfileData();
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn::setValue, throwable -> isLoggedIn.setValue(false)));

        disposables.add(accountUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(username::setValue, throwable -> username.setValue("Unknown")));

        disposables.add(accountUseCase.getDateOfBirth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateOfBirth::setValue, throwable -> dateOfBirth.setValue("N/A")));

        disposables.add(accountUseCase.getGender()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gender::setValue, throwable -> gender.setValue("Unknown")));

        disposables.add(accountUseCase.getPhoneNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(phoneNumber::setValue, throwable -> phoneNumber.setValue("Unknown")));

        disposables.add(accountUseCase.getAge()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(age::setValue, throwable -> age.setValue(0)));
    }

    public void logout() {
        disposables.add(accountUseCase.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> isLoggedIn.setValue(false), throwable -> {
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
