package com.fmv.healthkiosk.ui.auth.pin;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

public class PinViewModel extends BaseViewModel {

    boolean isCreatingPin;

    public PinViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);

        this.isCreatingPin = getArgument("isCreatingPin");
    }
}
