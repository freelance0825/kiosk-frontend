package com.fmv.healthkiosk.ui.home.testreport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.databinding.ItemPackageGridBinding;
import com.fmv.healthkiosk.databinding.ItemTestResultColumnBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.Objects;

public class TestResultAdapter extends ListAdapter<TestsResultModel, TestResultAdapter.ViewHolder> {
    public TestResultAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTestResultColumnBinding binding = ItemTestResultColumnBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestsResultModel testResult = getItem(position);
        if (testResult == null) {
            holder.binding.getRoot().setVisibility(View.INVISIBLE);
        } else {
            String result = testResult.getResult();

            String[] parts = result.split("\\s+");

            String resultValue = parts[0];
            String resultExtension = parts.length > 1 ? parts[1] : "";

            holder.binding.getRoot().setVisibility(View.VISIBLE);
            holder.binding.tvResultName.setText(testResult.getName());
            holder.binding.tvResultValue.setText(resultValue);
            holder.binding.tvResultExtension.setText(resultExtension);
            holder.binding.tvResultStatus.setText(testResult.getRange());
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTestResultColumnBinding binding;

        public ViewHolder(@NonNull ItemTestResultColumnBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<TestsResultModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestsResultModel oldItem,
                                               @NonNull TestsResultModel newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestsResultModel oldItem,
                                                  @NonNull TestsResultModel newItem) {
                    return Objects.equals(oldItem.getName(), newItem.getName());
                }
            };
}
