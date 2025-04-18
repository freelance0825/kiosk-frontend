package com.fmv.healthkiosk.ui.telemedicine.postconsultation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.FragmentPostConsultationBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.PostConsultationModel;
import com.fmv.healthkiosk.ui.report.prescription.PdfPrescriptionReportActivity;
import com.fmv.healthkiosk.ui.report.testing.PdfTestingReportActivity;
import com.fmv.healthkiosk.ui.telemedicine.postconsultation.adapters.PostConsultationMedicineAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostConsultationFragment extends BaseFragment<FragmentPostConsultationBinding, PostConsultationViewModel> {

    private final PostConsultationMedicineAdapter postConsultationMedicineAdapter = new PostConsultationMedicineAdapter();

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
        if (appointment.getDoctor().getImageBase64() != null) {
            if (!appointment.getDoctor().getImageBase64().isEmpty()) {
                binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(appointment.getDoctor().getImageBase64()));
            }
        }

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

        Log.e("FTEST", "setViews: " + postConsultation.getMedicines().size() );

        postConsultationMedicineAdapter.submitList(postConsultation.getMedicines());

        binding.rvMedicines.setAdapter(postConsultationMedicineAdapter);
        binding.rvMedicines.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
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

        binding.btnExportToPdf.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PdfPrescriptionReportActivity.EXTRA_APPOINTMENT_RESULT, viewModel.appointment.getValue());

            navigateToActivity(PdfPrescriptionReportActivity.class, bundle);
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
