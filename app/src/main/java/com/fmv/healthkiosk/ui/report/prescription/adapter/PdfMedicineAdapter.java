package com.fmv.healthkiosk.ui.report.prescription.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.databinding.ItemMedicinePdfRowBinding;
import com.fmv.healthkiosk.databinding.ItemTestResultPdfGridBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.MedicineModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.Objects;

public class PdfMedicineAdapter extends ListAdapter<MedicineModel, PdfMedicineAdapter.ViewHolder> {
    public PdfMedicineAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMedicinePdfRowBinding binding = ItemMedicinePdfRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicineModel medicine = getItem(position);
        if (medicine != null) {
            holder.binding.tvMedicineName.setText(position + 1 + ". " + medicine.getName());

            String morning = medicine.getMorning() != null ? medicine.getMorning() : "";
            String night = medicine.getNight() != null ? ", " + medicine.getNight() : "";
            String notes = medicine.getNotes() != null ? ", " + medicine.getNotes() : "";

            String infoBuilder = morning + night + notes;

            holder.binding.tvMedicineDosage.setText(infoBuilder);
            holder.binding.tvMedicineDuration.setText(medicine.getDuration());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMedicinePdfRowBinding binding;

        public ViewHolder(@NonNull ItemMedicinePdfRowBinding binding) {
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
