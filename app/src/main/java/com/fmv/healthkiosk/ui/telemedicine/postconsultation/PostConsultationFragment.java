package com.fmv.healthkiosk.ui.telemedicine.postconsultation;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentMakeAppointmentBinding;
import com.fmv.healthkiosk.databinding.FragmentPostConsultationBinding;
import com.fmv.healthkiosk.ui.telemedicine.makeappointment.MakeAppointmentViewModel;
import com.fmv.healthkiosk.ui.telemedicine.makeappointment.adapter.TimeSlotAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        binding.tvDoctorName.setText(viewModel.doctor.getName());
        binding.tvDoctorOccupation.setText(viewModel.doctor.getSpecialization());
        binding.tvDateTime.setText(formatDateTime(viewModel.doctor.getDateTime()));
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
