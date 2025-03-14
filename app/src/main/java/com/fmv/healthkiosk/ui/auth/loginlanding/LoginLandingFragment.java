package com.fmv.healthkiosk.ui.auth.loginlanding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentLoginLandingBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginLandingFragment extends BaseFragment<FragmentLoginLandingBinding, LoginLandingViewModel> {

    @Override
    protected Class<LoginLandingViewModel> getViewModelClass() {
        return LoginLandingViewModel.class;
    }

    @Override
    protected FragmentLoginLandingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLoginLandingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setListeners();
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnLoginPhoneNumber.setOnClickListener(v -> {
            String loginType = getString(R.string.fragment_login_landing_mobile_number);

            navigateToFragment(LoginLandingFragmentDirections.actionNavigationLoginLandingToNavigationLogin(loginType), false);
        });

        binding.btnLoginHealthCard.setOnClickListener(v -> {
            String loginType = getString(R.string.fragment_login_landing_health_card);

            navigateToFragment(LoginLandingFragmentDirections.actionNavigationLoginLandingToNavigationLogin(loginType), false);
        });
    }
}
