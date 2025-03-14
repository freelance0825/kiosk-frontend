package com.fmv.healthkiosk.ui.auth.landing;

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
public class LandingViewModel extends BaseViewModel {
    private final AccountUseCase accountUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    @Inject
    public LandingViewModel(SavedStateHandle savedStateHandle, AccountUseCase accountUseCase) {
        super(savedStateHandle);

        this.accountUseCase = accountUseCase;
        observeProfileData();
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
