package com.fmv.healthkiosk.ui.auth.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentLandingBinding;

public class LandingFragment extends BaseFragment<FragmentLandingBinding, LandingViewModel> {

    @Override
    protected Class<LandingViewModel> getViewModelClass() {
        return LandingViewModel.class;
    }

    @Override
    protected FragmentLandingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLandingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setListeners();
    }

    private void setListeners() {
        binding.btnLoginPatient.setOnClickListener(v -> navigateToFragment(LandingFragmentDirections.actionNavigationLandingToNavigationLoginLanding(), false));
        binding.btnRegisterPatient.setOnClickListener(v -> navigateToFragment(LandingFragmentDirections.actionNavigationLandingToNavigationRegister(), false));
    }
}
