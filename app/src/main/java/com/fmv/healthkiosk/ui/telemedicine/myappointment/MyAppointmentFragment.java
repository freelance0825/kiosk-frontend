package com.fmv.healthkiosk.ui.telemedicine.myappointment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.customview.MarginItemDecoration;
import com.fmv.healthkiosk.databinding.FragmentMyAppointmentBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.ui.telemedicine.consultnow.ConsultNowFragmentDirections;
import com.fmv.healthkiosk.ui.telemedicine.myappointment.adapters.MyAppointmentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        viewModel.pagedDoctorItems.observe(getViewLifecycleOwner(), myAppointmentAdapter::submitList);

        viewModel.showNextDoctorButton.observe(getViewLifecycleOwner(), show -> binding.btnNextDoctors.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
        viewModel.showBackDoctorButton.observe(getViewLifecycleOwner(), show -> binding.btnBackDoctors.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));

        viewModel.selectedAppointmentToCancel.observe(getViewLifecycleOwner(), doctor -> {
            binding.layoutCancelAppointmentConfirmation.setVisibility(doctor != null ? VISIBLE : GONE);
            binding.btnBookAppointment.setEnabled(doctor == null);
            binding.btnBack.setEnabled(doctor == null);
            binding.btnNotification.setEnabled(doctor == null);
        });
    }

    private void setViews() {
        binding.rvDoctors.setAdapter(myAppointmentAdapter);
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
            navigateToFragment(MyAppointmentFragmentDirections.actionNavigationMyAppointmentFragmentToNavigationNotificationFragment(), false);
        });

        binding.btnBookAppointment.setOnClickListener(v -> {
            navigateToFragment(MyAppointmentFragmentDirections.actionNavigationMyAppointmentFragmentToNavigationBookAppointmentFragment(), false);
        });

        binding.btnYes.setOnClickListener(v -> {
            Integer appointmentId = viewModel.selectedAppointmentToCancel.getValue();
            if (appointmentId != null) {
                viewModel.cancelAppointment(appointmentId);
                Toast.makeText(requireContext(), "Appointment cancelled successfully", Toast.LENGTH_SHORT).show();
                // Close the confirmation layout
                viewModel.selectedAppointmentToCancel.setValue(null);
            }
        });

        binding.btnNo.setOnClickListener(v -> {
            viewModel.selectedAppointmentToCancel.setValue(null);
        });

        // When cancel is overlayed this listener maybe still listen to the click event
        myAppointmentAdapter.setOnItemClickListener(new MyAppointmentAdapter.OnItemClickListener() {
            @Override
            public void onConsultNowClick(AppointmentModel appointment, int position) {
                navigateToFragment(MyAppointmentFragmentDirections.actionNavigationMyAppointmentFragmentToNavigationVideoCall(appointment.getDoctor()), false);
            }

            @Override
            public void onConsultRescheduleClick(AppointmentModel appointment, int position) {
                navigateToFragment(MyAppointmentFragmentDirections.actionNavigationMyAppointmentFragmentToNavigationRescheduleAppointmentFragment(appointment.getId(), appointment.getDoctor()), false);
            }

            @Override
            public void onConsultCancelClick(AppointmentModel appointment, int position) {
                viewModel.selectedAppointmentToCancel.setValue(appointment.getId());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getMyAppointments();
    }

}
