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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TodayNotificationAdapter extends ListAdapter<Notification, TodayNotificationAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TodayNotificationAdapter() {
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
//        Notification notification = getItem(position);
//        long dateTime = notification.getDateTime();
//
//        holder.binding.tvDateTimeAppointment.setText(formatToCustomDateTime(dateTime));
//
//        if (notification.isCancelled()) {
//            holder.binding.tvAppointmentTitle.setText("Your appointment has been cancelled");
//            holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getCancelledMessage(username, formatDateTime(dateTime), notification.getDoctor().getName(), notification.getDoctor().getSpecialization()));
//        } else if (notification.isRescheduled()) {
//            holder.binding.tvAppointmentTitle.setText("You have new notification time proposal");
//            // Currently no reschedule
//        } else if (isNow(dateTime)) {
//            holder.binding.tvAppointmentTitle.setText("Your appointment happening now");
//            holder.binding.tvAppointmentDesc.setText(NotificationMessageBuilder.getNowMessage(username, dateTime, notification.getDoctor().getName()));
//        }
//
//        holder.binding.layoutDoctorConfirmation.setVisibility(ViewGroup.VISIBLE);
//
//        holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
//        holder.binding.tvDoctorName.setText(notification.getDoctor().getName());
//        holder.binding.tvDoctorOccupation.setText(notification.getDoctor().getSpecialization());
//
//        holder.binding.tvDateTime.setText(formatDateTime(dateTime));
//        holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));
//
//        holder.binding.btnJoinNow.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onJoinNowClick(notification, position);
//            }
//        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemNotificationRowBinding binding;

        public ViewHolder(@NonNull ItemNotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    public interface OnItemClickListener {
        void onJoinNowClick(Notification doctor, int position);

        void onConfirmClick(Notification doctor, int position);
    }

    public static String formatToCustomDateTime(long dateTimeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy 'at' hh:mm a", Locale.getDefault());
        return sdf.format(new Date(dateTimeMillis));
    }

    public String formatDateTime(long dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(dateTime));
    }

    public boolean isNow(long dateTime) {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(dateTime);

        return now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR) &&
                now.get(Calendar.HOUR_OF_DAY) == target.get(Calendar.HOUR_OF_DAY);
    }
}
