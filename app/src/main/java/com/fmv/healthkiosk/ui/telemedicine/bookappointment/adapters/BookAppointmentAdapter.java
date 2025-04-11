package com.fmv.healthkiosk.ui.telemedicine.bookappointment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.databinding.ItemBookAppointmentDoctorRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;

public class BookAppointmentAdapter extends ListAdapter<Doctor, BookAppointmentAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookAppointmentAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookAppointmentDoctorRowBinding binding = ItemBookAppointmentDoctorRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctor = getItem(position);

        holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
        holder.binding.tvDoctorName.setText(doctor.getName());
        holder.binding.tvDoctorOccupation.setText(doctor.getSpecialization());
        holder.binding.tvRatings.setText(String.valueOf(doctor.getReview()));

        // Use .equals() for string comparison to check if the status is "live"
        if ("live".equals(doctor.getStatus())) {
            holder.binding.tvLiveStatus.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvLiveStatus.setVisibility(View.INVISIBLE);
        }

        holder.binding.btnBookAppointment.setVisibility(View.VISIBLE);
        holder.binding.btnBookAppointment.setOnClickListener(v -> {
            if (listener != null) listener.onBookAppointmentClick(doctor, position);
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemBookAppointmentDoctorRowBinding binding;

        public ViewHolder(@NonNull ItemBookAppointmentDoctorRowBinding binding) {
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
        void onBookAppointmentClick(Doctor doctor, int position);
    }

}
