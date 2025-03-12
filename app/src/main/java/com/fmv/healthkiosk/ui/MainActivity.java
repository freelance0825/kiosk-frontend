package com.fmv.healthkiosk.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.fmv.healthkiosk.core.base.BaseActivity;
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
        binding.tvCek.setText("Hello World!");
        binding.btnNext.setOnClickListener(v -> {
            Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
        });
    }
}
