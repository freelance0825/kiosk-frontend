package com.fmv.healthkiosk.ui;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends BaseViewModel {

    @Inject
    public MainViewModel(
            SavedStateHandle savedStateHandle
    ) {
        super(savedStateHandle);
    }
}
