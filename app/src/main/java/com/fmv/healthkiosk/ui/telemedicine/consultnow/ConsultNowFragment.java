package com.fmv.healthkiosk.ui.telemedicine.consultnow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.customview.MarginItemDecoration;
import com.fmv.healthkiosk.databinding.FragmentConsultNowBinding;
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
        viewModel.pagedDoctorItems.observe(getViewLifecycleOwner(), consultNowAdapter::submitList);

        viewModel.showNextDoctorButton.observe(getViewLifecycleOwner(), show -> binding.btnNextDoctors.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
        viewModel.showBackDoctorButton.observe(getViewLifecycleOwner(), show -> binding.btnBackDoctors.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
    }

    private void setViews() {
        binding.rvDoctors.setAdapter(consultNowAdapter);
        binding.rvDoctors.addItemDecoration(new MarginItemDecoration(0, 0, 0, 36, MarginItemDecoration.LastPaddingToBeExcluded.BOTTOM));
        binding.rvDoctors.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNextDoctors.setOnClickListener(v -> viewModel.nextDoctorPage());
        binding.btnBackDoctors.setOnClickListener(v -> viewModel.previousDoctorPage());

        binding.btnNotification.setOnClickListener(v -> {
            navigateToFragment(ConsultNowFragmentDirections.actionNavigationConsultNowToNavigationNotificationFragment(), false);
        });

        binding.btnBookAppointment.setOnClickListener(v -> {
            navigateToFragment(ConsultNowFragmentDirections.actionNavigationConsultNowToNavigationBookAppointmentFragment(), false);
        });

        consultNowAdapter.setOnItemClickListener((doctor, position) -> navigateToFragment(ConsultNowFragmentDirections.actionNavigationConsultNowToNavigationVideoCall(doctor), false));
    }
}
