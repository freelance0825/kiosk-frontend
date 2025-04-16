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

import java.util.Objects;

public class TestResultAdapter extends ListAdapter<TestResult, TestResultAdapter.ViewHolder> {
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
        TestResult testResult = getItem(position);
        if (testResult == null) {
            holder.binding.getRoot().setVisibility(View.INVISIBLE);
        } else {
            holder.binding.getRoot().setVisibility(View.VISIBLE);
            holder.binding.tvResultName.setText(testResult.getName());
            holder.binding.tvResultValue.setText(String.valueOf(testResult.getValue()));
            holder.binding.tvResultExtension.setText(testResult.getExtension());
            holder.binding.tvResultStatus.setText(testResult.getStatus());
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

    private static final DiffUtil.ItemCallback<TestResult> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestResult oldItem,
                                               @NonNull TestResult newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestResult oldItem,
                                                  @NonNull TestResult newItem) {
                    return Objects.equals(oldItem.getName(), newItem.getName());
                }
            };
}
