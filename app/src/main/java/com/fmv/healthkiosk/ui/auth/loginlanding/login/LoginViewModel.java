package com.fmv.healthkiosk.ui.auth.loginlanding.login;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

public class LoginViewModel extends BaseViewModel {

    String loginType;

    public LoginViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);

        this.loginType = getArgument("loginType");
    }
}
