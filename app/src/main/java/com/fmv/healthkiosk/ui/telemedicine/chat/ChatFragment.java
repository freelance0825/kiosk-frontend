package com.fmv.healthkiosk.ui.telemedicine.chat;

import static com.fmv.healthkiosk.ui.telemedicine.reschedule.RescheduleAppointmentFragment.REQUEST_RESCHEDULE_KEY_CHAT;
import static com.fmv.healthkiosk.ui.telemedicine.reschedule.RescheduleAppointmentFragment.REQUEST_RESCHEDULE_KEY_CHAT_UPDATED;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.FragmentChatBinding;
import com.fmv.healthkiosk.databinding.FragmentVideoCallBinding;
import com.fmv.healthkiosk.ui.telemedicine.chat.adapters.ChatAdapter;
import com.fmv.healthkiosk.ui.telemedicine.videocall.VideoCallViewModel;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends BaseFragment<FragmentChatBinding, ChatViewModel> {

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;


    private final ChatAdapter chatAdapter = new ChatAdapter();

    @Override
    protected Class<ChatViewModel> getViewModelClass() {
        return ChatViewModel.class;
    }

    @Override
    protected FragmentChatBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentChatBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setupSpeechToText();
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.chatMessageList.observe(getViewLifecycleOwner(), chatMessages -> {
            chatAdapter.submitList(chatMessages, () -> binding.rvChat.scrollToPosition(chatAdapter.getItemCount() - 1));
        });

        viewModel.updatedAppointment.observe(getViewLifecycleOwner(), appointment -> {
            if (appointment != null && !appointment.isEmpty()) {
                notifyRescheduledAppointment();
                String firstKey = appointment.keySet().iterator().next();
                viewModel.sendMessage(firstKey);
            }
        });

        viewModel.isStartingSpeech.observe(getViewLifecycleOwner(), isStarting -> {
            if (isStarting) {
                viewModel.isLoadingSpeech.setValue(true);
                binding.btnSpeechToChat.setVisibility(ViewGroup.GONE);
                binding.layoutStartSpeech.setVisibility(ViewGroup.VISIBLE);
                speechRecognizer.startListening(speechRecognizerIntent);
            } else {
                binding.btnSpeechToChat.setVisibility(ViewGroup.VISIBLE);
                binding.layoutStartSpeech.setVisibility(ViewGroup.GONE);
                speechRecognizer.stopListening();
            }
        });

        viewModel.isLoadingSpeech.observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressbarSpeech.setVisibility(isLoading ? ViewGroup.VISIBLE : ViewGroup.GONE);
            binding.layoutSpeechButtons.setVisibility(isLoading ? ViewGroup.GONE : ViewGroup.VISIBLE);
        });
    }

    private void setViews() {
        if (viewModel.doctorModel.getImageBase64() == null) {
            binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.asset_dummy_doctor));
        } else {
            binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(viewModel.doctorModel.getImageBase64()));
        }

        binding.tvDoctorName.setText(viewModel.doctorModel.getName());

        binding.rvChat.setAdapter(chatAdapter);
        binding.rvChat.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setListeners() {
        binding.btnFinish.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnSpeechToChat.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            } else {
                viewModel.isStartingSpeech.setValue(true);
            }
        });

        binding.btnClearSpeech.setOnClickListener(v -> {
            viewModel.isStartingSpeech.setValue(false);
            binding.edSpeechText.setText("");
            viewModel.speechMessage.setValue("");
        });

        binding.btnSubmitSpeech.setOnClickListener(v -> {
            viewModel.isStartingSpeech.setValue(false);
            binding.edSpeechText.setText("");
            viewModel.sendMessage(viewModel.speechMessage.getValue());
            viewModel.speechMessage.setValue("");
        });

        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onRescheduleSuggestionClick(String command, int position) {
                viewModel.sendMessage(command);
            }

            @Override
            public void onFirstDateSuggestionClick(String selectedDate, int position) {
                viewModel.updateMyAppointments(convertDateToDayMonth(selectedDate), selectedDate);
            }

            @Override
            public void onSecondDateSuggestionClick(String selectedDate, int position) {
                viewModel.updateMyAppointments(convertDateToDayMonth(selectedDate), selectedDate);
            }

            @Override
            public void onThirdDateSuggestionClick(String selectedDate, int position) {
                viewModel.updateMyAppointments(convertDateToDayMonth(selectedDate), selectedDate);
            }

            @Override
            public void onSelectAnotherDateClick(String command, int position) {
                showDatePicker(command);
            }
        });
    }

    private void showDatePicker(String message) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(Calendar.YEAR, selectedYear);
                    selectedCal.set(Calendar.MONTH, selectedMonth);
                    selectedCal.set(Calendar.DAY_OF_MONTH, selectedDay);
                    selectedCal.set(Calendar.HOUR_OF_DAY, 0);
                    selectedCal.set(Calendar.MINUTE, 0);
                    selectedCal.set(Calendar.SECOND, 0);
                    selectedCal.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS000", Locale.getDefault());
                    String formattedDate = dateFormat.format(selectedCal.getTime());

                    viewModel.updateMyAppointments(message, formattedDate);
                },
                year, month, day
        );

        // Ensure the minimum date is the current date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private String convertDateToDayMonth(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH);

        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
        return outputFormatter.format(dateTime);
    }

    private void notifyRescheduledAppointment() {
        Bundle result = new Bundle();
        result.putBoolean(REQUEST_RESCHEDULE_KEY_CHAT_UPDATED, true);
        getParentFragmentManager().setFragmentResult(REQUEST_RESCHEDULE_KEY_CHAT, result);
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
                    String finalMessage = matches.get(0);

                    viewModel.isLoadingSpeech.setValue(false);
                    binding.edSpeechText.setText(finalMessage); // display final result
                    viewModel.speechMessage.setValue(finalMessage);
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

    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
