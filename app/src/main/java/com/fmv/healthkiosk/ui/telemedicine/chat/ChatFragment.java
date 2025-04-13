package com.fmv.healthkiosk.ui.telemedicine.chat;

import static com.fmv.healthkiosk.ui.telemedicine.reschedule.RescheduleAppointmentFragment.REQUEST_RESCHEDULE_KEY_CHAT;
import static com.fmv.healthkiosk.ui.telemedicine.reschedule.RescheduleAppointmentFragment.REQUEST_RESCHEDULE_KEY_CHAT_UPDATED;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends BaseFragment<FragmentChatBinding, ChatViewModel> {

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
}
