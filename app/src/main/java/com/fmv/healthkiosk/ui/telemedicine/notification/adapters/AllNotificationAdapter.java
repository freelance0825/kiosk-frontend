package com.fmv.healthkiosk.ui.telemedicine.notification.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.databinding.ItemNotificationRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.NotificationModel;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.util.Locale;

public class AllNotificationAdapter extends ListAdapter<NotificationModel, AllNotificationAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AllNotificationAdapter() {
        super(DIFF_CALLBACK);
    }

    private String username = "";

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationRowBinding binding = ItemNotificationRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = getItem(position);
        holder.binding.tvDateTimeAppointment.setText(formatDate(notification.getCreateAt()));
        // Use OffsetDateTime instead of LocalDateTime to handle 'Z' (UTC)
        try {
            OffsetDateTime apptDateTime = OffsetDateTime.parse(notification.getApptDateTime());
            if (notification.isCancelled()) {
                holder.binding.tvAppointmentTitle.setText("Your appointment has been cancelled");
                holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getCancelledMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName(), notification.getApptDoctorSpecialization()));
            } else if (notification.isRescheduled()) {
                holder.binding.tvAppointmentTitle.setText("You have new notification time proposal");
                holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getRescheduledMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName()));
            } else if (notification.isBooked()) {
                holder.binding.tvAppointmentTitle.setText("You have booked a new appointment");
                holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getBookedMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName()));
            } else if (isNow(apptDateTime)) {
                holder.binding.tvAppointmentTitle.setText("Your appointment happening now");
                holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getNowMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName()));
            }
        } catch (DateTimeParseException e) {
            // Log and handle the parsing error
            Log.e("DateTimeParse", "Unable to parse appointment date: " + notification.getApptDateTime(), e);
        }

        holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
        holder.binding.tvDoctorName.setText(notification.getApptDoctorName());
        holder.binding.tvDoctorOccupation.setText(notification.getApptDoctorSpecialization());

        holder.binding.tvDateTime.setText(formatDate(notification.getApptDateTime()));
        holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemNotificationRowBinding binding;

        public ViewHolder(@NonNull ItemNotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<NotificationModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull NotificationModel oldItem, @NonNull NotificationModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull NotificationModel oldItem, @NonNull NotificationModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    public interface OnItemClickListener {
        void onJoinNowClick(NotificationModel doctor, int position);

        void onConfirmClick(NotificationModel doctor, int position);
    }

    public String formatDate(String inputDate) {
        if (inputDate == null || inputDate.isEmpty()) return inputDate;

        inputDate = inputDate.trim();

        // Desired output format: "20/4/2025 at 02:30"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy 'at' HH:mm", Locale.ENGLISH);

        // Try parsing OffsetDateTime (ISO 8601 format with 'Z' or offset)
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(inputDate); // Parses "2025-04-20T02:30:00Z"
            return offsetDateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // Log and return original string if parsing fails
            Log.e("DateFormat", "Unable to parse date: " + inputDate, e);
            return inputDate;
        }
    }

    public boolean isNow(OffsetDateTime targetTime) {
        OffsetDateTime now = OffsetDateTime.now();

        return now.getYear() == targetTime.getYear() &&
                now.getDayOfYear() == targetTime.getDayOfYear() &&
                now.getHour() == targetTime.getHour();
    }
}
