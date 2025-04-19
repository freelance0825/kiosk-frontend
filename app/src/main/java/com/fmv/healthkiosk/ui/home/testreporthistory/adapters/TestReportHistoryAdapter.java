package com.fmv.healthkiosk.ui.home.testreporthistory.adapters;

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
import com.fmv.healthkiosk.databinding.ItemMyAppointmentDoctorRowBinding;
import com.fmv.healthkiosk.databinding.ItemMyTestReportHistoryRowBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Objects;

public class TestReportHistoryAdapter extends ListAdapter<TestHistoryModel, TestReportHistoryAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TestReportHistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyTestReportHistoryRowBinding binding = ItemMyTestReportHistoryRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestHistoryModel testHistoryModel = getItem(position);
        if (testHistoryModel == null) {
            holder.binding.getRoot().setVisibility(View.INVISIBLE);
        } else {
            holder.binding.getRoot().setVisibility(View.VISIBLE);

            holder.binding.tvPackageName.setText(testHistoryModel.getPackageName());
            holder.binding.tvDateTime.setText(testHistoryModel.getCreatedAt());

//            DoctorModel doctor = appointment.getDoctor();
//            holder.binding.layoutButtonConsultationHistory.setVisibility(View.VISIBLE);
//
//            if (doctor.getImageBase64() == null) {
//                holder.binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.asset_image_height_placeholder));
//            } else {
//                holder.binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(doctor.getImageBase64()));
//            }
//
//            holder.binding.tvDoctorName.setText(doctor.getName());
//            holder.binding.tvDoctorOccupation.setText(doctor.getSpecialization());
//
//            // Use .equals() for string comparison to check if the status is "live"
//            if ("live".equals(doctor.getStatus())) {
//                holder.binding.tvLiveStatus.setVisibility(View.VISIBLE);
//            } else {
//                holder.binding.tvLiveStatus.setVisibility(View.INVISIBLE);
//            }
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm");
//            LocalDateTime ldt = LocalDateTime.parse(appointment.getDateTime(), formatter);
//
//            // Dynamically format the date/time using the formatDateTime method
//            String formattedDate = formatDateTime(ldt);
//            holder.binding.tvDateTime.setText(formattedDate);
//            holder.binding.tvDateTime.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));
//
//            // Handle button clicks for rebooking and viewing reports
//            holder.binding.btnBookAgain.setOnClickListener(v -> {
//                if (listener != null) listener.onBookAgainClick(appointment, position);
//            });

            holder.binding.btnViewReport.setOnClickListener(v -> {
                if (listener != null) listener.onViewReportClick(testHistoryModel, position);
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMyTestReportHistoryRowBinding binding;

        public ViewHolder(@NonNull ItemMyTestReportHistoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<TestHistoryModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestHistoryModel oldItem, @NonNull TestHistoryModel newItem) {
                    return Objects.equals(oldItem.getCreatedAt(), newItem.getCreatedAt());
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestHistoryModel oldItem, @NonNull TestHistoryModel newItem) {
                    return Objects.equals(oldItem.getCreatedAt(), newItem.getCreatedAt());
                }
            };

    public interface OnItemClickListener {
        void onViewReportClick(TestHistoryModel testHistoryModel, int position);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm");
        return formatter.format(dateTime);
    }

    // Method to check if the appointment is happening right now
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
