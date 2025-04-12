package com.fmv.healthkiosk.ui.telemedicine.postconsultation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.ItemMedicinePdfRowBinding;
import com.fmv.healthkiosk.databinding.ItemMedicinePostConsultationGridBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.MedicineModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.Objects;

public class PostConsultationMedicineAdapter extends ListAdapter<MedicineModel, PostConsultationMedicineAdapter.ViewHolder> {
    public PostConsultationMedicineAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMedicinePostConsultationGridBinding binding = ItemMedicinePostConsultationGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicineModel medicine = getItem(position);
        if (medicine != null) {
            if (!medicine.getImageBase64().isEmpty()) {
                holder.binding.ivMedicine.setImageBitmap(Base64Helper.convertToBitmap(medicine.getImageBase64()));
            }

            holder.binding.tvMedicineName.setText(medicine.getName());

            String morning = medicine.getMorning() != null ? medicine.getMorning() : "";
            String night = medicine.getNight() != null ? ", " + medicine.getNight() : "";
            String notes = medicine.getNotes() != null ? medicine.getNotes() : "";

            String infoBuilder = morning + night + " (" + notes + ")";
            holder.binding.tvMedicineInfo.setText(infoBuilder);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMedicinePostConsultationGridBinding binding;

        public ViewHolder(@NonNull ItemMedicinePostConsultationGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<MedicineModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull MedicineModel oldStory,
                                               @NonNull MedicineModel newStory) {
                    return oldStory.equals(newStory);
                }

                @Override
                public boolean areContentsTheSame(@NonNull MedicineModel oldStory,
                                                  @NonNull MedicineModel newStory) {
                    return Objects.equals(oldStory.getName(), newStory.getName());
                }
            };
}
