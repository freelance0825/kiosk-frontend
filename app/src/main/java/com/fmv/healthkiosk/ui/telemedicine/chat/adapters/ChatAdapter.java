package com.fmv.healthkiosk.ui.telemedicine.chat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.databinding.ItemDoctorChatDateRowBinding;
import com.fmv.healthkiosk.databinding.ItemDoctorChatMenuRowBinding;
import com.fmv.healthkiosk.databinding.ItemDoctorChatRowBinding;
import com.fmv.healthkiosk.databinding.ItemDoctorChatSelectDateRowBinding;
import com.fmv.healthkiosk.databinding.ItemPatientChatRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;
import com.fmv.healthkiosk.feature.telemedicine.utils.ChatbotCommands;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.temporal.ChronoField;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends ListAdapter<ChatMessage, RecyclerView.ViewHolder> {

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_ASSISTANT = 2;
    private static final int VIEW_TYPE_STARTING_MENU_ASSISTANT = 3;
    private static final int VIEW_TYPE_DATE_SUGGESTION_ASSISTANT = 4;
    private static final int VIEW_TYPE_SELECT_ANOTHER_DATE_ASSISTANT = 5;

    public ChatAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isFromUser()) {
            return VIEW_TYPE_USER;
        } else {
            if (getItem(position).getMessage().toLowerCase(Locale.ROOT).contains(ChatbotCommands.STARTING_MENU)) {
                return VIEW_TYPE_STARTING_MENU_ASSISTANT;
            } else if (getItem(position).getMessage().toLowerCase(Locale.ROOT).contains(ChatbotCommands.SUGGESTION_DATE_MENU)) {
                return VIEW_TYPE_DATE_SUGGESTION_ASSISTANT;
            } else if (getItem(position).getMessage().toLowerCase(Locale.ROOT).contains(ChatbotCommands.ANOTHER_DATE_MENU)) {
                return VIEW_TYPE_SELECT_ANOTHER_DATE_ASSISTANT;
            } else {
                return VIEW_TYPE_ASSISTANT;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_USER) {
            ItemPatientChatRowBinding binding = ItemPatientChatRowBinding.inflate(inflater, parent, false);
            return new UserViewHolder(binding);
        } else if (viewType == VIEW_TYPE_STARTING_MENU_ASSISTANT) {
            ItemDoctorChatMenuRowBinding binding = ItemDoctorChatMenuRowBinding.inflate(inflater, parent, false);
            return new StartingMenuAssistantViewHolder(binding);
        } else if (viewType == VIEW_TYPE_DATE_SUGGESTION_ASSISTANT) {
            ItemDoctorChatDateRowBinding binding = ItemDoctorChatDateRowBinding.inflate(inflater, parent, false);
            return new DateSuggestionAssistantViewHolder(binding);
        } else if (viewType == VIEW_TYPE_SELECT_ANOTHER_DATE_ASSISTANT) {
            ItemDoctorChatSelectDateRowBinding binding = ItemDoctorChatSelectDateRowBinding.inflate(inflater, parent, false);
            return new SelectDateAssistantViewHolder(binding);
        } else {
            ItemDoctorChatRowBinding binding = ItemDoctorChatRowBinding.inflate(inflater, parent, false);
            return new DoctorAssistantViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = getItem(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message);
        } else if (holder instanceof DoctorAssistantViewHolder) {
            ((DoctorAssistantViewHolder) holder).bind(message);
        } else if (holder instanceof StartingMenuAssistantViewHolder) {
            ((StartingMenuAssistantViewHolder) holder).binding.btnReschedule.setOnClickListener(v -> listener.onRescheduleSuggestionClick(ChatbotCommands.RESCHEDULE_DATE, position));
        } else if (holder instanceof DateSuggestionAssistantViewHolder) {
            List<String> randomDates = generateRandomFutureDates(3);

            ((DateSuggestionAssistantViewHolder) holder).binding.btnFirstDate.setText(convertDateToDayMonth(randomDates.get(0)));
            ((DateSuggestionAssistantViewHolder) holder).binding.btnSecondDate.setText(convertDateToDayMonth(randomDates.get(1)));
            ((DateSuggestionAssistantViewHolder) holder).binding.btnThirdDate.setText(convertDateToDayMonth(randomDates.get(2)));

            ((DateSuggestionAssistantViewHolder) holder).binding.btnFirstDate.setOnClickListener(v -> listener.onFirstDateSuggestionClick(randomDates.get(0), position));
            ((DateSuggestionAssistantViewHolder) holder).binding.btnSecondDate.setOnClickListener(v -> listener.onSecondDateSuggestionClick(randomDates.get(1), position));
            ((DateSuggestionAssistantViewHolder) holder).binding.btnThirdDate.setOnClickListener(v -> listener.onThirdDateSuggestionClick(randomDates.get(2), position));
        } else if (holder instanceof SelectDateAssistantViewHolder) {
            ((SelectDateAssistantViewHolder) holder).binding.btnSelectDate.setOnClickListener(v -> listener.onSelectAnotherDateClick(ChatbotCommands.DONE_RESCHEDULE_DATE, position));
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemPatientChatRowBinding binding;

        public UserViewHolder(@NonNull ItemPatientChatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessage message) {
            binding.tvChat.setText(message.getMessage());
        }
    }

    static class DoctorAssistantViewHolder extends RecyclerView.ViewHolder {
        private final ItemDoctorChatRowBinding binding;

        public DoctorAssistantViewHolder(@NonNull ItemDoctorChatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessage message) {
            binding.tvChat.setText(message.getMessage());
        }
    }

    static class StartingMenuAssistantViewHolder extends RecyclerView.ViewHolder {
        private final ItemDoctorChatMenuRowBinding binding;

        public StartingMenuAssistantViewHolder(@NonNull ItemDoctorChatMenuRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class DateSuggestionAssistantViewHolder extends RecyclerView.ViewHolder {
        private final ItemDoctorChatDateRowBinding binding;

        public DateSuggestionAssistantViewHolder(@NonNull ItemDoctorChatDateRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class SelectDateAssistantViewHolder extends RecyclerView.ViewHolder {
        private final ItemDoctorChatSelectDateRowBinding binding;

        public SelectDateAssistantViewHolder(@NonNull ItemDoctorChatSelectDateRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<ChatMessage> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull ChatMessage oldItem, @NonNull ChatMessage newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ChatMessage oldItem, @NonNull ChatMessage newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    private List<String> generateRandomFutureDates(int count) {
        List<String> randomDates = new ArrayList<>();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .appendLiteral('.')
                .appendValue(ChronoField.MICRO_OF_SECOND, 6)
                .toFormatter();

        LocalDateTime startDate = LocalDateTime.now().plusDays(1); // Start from tomorrow

        for (int i = 0; i < count; i++) {
            LocalDateTime futureDate = startDate.plusDays(i);
            String formattedDate = futureDate.format(formatter);
            randomDates.add(formattedDate);
        }

        return randomDates;
    }

    private String convertDateToDayMonth(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH);

        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
        return outputFormatter.format(dateTime);
    }

    public interface OnItemClickListener {
        void onRescheduleSuggestionClick(String command, int position);

        void onFirstDateSuggestionClick(String selectedDate, int position);

        void onSecondDateSuggestionClick(String selectedDate, int position);

        void onThirdDateSuggestionClick(String selectedDate, int position);

        void onSelectAnotherDateClick(String command, int position);
    }
}
