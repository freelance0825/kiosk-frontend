package com.fmv.healthkiosk.ui.auth.loginlanding.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentLoginBinding;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> {

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected FragmentLoginBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setViews();
        setListeners();
    }

    private void setViews() {
        binding.tvTitleLoginType.setText(viewModel.loginType);
        binding.tvLoginType.setText(viewModel.loginType);

        if (viewModel.loginType.equals(getString(R.string.fragment_login_landing_mobile_number))) {
            binding.layoutPhoneNumber.setVisibility(View.VISIBLE);
            binding.layoutHealthUnique.setVisibility(View.GONE);
        } else {
            binding.layoutPhoneNumber.setVisibility(View.GONE);
            binding.layoutHealthUnique.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.btnSubmit.setOnClickListener(v -> {
            boolean isCreatingPin = false;

            navigateToFragment(LoginFragmentDirections.actionNavigationLoginToNavigationPin(isCreatingPin), false);
        });
    }
}
