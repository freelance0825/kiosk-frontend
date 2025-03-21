package com.fmv.healthkiosk.ui.home.customizetest.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.ItemPackageGridBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;

import java.util.Objects;

public class PackageTestAdapter extends ListAdapter<TestItem, PackageTestAdapter.ViewHolder> {
    public PackageTestAdapter() {
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
            holder.binding.tvPackageName.setText(testItem.getName());
            holder.binding.ivPackage.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), testItem.getIcon()));
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
}
