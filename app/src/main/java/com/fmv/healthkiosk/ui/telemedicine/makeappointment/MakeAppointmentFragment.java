package com.fmv.healthkiosk.ui.telemedicine.makeappointment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.FragmentMakeAppointmentBinding;
import com.fmv.healthkiosk.ui.telemedicine.makeappointment.adapter.TimeSlotAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MakeAppointmentFragment extends BaseFragment<FragmentMakeAppointmentBinding, MakeAppointmentViewModel> {

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

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
        setupSpeechToText();
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

        viewModel.isStartingSpeech.observe(getViewLifecycleOwner(), isStarting -> {
            if (isStarting) {
                viewModel.isLoadingSpeech.setValue(true);
                binding.btnTellComplaints.setVisibility(ViewGroup.GONE);
                binding.layoutStartSpeech.setVisibility(ViewGroup.VISIBLE);
                speechRecognizer.startListening(speechRecognizerIntent);
            } else {
                binding.btnTellComplaints.setVisibility(ViewGroup.VISIBLE);
                binding.layoutStartSpeech.setVisibility(ViewGroup.GONE);
                speechRecognizer.stopListening();
            }
        });

        viewModel.isLoadingSpeech.observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressbarSpeech.setVisibility(isLoading ? ViewGroup.VISIBLE : ViewGroup.GONE);
            binding.btnClearSpeech.setVisibility(isLoading ? ViewGroup.GONE : ViewGroup.VISIBLE);
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

        if (viewModel.doctor.getImageBase64() != null) {
            if (!viewModel.doctor.getImageBase64().isEmpty()) {
                binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(viewModel.doctor.getImageBase64()));
            }
        }

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

        binding.btnTellComplaints.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            } else {
                viewModel.isStartingSpeech.setValue(true);
            }
        });

        binding.btnClearSpeech.setOnClickListener(v -> {
            viewModel.isStartingSpeech.setValue(false);
            binding.edSpeechText.setText("");
            viewModel.healthComplaints.setValue("");
        });

        binding.btnSubmit.setOnClickListener(v -> {
            if (viewModel.healthComplaints.getValue().isEmpty()) {
                Toast.makeText(requireContext(), "Please tell you Health Complaints first", Toast.LENGTH_SHORT).show();
            } else if (viewModel.selectedDate.getValue().isEmpty() || viewModel.selectedTime.getValue().isEmpty()) {
                Toast.makeText(requireContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
            } else {
                String dateTime = viewModel.selectedDate.getValue() + ", " + viewModel.selectedTime.getValue();
                binding.tvDateTime.setText(dateTime);

                viewModel.makeAppointment();
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

    private void setupSpeechToText() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {}

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String finalComplaints = matches.get(0);

                    viewModel.isLoadingSpeech.setValue(false);
                    binding.edSpeechText.setText(finalComplaints); // display final result
                    viewModel.healthComplaints.setValue(finalComplaints);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> partialMatches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (partialMatches != null && !partialMatches.isEmpty()) {
                    binding.edSpeechText.setText(partialMatches.get(0)); // live update while speaking
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
