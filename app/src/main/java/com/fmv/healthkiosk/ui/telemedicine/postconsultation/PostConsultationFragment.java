package com.fmv.healthkiosk.ui.telemedicine.postconsultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentPostConsultationBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.PostConsultationModel;

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
        setListeners();
    }

    private void observeViewModel() {
        viewModel.appointment.observe(getViewLifecycleOwner(), appointment -> {
            if (appointment != null) {
                setViews(appointment);
            } else {
                showNoAppointmentFound();
            }
        });
    }

    private void setViews(AppointmentModel appointment) {
        if (appointment.getDoctor() != null) {
            binding.tvDoctorName.setText(appointment.getDoctor().getName());
            binding.tvDoctorOccupation.setText(appointment.getDoctor().getSpecialization());
        } else {
            binding.tvDoctorName.setText("Doctor unavailable");
            binding.tvDoctorOccupation.setText("");
        }

        PostConsultationModel postConsultation = appointment.getPostConsultation();
        if (postConsultation != null) {
            binding.tvDoctorDiagnosis.setText(safeText(postConsultation.getDiagnosis(), "No diagnosis available"));
            binding.tvDoctorSuggestions.setText(safeText(postConsultation.getSuggestions(), "No suggestions available"));
        } else {
            binding.tvDoctorDiagnosis.setText("No diagnosis available");
            binding.tvDoctorSuggestions.setText("No suggestions available");
        }

        binding.tvDateTime.setText(formatDateTime(appointment.getDateTime()));
    }

    private void showNoAppointmentFound() {
        binding.tvDoctorName.setText("No appointment found.");
        binding.tvDoctorOccupation.setText("");
        binding.tvDoctorDiagnosis.setText("");
        binding.tvDoctorSuggestions.setText("");
        binding.tvDateTime.setText("");
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.btnViewReport.setOnClickListener(v -> {
            // TODO: Handle report viewing
        });

        binding.btnExportToPdf.setOnClickListener(v -> {
            // TODO: Handle exporting to PDF
        });

        binding.btnBackContent.setOnClickListener(v -> navigateBack());
    }

    private String formatDateTime(String isoDateTime) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = isoFormat.parse(isoDateTime);
            if (date != null) {
                SimpleDateFormat displayFormat = new SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault());
                return displayFormat.format(date);
            }
        } catch (Exception ignored) {}
        return "Invalid date";
    }

    private String safeText(String input, String fallback) {
        return (input == null || input.trim().isEmpty()) ? fallback : input;
    }
}
