package com.fmv.healthkiosk.ui.telemedicine.reschedule.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fmv.healthkiosk.R;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<String> timeList;
    private int selectedPosition = -1;
    private final Context context;
    private final OnTimeSlotSelectedListener listener;

    public interface OnTimeSlotSelectedListener {
        void onTimeSlotSelected(String time);
    }

    public TimeSlotAdapter(Context context, List<String> timeList, OnTimeSlotSelectedListener listener) {
        this.context = context;
        this.timeList = timeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot_grid, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String time = timeList.get(position);
        holder.timeText.setText(time);

        if (position == selectedPosition) {
            holder.cardView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_appointment_down_gradient));
            holder.timeText.setTextColor(Color.BLACK);
        } else {
            holder.cardView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_white_border));
            holder.timeText.setTextColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            listener.onTimeSlotSelected(time);
        });
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        LinearLayout cardView;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.time_layout);
            timeText = itemView.findViewById(R.id.tvTime);
        }
    }
}
