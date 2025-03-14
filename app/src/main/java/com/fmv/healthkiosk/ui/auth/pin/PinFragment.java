package com.fmv.healthkiosk.ui.auth.pin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentPinBinding;

public class PinFragment extends BaseFragment<FragmentPinBinding, PinViewModel> {

    @Override
    protected Class<PinViewModel> getViewModelClass() {
        return PinViewModel.class;
    }

    @Override
    protected FragmentPinBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentPinBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setViews();
        setListeners();
    }

    private void setViews() {
        if (viewModel.isCreatingPin) {
            binding.tvTitlePinType.setText(getString(R.string.fragment_pin_create));
        } else {
            binding.tvTitlePinType.setText(getString(R.string.fragment_pin_input));
        }
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnVerify.setOnClickListener(v -> {
            navigateToFragment(PinFragmentDirections.actionNavigationPinToNavigationHomeLanding(), true);
        });
    }
}
