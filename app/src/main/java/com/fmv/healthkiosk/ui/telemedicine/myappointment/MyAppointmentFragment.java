package com.fmv.healthkiosk.ui.telemedicine.myappointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentLoginLandingBinding;
import com.fmv.healthkiosk.databinding.FragmentMyAppointmentBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.ui.auth.loginlanding.LoginLandingFragmentDirections;
import com.fmv.healthkiosk.ui.auth.loginlanding.LoginLandingViewModel;
import com.fmv.healthkiosk.ui.telemedicine.myappointment.adapters.MyAppointmentAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyAppointmentFragment extends BaseFragment<FragmentMyAppointmentBinding, MyAppointmentViewModel> {

    private final MyAppointmentAdapter myAppointmentAdapter = new MyAppointmentAdapter();

    @Override
    protected Class<MyAppointmentViewModel> getViewModelClass() {
        return MyAppointmentViewModel.class;
    }

    @Override
    protected FragmentMyAppointmentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMyAppointmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.myAppointmentList.observe(getViewLifecycleOwner(), myAppointmentAdapter::submitList);
    }

    private void setViews() {
        binding.rvDoctors.setAdapter(myAppointmentAdapter);
        binding.rvDoctors.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNotification.setOnClickListener(v -> {
//            navigateToFragment(LoginLandingFragmentDirections.actionNavigationLoginLandingToNavigationLogin(loginType), false);
        });

        myAppointmentAdapter.setOnItemClickListener(new MyAppointmentAdapter.OnItemClickListener() {
            @Override
            public void onConsultNowClick(Doctor doctor, int position) {

            }

            @Override
            public void onConsultRescheduleClick(Doctor doctor, int position) {

            }

            @Override
            public void onConsultCancelClick(Doctor doctor, int position) {

            }
        });
    }
}
