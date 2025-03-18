package com.fmv.healthkiosk.ui.home.customizetest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentCustomizeTestBinding;
import com.fmv.healthkiosk.databinding.FragmentMedicalPackageBinding;
import com.fmv.healthkiosk.ui.home.medicalpackage.MedicalPackageViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomizeTestFragment extends BaseFragment<FragmentCustomizeTestBinding, CustomizeTestViewModel> {

    @Override
    protected Class<CustomizeTestViewModel> getViewModelClass() {
        return CustomizeTestViewModel.class;
    }

    @Override
    protected FragmentCustomizeTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCustomizeTestBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {

    }

    private void setViews() {

    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());
    }
}
