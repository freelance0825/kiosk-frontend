package com.fmv.healthkiosk.ui.home.medicalpackage.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.ItemPackageGridBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;

import java.util.Objects;

public class MedicalPackageAdapter extends ListAdapter<MedicalPackage, MedicalPackageAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public MedicalPackageAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        MedicalPackage medicalPackage = getItem(position);
        if (medicalPackage != null) {
            holder.binding.tvPackageName.setText(medicalPackage.getName());
            holder.binding.ivPackage.setImageBitmap(Base64Helper.convertToBitmap(medicalPackage.getIcon()));
            holder.binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(medicalPackage);
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

    private static final DiffUtil.ItemCallback<MedicalPackage> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull MedicalPackage oldStory,
                                               @NonNull MedicalPackage newStory) {
                    return oldStory.equals(newStory);
                }

                @Override
                public boolean areContentsTheSame(@NonNull MedicalPackage oldStory,
                                                  @NonNull MedicalPackage newStory) {
                    return Objects.equals(oldStory.getId(), newStory.getId());
                }
            };

    public interface OnItemClickListener {
        void onItemClick(MedicalPackage medicalPackage);
    }
}
