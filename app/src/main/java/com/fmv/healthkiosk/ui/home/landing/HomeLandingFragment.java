package com.fmv.healthkiosk.ui.home.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentHomeLandingBinding;

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
        setListeners();
    }

    private void setListeners() {

    }
}
