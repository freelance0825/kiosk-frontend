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
import com.fmv.healthkiosk.databinding.ItemMenuColumnBinding;
import com.fmv.healthkiosk.databinding.ItemMyAppointmentDoctorRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.ui.home.model.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAppointmentAdapter extends ListAdapter<Doctor, MyAppointmentAdapter.ViewHolder> {
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
        Doctor doctor = getItem(position);

        // Flag for My Appointment Row
        holder.binding.layoutButtonMyAppointment.setVisibility(View.VISIBLE);

        holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
        holder.binding.tvDoctorName.setText(doctor.getName());
        holder.binding.tvDoctorOccupation.setText(doctor.getSpecialization());

        if (doctor.isLive()) {
            holder.binding.tvLiveStatus.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvLiveStatus.setVisibility(View.INVISIBLE);
        }

        long dateTime = doctor.getDateTime();

        if (isNow(dateTime)) {
            holder.binding.tvDateTime.setText("HAPPEN NOW");
            holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.primaryBlue));

            holder.binding.btnConsultNow.setVisibility(View.VISIBLE);
            holder.binding.btnReschedule.setVisibility(View.GONE);
            holder.binding.btnCancel.setVisibility(View.GONE);

        } else {
            holder.binding.tvDateTime.setText(formatDateTime(dateTime));
            holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));

            holder.binding.btnConsultNow.setVisibility(View.GONE);
            holder.binding.btnReschedule.setVisibility(View.VISIBLE);
            holder.binding.btnCancel.setVisibility(View.VISIBLE);
        }


        holder.binding.btnConsultNow.setOnClickListener(v -> {
            if (listener != null) listener.onConsultNowClick(doctor, position);
        });

        holder.binding.btnReschedule.setOnClickListener(v -> {
            if (listener != null) listener.onConsultRescheduleClick(doctor, position);
        });

        holder.binding.btnCancel.setOnClickListener(v -> {
            if (listener != null) listener.onConsultCancelClick(doctor, position);
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMyAppointmentDoctorRowBinding binding;

        public ViewHolder(@NonNull ItemMyAppointmentDoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<Doctor> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Doctor oldItem, @NonNull Doctor newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Doctor oldItem, @NonNull Doctor newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    public interface OnItemClickListener {
        void onConsultNowClick(Doctor doctor, int position);

        void onConsultRescheduleClick(Doctor doctor, int position);

        void onConsultCancelClick(Doctor doctor, int position);
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
