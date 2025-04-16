package com.fmv.healthkiosk.ui.telemedicine.consultnow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.ItemBookAppointmentDoctorRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;

public class ConsultNowAdapter extends ListAdapter<DoctorModel, ConsultNowAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ConsultNowAdapter() {
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
        DoctorModel doctor = getItem(position);
        if (doctor == null) {
            holder.binding.getRoot().setVisibility(View.INVISIBLE);
        } else {
            holder.binding.getRoot().setVisibility(View.VISIBLE);

            if (doctor.getImageBase64() == null) {
                holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
            } else {
                holder.binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(doctor.getImageBase64()));
            }

            holder.binding.tvDoctorName.setText(doctor.getName());
            holder.binding.tvDoctorOccupation.setText(doctor.getSpecialization());
            holder.binding.tvRatings.setText(String.valueOf(doctor.getReview()));

            // Use .equals() for string comparison to check if the status is "live"
            if ("live".equals(doctor.getStatus())) {
                holder.binding.tvLiveStatus.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvLiveStatus.setVisibility(View.INVISIBLE);
            }

            holder.binding.btnConsultNow.setVisibility(View.VISIBLE);
            holder.binding.btnConsultNow.setOnClickListener(v -> {
                if (listener != null) listener.onConsultNowClick(doctor, position);
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemBookAppointmentDoctorRowBinding binding;

        public ViewHolder(@NonNull ItemBookAppointmentDoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

//    @Override
//    public int getItemCount() {
//        return 3;
//    }

    private static final DiffUtil.ItemCallback<DoctorModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull DoctorModel oldItem, @NonNull DoctorModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull DoctorModel oldItem, @NonNull DoctorModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    public interface OnItemClickListener {
        void onConsultNowClick(DoctorModel doctor, int position);
    }
}
