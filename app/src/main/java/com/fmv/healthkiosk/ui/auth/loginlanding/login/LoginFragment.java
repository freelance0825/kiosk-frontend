package com.fmv.healthkiosk.ui.auth.loginlanding.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentLoginBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.isLoading.observe(this, isLoading -> {

        });

        viewModel.loginSuccessMessage.observe(this, successMessage -> {
            if (successMessage != null) {
                navigateToPin();
            }
        });

        viewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null) {

            }
        });
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
            String phoneNumber = binding.edPhoneNumber.getText().toString().trim();

            if (viewModel.loginType.equals(getString(R.string.fragment_login_landing_mobile_number))) {
                if (isValid(phoneNumber)) {
                    viewModel.loginPhoneNumber(getString(R.string.fragment_login_mobile_number_country_code) + phoneNumber);
                }
            } else {

            }
        });
    }

    private boolean isValid(String edField) {
        return !edField.isEmpty();
    }

    private void navigateToPin() {
        boolean isCreatingPin = false;
        navigateToFragment(LoginFragmentDirections.actionNavigationLoginToNavigationPin(isCreatingPin), false);
    }
}
