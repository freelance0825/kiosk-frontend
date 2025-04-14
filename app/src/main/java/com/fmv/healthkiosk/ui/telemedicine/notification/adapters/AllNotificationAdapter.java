package com.fmv.healthkiosk.ui.telemedicine.notification.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.databinding.ItemNotificationRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.NotificationModel;

import org.threeten.bp.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        holder.binding.tvDateTimeAppointment.setText(formatDate(notification.getApptDateTime()));

        LocalDateTime ldt = LocalDateTime.parse(notification.getApptDateTime());

        if (notification.isCancelled()) {
            holder.binding.tvAppointmentTitle.setText("Your appointment has been cancelled");
            holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getCancelledMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName(), notification.getApptDoctorSpecialization()));
        } else if (notification.isRescheduled()) {
            holder.binding.tvAppointmentTitle.setText("You have new notification time proposal");
            holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getRescheduledMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName()));
        } else if (notification.isBooked()) {
            holder.binding.tvAppointmentTitle.setText("You have booked a new appointment");
            holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getBookedMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName()));
        } else if (isNow(ldt)) {
            holder.binding.tvAppointmentTitle.setText("Your appointment happening now");
            holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getNowMessage(username, formatDate(notification.getApptDateTime()), notification.getApptDoctorName()));
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
        try {
            // Replace 'T' with a space so SimpleDateFormat can parse it
            inputDate = inputDate.replace("T", " ");

            // Define the formats, one with milliseconds and one without
            SimpleDateFormat inputFormatWithMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.ENGLISH);
            SimpleDateFormat inputFormatWithoutMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date = null;
            try {
                // Try parsing with milliseconds
                date = inputFormatWithMillis.parse(inputDate);
            } catch (ParseException e) {
                // Fallback to parsing without milliseconds if the first format fails
                date = inputFormatWithoutMillis.parse(inputDate);
            }

            // Format the date in the desired output format (for example: "dd MMMM yyyy HH:mm:ss")
            SimpleDateFormat outputFormat = new SimpleDateFormat("d/M/yyyy 'at' hh:mm a", Locale.ENGLISH);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Return null if parsing fails
        }
    }


    public boolean isNow(LocalDateTime targetTime) {
        LocalDateTime now = LocalDateTime.now();

        return now.getYear() == targetTime.getYear() &&
                now.getDayOfYear() == targetTime.getDayOfYear() &&
                now.getHour() == targetTime.getHour();
    }
}
