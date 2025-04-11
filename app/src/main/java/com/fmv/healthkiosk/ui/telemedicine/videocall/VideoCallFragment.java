package com.fmv.healthkiosk.ui.telemedicine.videocall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentVideoCallBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VideoCallFragment extends BaseFragment<FragmentVideoCallBinding, VideoCallViewModel> {

    @Override
    protected Class<VideoCallViewModel> getViewModelClass() {
        return VideoCallViewModel.class;
    }

    @Override
    protected FragmentVideoCallBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentVideoCallBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.timeString.observe(getViewLifecycleOwner(), timeString -> {
            binding.tvTimeCounter.setText(timeString);
        });

        viewModel.username.observe(getViewLifecycleOwner(), username -> {
            binding.tvPatientName.setText(username);
        });
    }

    private void setViews() {
        binding.tvDoctorName.setText(viewModel.doctorForNotification.getName());
        binding.tvDoctorSpecialization.setText(viewModel.doctorForNotification.getSpecialization());
    }

    private void setListeners() {
        binding.btnEndCall.setOnClickListener(v -> {
            navigateBack();
        });
    }
}
