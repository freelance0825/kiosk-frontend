package com.fmv.healthkiosk.core.base.ui;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {
    protected final SavedStateHandle savedStateHandle;

    public BaseViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    protected <T> T getArgument(String key) {
        return savedStateHandle.get(key);
    }
}
