package com.fmv.healthkiosk.ui.auth.loginlanding.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.LoginUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    private final LoginUseCase loginUseCase;

    String loginType;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<String> loginSuccessMessage = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public LoginViewModel(SavedStateHandle savedStateHandle, LoginUseCase loginUseCase) {
        super(savedStateHandle);
        this.loginType = getArgument("loginType");

        this.loginUseCase = loginUseCase;
    }

    public void loginPhoneNumber(String phoneNumber) {
        isLoading.postValue(true);
        errorMessage.postValue(null);
        disposables.add(
                loginUseCase.execute(phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.postValue(false))
                        .subscribe(
                                loginSuccessMessage::postValue,
                                throwable -> errorMessage.postValue(throwable.getMessage())
                        ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
