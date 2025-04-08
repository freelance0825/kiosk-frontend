package com.fmv.healthkiosk.ui.telemedicine.consultationhistory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentConsultationHistoryBinding;
import com.fmv.healthkiosk.databinding.FragmentMyAppointmentBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.ui.telemedicine.consultationhistory.adapters.ConsultationHistoryAdapter;
import com.fmv.healthkiosk.ui.telemedicine.myappointment.MyAppointmentFragmentDirections;
import com.fmv.healthkiosk.ui.telemedicine.myappointment.MyAppointmentViewModel;
import com.fmv.healthkiosk.ui.telemedicine.myappointment.adapters.MyAppointmentAdapter;

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
            public void onBookAgainClick(Doctor doctor, int position) {
                navigateToFragment(ConsultationHistoryFragmentDirections.actionNavigationConsultationHistoryFragmentToNavigationMakeAppointmentFragment(doctor), false);
            }

            @Override
            public void onViewReportClick(Doctor doctor, int position) {
                navigateToFragment(ConsultationHistoryFragmentDirections.actionNavigationConsultationHistoryFragmentToNavigationPostConsultationFragment(doctor), false);
            }
        });
    }
}
