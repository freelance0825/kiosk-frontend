package com.fmv.healthkiosk.ui.telemedicine.reschedule;

import static androidx.navigation.Navigation.findNavController;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.FragmentRescheduleAppointmentBinding;
import com.fmv.healthkiosk.ui.telemedicine.makeappointment.adapter.TimeSlotAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RescheduleAppointmentFragment extends BaseFragment<FragmentRescheduleAppointmentBinding, RescheduleAppointmentViewModel> {

    public static final String REQUEST_RESCHEDULE_KEY_CHAT = "request_reschedule_key_chat";
    public static final String REQUEST_RESCHEDULE_KEY_CHAT_UPDATED = "request_reschedule_key_chat_updated";

    List<String> timeSlots = Arrays.asList(
            "08:00", "10:00", "12:00", "14:00", "16:00", "18:00",
            "20:00", "21:00", "22:00", "22:30", "23:00", "23:30"
    );

    @Override
    protected Class<RescheduleAppointmentViewModel> getViewModelClass() {
        return RescheduleAppointmentViewModel.class;
    }

    @Override
    protected FragmentRescheduleAppointmentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentRescheduleAppointmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        getParentFragmentManager().setFragmentResultListener(REQUEST_RESCHEDULE_KEY_CHAT, getViewLifecycleOwner(), (key, bundle) -> {
            boolean updated = bundle.getBoolean(REQUEST_RESCHEDULE_KEY_CHAT_UPDATED, false);
            if (updated) {
                navigateBack();
            }
        });

        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.updatedAppointments.observe(getViewLifecycleOwner(), updatedAppointment -> {
            if (updatedAppointment != null) {
                Log.e("FTEST", "setViews: " + updatedAppointment.getDoctor().getImageBase64() );

                if (updatedAppointment.getDoctor().getImageBase64() != null) {
                    if (!updatedAppointment.getDoctor().getImageBase64().isEmpty()) {
                        binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(updatedAppointment.getDoctor().getImageBase64()));
                    }
                }

                binding.tvDoctorName.setText(updatedAppointment.getDoctor().getName());
                binding.tvDoctorOccupation.setText(updatedAppointment.getDoctor().getSpecialization());

                binding.updateAppointmentLayout.setVisibility(ViewGroup.GONE);
                binding.confirmAppointmentLayout.setVisibility(ViewGroup.VISIBLE);
            } else {
                binding.updateAppointmentLayout.setVisibility(ViewGroup.VISIBLE);
                binding.confirmAppointmentLayout.setVisibility(ViewGroup.GONE);
            }
        });

        viewModel.selectedDate.observe(getViewLifecycleOwner(), selectedDate -> {
            if (!selectedDate.isEmpty()) {
                // Update UI when a date is selected
                binding.btnSelectDate.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_appointment_down_gradient));
                binding.tvSelectDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                binding.ivSelectDateDropdown.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black)));
            } else {
                // Reset UI if no date is selected
                binding.btnSelectDate.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_white_border));
                binding.tvSelectDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
                binding.ivSelectDateDropdown.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryBlue)));
            }
        });
    }

    private void setViews() {
        TimeSlotAdapter adapter = new TimeSlotAdapter(requireContext(), timeSlots, selectedTime -> {
            viewModel.selectedTime.setValue(selectedTime);
        });

        binding.rvTime.setAdapter(adapter);
        binding.rvTime.setLayoutManager(new GridLayoutManager(requireContext(), 6, GridLayoutManager.VERTICAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnSelectDate.setOnClickListener(v -> {
            showDatePicker();
        });

        binding.btnSubmit.setOnClickListener(v -> {
            String selectedDate = viewModel.selectedDate.getValue();
            String selectedTime = viewModel.selectedTime.getValue();

            if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(requireContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
            } else {
                // Format for backend submission
                String dateTimeForSubmission = formatDateTime(selectedDate, selectedTime);
                viewModel.updateMyAppointments(dateTimeForSubmission);

                // Format for confirmation UI
                String dateTimeForDisplay = selectedDate + ", " + selectedTime;
                binding.tvDateTime.setText(dateTimeForDisplay);

                viewModel.isAppointmentSubmitted.setValue(true);
            }
        });

        binding.btnAssistantHelp.setOnClickListener(v -> {
           navigateToFragment(RescheduleAppointmentFragmentDirections.actionNavigationRescheduleAppointmentFragmentToNavigationChatFragment(viewModel.doctorModel, viewModel.appointmentId), false);
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

        // Ensure the minimum date is the current date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    // Helper method for formatting date and time into ISO 8601 format
    private String formatDateTime(String selectedDate, String selectedTime) {
        try {
            // Log inputs
            Log.d("Reschedule", "Selected date: " + selectedDate + " | Selected time: " + selectedTime);

            // Expected input: "22 April 2025" and "10:00"
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.ENGLISH); // force English
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

            String inputDateTime = selectedDate + ", " + selectedTime;
            Date parsedDate = inputFormat.parse(inputDateTime);

            if (parsedDate == null) {
                Toast.makeText(requireContext(), "Failed to parse date/time", Toast.LENGTH_SHORT).show();
                Log.e("Reschedule", "Parsed date is null for input: " + inputDateTime);
                return "";
            }

            String formattedDateTime = isoFormat.format(parsedDate);
            Log.d("Reschedule", "Formatted datetime: " + formattedDateTime);
            return formattedDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to format date and time", Toast.LENGTH_SHORT).show();
            return "";
        }
    }

}
