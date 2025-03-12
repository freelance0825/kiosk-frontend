package com.fmv.healthkiosk.core.base;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public abstract class BaseViewModel extends ViewModel {
    protected final SavedStateHandle savedStateHandle;

    public BaseViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    protected <T> T getArgument(String key) {
        return savedStateHandle.get(key);
    }
}
