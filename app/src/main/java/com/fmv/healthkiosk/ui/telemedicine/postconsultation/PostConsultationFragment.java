package com.fmv.healthkiosk.ui.telemedicine.postconsultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentPostConsultationBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostConsultationFragment extends BaseFragment<FragmentPostConsultationBinding, PostConsultationViewModel> {

    @Override
    protected Class<PostConsultationViewModel> getViewModelClass() {
        return PostConsultationViewModel.class;
    }

    @Override
    protected FragmentPostConsultationBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentPostConsultationBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {

    }

    private void setViews() {
        binding.tvDoctorName.setText(viewModel.doctorForNotification.getName());
        binding.tvDoctorOccupation.setText(viewModel.doctorForNotification.getSpecialization());
        binding.tvDateTime.setText(formatDateTime(viewModel.doctorForNotification.getDateTime()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnViewReport.setOnClickListener(v -> {

        });

        binding.btnExportToPdf.setOnClickListener(v -> {

        });

        binding.btnBackContent.setOnClickListener(v -> {
            navigateBack();
        });
    }

    public String formatDateTime(long dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(dateTime));
    }
}
