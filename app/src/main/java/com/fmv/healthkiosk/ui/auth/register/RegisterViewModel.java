package com.fmv.healthkiosk.ui.auth.register;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

public class RegisterViewModel extends BaseViewModel {

    String loginType;

    public RegisterViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);

        this.loginType = getArgument("loginType");
    }
}
