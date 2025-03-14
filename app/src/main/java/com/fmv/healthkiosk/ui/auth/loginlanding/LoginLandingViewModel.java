package com.fmv.healthkiosk.ui.auth.loginlanding;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginLandingViewModel extends BaseViewModel {

    @Inject
    public LoginLandingViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
