package com.fmv.healthkiosk.ui.home.medicalpackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentLoginBinding;
import com.fmv.healthkiosk.databinding.FragmentMedicalPackageBinding;
import com.fmv.healthkiosk.ui.auth.loginlanding.login.LoginFragmentDirections;
import com.fmv.healthkiosk.ui.auth.loginlanding.login.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MedicalPackageFragment extends BaseFragment<FragmentMedicalPackageBinding, MedicalPackageViewModel> {

    @Override
    protected Class<MedicalPackageViewModel> getViewModelClass() {
        return MedicalPackageViewModel.class;
    }

    @Override
    protected FragmentMedicalPackageBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMedicalPackageBinding.inflate(inflater, container, false);
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

        binding.btnStartTest.setOnClickListener(v -> navigateToFragment(MedicalPackageFragmentDirections.actionNavigationMedicalPackageToNavigationCustomizeTest(), false));
    }
}
