package com.fmv.healthkiosk.ui.report.testing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.databinding.ItemTestResultColumnBinding;
import com.fmv.healthkiosk.databinding.ItemTestResultPdfGridBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.Objects;

public class PdfTestResultAdapter extends ListAdapter<TestResult, PdfTestResultAdapter.ViewHolder> {
    public PdfTestResultAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTestResultPdfGridBinding binding = ItemTestResultPdfGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestResult testResult = getItem(position);
        if (testResult != null) {
            holder.binding.tvResultName.setText(testResult.getName());
            holder.binding.tvResultValue.setText(String.valueOf(testResult.getValue()));
            holder.binding.tvResultExtension.setText(testResult.getExtension());
            holder.binding.tvResultStatus.setText(testResult.getStatus());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTestResultPdfGridBinding binding;

        public ViewHolder(@NonNull ItemTestResultPdfGridBinding binding) {
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
