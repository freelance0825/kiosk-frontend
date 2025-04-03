package com.fmv.healthkiosk.ui.home.test.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.databinding.ItemCustomizePackageGridBinding;
import com.fmv.healthkiosk.databinding.ItemPackageGridBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;

import java.util.Objects;

public class TestAdapter extends ListAdapter<TestItem, TestAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TestAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPackageGridBinding binding = ItemPackageGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestItem testItem = getItem(position);
        if (testItem != null) {
            if (testItem.isActive()) {
                holder.binding.getRoot().setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_rounded_child_card_selected));
            } else {
                holder.binding.getRoot().setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_rounded_child_card_inactive));
            }

            holder.binding.tvPackageName.setText(testItem.getName());
            holder.binding.ivPackage.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), testItem.getIcon()));

            holder.binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(testItem, position);
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemPackageGridBinding binding;

        public ViewHolder(@NonNull ItemPackageGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<TestItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestItem oldStory,
                                               @NonNull TestItem newStory) {
                    return oldStory.equals(newStory);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestItem oldStory,
                                                  @NonNull TestItem newStory) {
                    return Objects.equals(oldStory.getId(), newStory.getId());
                }
            };

    public interface OnItemClickListener {
        void onItemClick(TestItem testItem, int position);
    }
}
