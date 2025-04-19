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
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.Objects;

public class PdfTestResultAdapter extends ListAdapter<TestsResultModel, PdfTestResultAdapter.ViewHolder> {
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
        TestsResultModel testResult = getItem(position);
        if (testResult != null) {
            String result = testResult.getResult();

            String[] parts = result.split("\\s+");

            String resultValue = parts[0];
            String resultExtension = parts.length > 1 ? parts[1] : "";

            holder.binding.tvResultName.setText(testResult.getName());
            holder.binding.tvResultValue.setText(resultValue);
            holder.binding.tvResultExtension.setText(resultExtension);
            holder.binding.tvResultStatus.setText(testResult.getRange());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTestResultPdfGridBinding binding;

        public ViewHolder(@NonNull ItemTestResultPdfGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<TestsResultModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestsResultModel oldStory,
                                               @NonNull TestsResultModel newStory) {
                    return oldStory.equals(newStory);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestsResultModel oldStory,
                                                  @NonNull TestsResultModel newStory) {
                    return Objects.equals(oldStory.getName(), newStory.getName());
                }
            };
}
