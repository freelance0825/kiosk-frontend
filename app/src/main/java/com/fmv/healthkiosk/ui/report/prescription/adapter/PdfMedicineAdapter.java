package com.fmv.healthkiosk.ui.report.prescription.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.databinding.ItemMedicinePdfRowBinding;
import com.fmv.healthkiosk.databinding.ItemTestResultPdfGridBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.Objects;

public class PdfMedicineAdapter extends ListAdapter<TestResult, PdfMedicineAdapter.ViewHolder> {
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
        TestResult medicine = getItem(position);
        if (medicine != null) {
            holder.binding.tvMedicineName.setText(position + 1 + ". a");
            holder.binding.tvMedicineDosage.setText("a");
            holder.binding.tvMedicineDuration.setText("a");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMedicinePdfRowBinding binding;

        public ViewHolder(@NonNull ItemMedicinePdfRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<TestResult> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestResult oldStory,
                                               @NonNull TestResult newStory) {
                    return oldStory.equals(newStory);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestResult oldStory,
                                                  @NonNull TestResult newStory) {
                    return Objects.equals(oldStory.getName(), newStory.getName());
                }
            };
}
