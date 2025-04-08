package com.fmv.healthkiosk.ui.telemedicine.makeappointment;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentBookAppointmentBinding;
import com.fmv.healthkiosk.databinding.FragmentMakeAppointmentBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.ui.telemedicine.bookappointment.BookAppointmentViewModel;
import com.fmv.healthkiosk.ui.telemedicine.bookappointment.adapters.BookAppointmentAdapter;
import com.fmv.healthkiosk.ui.telemedicine.makeappointment.adapter.TimeSlotAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MakeAppointmentFragment extends BaseFragment<FragmentMakeAppointmentBinding, MakeAppointmentViewModel> {

    List<String> timeSlots = Arrays.asList(
            "08:00", "10:00", "12:00", "14:00", "16:00", "18:00",
            "20:00", "21:00", "22:00", "22:30", "23:00", "23:30"
    );

    @Override
    protected Class<MakeAppointmentViewModel> getViewModelClass() {
        return MakeAppointmentViewModel.class;
    }

    @Override
    protected FragmentMakeAppointmentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMakeAppointmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.isAppointmentSubmitted.observe(getViewLifecycleOwner(), isSubmitted -> {
            if (isSubmitted) {
                binding.makeAppointmentLayout.setVisibility(ViewGroup.GONE);
                binding.confirmAppointmentLayout.setVisibility(ViewGroup.VISIBLE);
            } else {
                binding.makeAppointmentLayout.setVisibility(ViewGroup.VISIBLE);
                binding.confirmAppointmentLayout.setVisibility(ViewGroup.GONE);
            }
        });

        viewModel.selectedDate.observe(getViewLifecycleOwner(), selectedDate -> {
            if (!selectedDate.isEmpty()) {
                binding.btnSelectDate.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_appointment_down_gradient));
                binding.tvSelectDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                binding.ivSelectDateDropdown.setImageTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
                );
            } else {
                binding.btnSelectDate.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_white_border));
                binding.tvSelectDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
                binding.ivSelectDateDropdown.setImageTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryBlue))
                );
            }
        });
    }

    private void setViews() {
        TimeSlotAdapter adapter = new TimeSlotAdapter(requireContext(), timeSlots, selectedTime -> {
            viewModel.selectedTime.setValue(selectedTime);
        });

        binding.rvTime.setAdapter(adapter);
        binding.rvTime.setLayoutManager(new GridLayoutManager(requireContext(), 6, GridLayoutManager.VERTICAL, false));

        binding.tvDoctorName.setText(viewModel.doctor.getName());
        binding.tvDoctorOccupation.setText(viewModel.doctor.getSpecialization());
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnSelectDate.setOnClickListener(v -> {
            showDatePicker();
        });

        binding.btnSubmit.setOnClickListener(v -> {
            if (viewModel.selectedDate.getValue().isEmpty() || viewModel.selectedTime.getValue().isEmpty()) {
                Toast.makeText(requireContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
            } else {
                String dateTime = viewModel.selectedDate.getValue() + ", " + viewModel.selectedTime.getValue();
                binding.tvDateTime.setText(dateTime);

                viewModel.isAppointmentSubmitted.setValue(true);
            }
        });

        binding.btnOkay.setOnClickListener(v -> {
            navigateBack();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(selectedYear, selectedMonth, selectedDay);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(selectedCal.getTime());

                    viewModel.selectedDate.setValue(formattedDate);
                    binding.tvSelectDate.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }
}
