package com.fmv.healthkiosk.ui.home.landing;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeLandingViewModel extends BaseViewModel {

    @Inject
    public HomeLandingViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
