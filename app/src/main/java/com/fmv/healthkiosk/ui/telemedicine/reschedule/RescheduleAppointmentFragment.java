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
                Log.e("FTEST", "setViews: " + updatedAppointment.getDoctor().getImageBase64());

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

        viewModel.doctorTimeslots.observe(getViewLifecycleOwner(), doctorTimeslots -> {
            if (doctorTimeslots != null) {
                TimeSlotAdapter adapter = new TimeSlotAdapter(requireContext(), doctorTimeslots.getAvailableTimeSlots(), selectedTime -> {
                    viewModel.selectedTime.setValue(selectedTime);
                });

                binding.rvTime.setAdapter(adapter);
                binding.rvTime.setLayoutManager(new GridLayoutManager(requireContext(), 6, GridLayoutManager.VERTICAL, false));

                binding.tvSelectTime.setVisibility(ViewGroup.VISIBLE);
                binding.rvTime.setVisibility(ViewGroup.VISIBLE);
            } else {
                binding.tvSelectTime.setVisibility(ViewGroup.GONE);
                binding.rvTime.setVisibility(ViewGroup.GONE);
            }
        });
    }

    private void setViews() {

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

                viewModel.updateMyAppointments();
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

                    SimpleDateFormat dateFormatForRequestTimeslots = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    viewModel.selectedDate.setValue(formattedDate);
                    binding.tvSelectDate.setText(formattedDate);

                    viewModel.getDoctorTimeslots(dateFormatForRequestTimeslots.format(selectedCal.getTime()));
                },
                year, month, day
        );

        // Ensure the minimum date is the current date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void notifyRescheduledAppointment() {
        Bundle result = new Bundle();
        result.putBoolean(REQUEST_RESCHEDULE_KEY_CHAT_UPDATED, true);
        getParentFragmentManager().setFragmentResult(REQUEST_RESCHEDULE_KEY_CHAT, result);
    }
}
