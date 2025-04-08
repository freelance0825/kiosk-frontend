package com.fmv.healthkiosk.ui.telemedicine.notification.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.databinding.ItemMyAppointmentDoctorRowBinding;
import com.fmv.healthkiosk.databinding.ItemNotificationRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AllNotificationAdapter extends ListAdapter<Appointment, AllNotificationAdapter.ViewHolder> {
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
        Appointment appointment = getItem(position);
        long dateTime = appointment.getDateTime();

        holder.binding.tvDateTimeAppointment.setText(formatToCustomDateTime(dateTime));

        if (appointment.isCancelled()) {
            holder.binding.tvAppointmentTitle.setText("Your appointment has been cancelled");
            holder.binding.tvAppointmentDesc.setText(AppointmentMessageBuilder.getCancelledMessage(username, dateTime, appointment.getDoctor().getName(), appointment.getDoctor().getSpecialization()));
        } else if (appointment.isRescheduled()) {
            holder.binding.tvAppointmentTitle.setText("You have new appointment time proposal");
            // Currently no reschedule
        } else if (isNow(dateTime)) {
            holder.binding.tvAppointmentTitle.setText("Your appointment happening now");
            holder.binding.tvAppointmentDesc.setText(AppointmentMessageBuilder.getNowMessage(username, dateTime, appointment.getDoctor().getName()));
        }

        holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
        holder.binding.tvDoctorName.setText(appointment.getDoctor().getName());
        holder.binding.tvDoctorOccupation.setText(appointment.getDoctor().getSpecialization());

        holder.binding.tvDateTime.setText(formatDateTime(dateTime));
        holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemNotificationRowBinding binding;

        public ViewHolder(@NonNull ItemNotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<Appointment> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    public interface OnItemClickListener {
        void onJoinNowClick(Appointment doctor, int position);

        void onConfirmClick(Appointment doctor, int position);
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
