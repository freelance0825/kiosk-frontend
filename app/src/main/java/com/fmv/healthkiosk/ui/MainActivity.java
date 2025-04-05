package com.fmv.healthkiosk.ui;

import static androidx.navigation.ActivityKt.findNavController;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseActivity;
import com.fmv.healthkiosk.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.isLoggedIn.observe(this, isLoggedIn -> {
            binding.layoutProfile.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);

            if (!isLoggedIn) {
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_landing);
            }
        });

        viewModel.username.observe(this, username -> {
            binding.tvPatientName.setText(username);
        });

        viewModel.dateOfBirth.observe(this, dateOfBirth -> {
            binding.tvPatientDateOfBirth.setText(dateOfBirth);
        });

        viewModel.gender.observe(this, gender -> {
            binding.tvPatientGender.setText(gender);
        });

        viewModel.phoneNumber.observe(this, phoneNumber -> {
            binding.tvPatientPhone.setText(phoneNumber);
        });

        viewModel.age.observe(this, age -> {
            binding.tvPatientAge.setText(String.valueOf(age));
        });
    }

    private void setListeners() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

                if (navController.getCurrentDestination() != null && navController.popBackStack()) {
                    return;
                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.btnLogout.setOnClickListener(v -> viewModel.logout());
    }
}
