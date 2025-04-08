package com.fmv.healthkiosk.ui.telemedicine.consultnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentConsultNowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.ui.telemedicine.bookappointment.BookAppointmentFragmentDirections;
import com.fmv.healthkiosk.ui.telemedicine.bookappointment.adapters.BookAppointmentAdapter;
import com.fmv.healthkiosk.ui.telemedicine.consultnow.adapters.ConsultNowAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConsultNowFragment extends BaseFragment<FragmentConsultNowBinding, ConsultNowViewModel> {

    private final ConsultNowAdapter consultNowAdapter = new ConsultNowAdapter();

    @Override
    protected Class<ConsultNowViewModel> getViewModelClass() {
        return ConsultNowViewModel.class;
    }

    @Override
    protected FragmentConsultNowBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentConsultNowBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.doctorList.observe(getViewLifecycleOwner(), consultNowAdapter::submitList);
    }

    private void setViews() {
        binding.rvDoctors.setAdapter(consultNowAdapter);
        binding.rvDoctors.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNotification.setOnClickListener(v -> {
            navigateToFragment(ConsultNowFragmentDirections.actionNavigationConsultNowToNavigationNotificationFragment(), false);
        });

        binding.btnBookAppointment.setOnClickListener(v -> {
            navigateToFragment(ConsultNowFragmentDirections.actionNavigationConsultNowToNavigationBookAppointmentFragment(), false);
        });

        consultNowAdapter.setOnItemClickListener((doctor, position) -> navigateToFragment(ConsultNowFragmentDirections.actionNavigationConsultNowToNavigationVideoCall(doctor), false));
    }
}
