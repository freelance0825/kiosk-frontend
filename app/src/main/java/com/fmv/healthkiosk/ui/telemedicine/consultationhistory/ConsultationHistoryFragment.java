package com.fmv.healthkiosk.ui.telemedicine.consultationhistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentConsultationHistoryBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.ui.telemedicine.consultationhistory.adapters.ConsultationHistoryAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConsultationHistoryFragment extends BaseFragment<FragmentConsultationHistoryBinding, ConsultationHistoryViewModel> {

    private final ConsultationHistoryAdapter consultationHistoryAdapter = new ConsultationHistoryAdapter();

    @Override
    protected Class<ConsultationHistoryViewModel> getViewModelClass() {
        return ConsultationHistoryViewModel.class;
    }

    @Override
    protected FragmentConsultationHistoryBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentConsultationHistoryBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.myAppointmentList.observe(getViewLifecycleOwner(), consultationHistoryAdapter::submitList);
    }

    private void setViews() {
        binding.rvDoctors.setAdapter(consultationHistoryAdapter);
        binding.rvDoctors.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNotification.setOnClickListener(v -> {
            navigateToFragment(ConsultationHistoryFragmentDirections.actionNavigationConsultationHistoryFragmentToNavigationNotificationFragment(), false);
        });

        binding.btnBookAppointment.setOnClickListener(v -> {
            navigateToFragment(ConsultationHistoryFragmentDirections.actionNavigationConsultationHistoryFragmentToNavigationBookAppointmentFragment(), false);
        });

        consultationHistoryAdapter.setOnItemClickListener(new ConsultationHistoryAdapter.OnItemClickListener() {
            @Override
            public void onBookAgainClick(AppointmentModel appointment, int position) {
                navigateToFragment(ConsultationHistoryFragmentDirections.actionNavigationConsultationHistoryFragmentToNavigationMakeAppointmentFragment(appointment.getDoctor()), false);
            }

            @Override
            public void onViewReportClick(AppointmentModel appointment, int position) {
                if (appointment.getPostConsultation() == null) {
                    Toast.makeText(requireContext(), "Report isn't ready yet!", Toast.LENGTH_SHORT).show();
                } else {
                    navigateToFragment(ConsultationHistoryFragmentDirections.actionNavigationConsultationHistoryFragmentToNavigationPostConsultationFragment(appointment.getId()), false);
                }
            }
        });
    }
}
