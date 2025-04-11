package com.fmv.healthkiosk.ui.telemedicine.myappointment.adapters;

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
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class MyAppointmentAdapter extends ListAdapter<Appointment, MyAppointmentAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MyAppointmentAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyAppointmentDoctorRowBinding binding = ItemMyAppointmentDoctorRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = getItem(position);
        Doctor doctor = appointment.getDoctor(); // This is how you get the Doctor

        // Flag for My Appointment Row
        holder.binding.layoutButtonMyAppointment.setVisibility(View.VISIBLE);

        holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
        holder.binding.tvDoctorName.setText(doctor.getName());
        holder.binding.tvDoctorOccupation.setText(doctor.getSpecialization());

        if ("live".equals(doctor.getStatus())) {
            holder.binding.tvLiveStatus.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvLiveStatus.setVisibility(View.INVISIBLE);
        }

        holder.binding.btnConsultNow.setVisibility(View.VISIBLE);
        holder.binding.btnConsultNow.setOnClickListener(v -> {
            if (listener != null) listener.onConsultNowClick(appointment, position);
        });

        Long dateTime = appointment.getDateTime();

        // Use the "isNow" method to determine if the appointment is happening now
        if (isNow(dateTime)) {
            holder.binding.tvDateTime.setText("HAPPEN NOW");
            holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.primaryBlue));

            holder.binding.btnConsultNow.setVisibility(View.VISIBLE);
            holder.binding.btnReschedule.setVisibility(View.GONE);
            holder.binding.btnCancel.setVisibility(View.GONE);

        } else {
            // Format the date/time dynamically every time it binds
            String formattedDate = formatDateTime(dateTime);
            holder.binding.tvDateTime.setText(formattedDate);
            holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));

            holder.binding.btnConsultNow.setVisibility(View.GONE);
            holder.binding.btnReschedule.setVisibility(View.VISIBLE);
            holder.binding.btnCancel.setVisibility(View.VISIBLE);
        }

        // Button listeners
        holder.binding.btnConsultNow.setOnClickListener(v -> {
            if (listener != null) listener.onConsultNowClick(appointment, position);
        });

        holder.binding.btnReschedule.setOnClickListener(v -> {
            if (listener != null) listener.onConsultRescheduleClick(appointment, position);
        });

        holder.binding.btnCancel.setOnClickListener(v -> {
            if (listener != null) listener.onConsultCancelClick(appointment, position);
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMyAppointmentDoctorRowBinding binding;

        public ViewHolder(@NonNull ItemMyAppointmentDoctorRowBinding binding) {
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
        void onConsultNowClick(Appointment appointment, int position);

        void onConsultRescheduleClick(Appointment appointment, int position);

        void onConsultCancelClick(Appointment appointment, int position);
    }

    public String formatDateTime(long timestampMillis) {
        // Convert long to LocalDateTime
        LocalDateTime dateTime = Instant.ofEpochMilli(timestampMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Format to desired output: "10 December 2024, 12:00"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm");
        return formatter.format(dateTime);
    }

    public boolean isNow(long timestampMillis) {
        LocalDateTime targetTime = Instant.ofEpochMilli(timestampMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime now = LocalDateTime.now();

        return now.getYear() == targetTime.getYear() &&
                now.getDayOfYear() == targetTime.getDayOfYear() &&
                now.getHour() == targetTime.getHour();
    }
}
