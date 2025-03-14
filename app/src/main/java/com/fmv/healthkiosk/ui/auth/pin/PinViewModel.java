package com.fmv.healthkiosk.ui.auth.pin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PinViewModel extends BaseViewModel {

    private final AccountUseCase accountUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    boolean isCreatingPin;

    public PinViewModel(SavedStateHandle savedStateHandle, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.isCreatingPin = getArgument("isCreatingPin");

        this.accountUseCase = accountUseCase;
        observeProfileData();
    }

    void setIsLoggedIn() {
        disposables.add(accountUseCase.setIsLoggedIn(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> {
                }));
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.isLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLoggedIn::setValue, throwable -> isLoggedIn.setValue(false)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
