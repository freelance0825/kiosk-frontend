package com.fmv.healthkiosk.ui.home.landing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.databinding.ItemMenuColumnBinding;
import com.fmv.healthkiosk.ui.home.model.MenuItem;

public class MenuItemAdapter extends ListAdapter<MenuItem, MenuItemAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MenuItemAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuColumnBinding binding = ItemMenuColumnBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = getItem(position);
        if (menuItem == null) {
            holder.binding.getRoot().setVisibility(View.INVISIBLE);
        } else {
            holder.binding.getRoot().setVisibility(View.VISIBLE);
            holder.binding.tvMenuName.setText(menuItem.getName());
            holder.binding.ivMenu.setImageResource(menuItem.getLogo());

            holder.binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(menuItem, position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMenuColumnBinding binding;

        public ViewHolder(@NonNull ItemMenuColumnBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<MenuItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }
            };

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem, int position);
    }
}
