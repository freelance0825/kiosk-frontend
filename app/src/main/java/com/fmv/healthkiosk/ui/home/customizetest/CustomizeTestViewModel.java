package com.fmv.healthkiosk.ui.home.customizetest;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CustomizeTestViewModel extends BaseViewModel {

    @Inject
    public CustomizeTestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
