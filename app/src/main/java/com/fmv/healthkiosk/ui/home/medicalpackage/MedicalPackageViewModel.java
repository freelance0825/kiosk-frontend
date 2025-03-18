package com.fmv.healthkiosk.ui.home.medicalpackage;

import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MedicalPackageViewModel extends BaseViewModel {

    @Inject
    public MedicalPackageViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
