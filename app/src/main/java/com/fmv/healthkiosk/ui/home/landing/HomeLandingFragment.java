package com.fmv.healthkiosk.ui.home.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentHomeLandingBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeLandingFragment extends BaseFragment<FragmentHomeLandingBinding, HomeLandingViewModel> {

    @Override
    protected Class<HomeLandingViewModel> getViewModelClass() {
        return HomeLandingViewModel.class;
    }

    @Override
    protected FragmentHomeLandingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeLandingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setListeners();
    }

    private void observeViewModel() {
        viewModel.username.observe(this, username -> binding.tvTitlePinType.setText(getString(R.string.fragment_home_welcome_name, username)));
    }

    private void setListeners() {
        binding.btnAdvancedTest.setOnClickListener(v -> navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationMedicalPackage(), false));
    }
}
