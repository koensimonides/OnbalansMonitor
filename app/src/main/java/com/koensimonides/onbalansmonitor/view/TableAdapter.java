package com.koensimonides.onbalansmonitor.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.data.types.UnprocessedRecord;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private final UnprocessedRecord[] records;

    public TableAdapter(UnprocessedRecord[] records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int index) {
        if(records != null && index < records.length && records[index] != null)
            holder.setRecord(records[index]);
        holder.layout.setBackgroundColor(holder.layout.getResources().getColor(
                index % 2 == 1 ? R.color.general_white_highlight : R.color.general_white));
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView time;
        private final TextView upwardDispatch;
        private final TextView downwardDispatch;
        private final TextView reserveUpwardDispatch;
        private final TextView reserveDownwardDispatch;
        private final TextView emergencyPower;
        private final TextView priceHigh;
        private final TextView priceLow;

        private final LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.table_row_layout);

            time = itemView.findViewById(R.id.table_time);
            upwardDispatch = itemView.findViewById(R.id.table_upward_dispatch);
            downwardDispatch = itemView.findViewById(R.id.table_downward_dispatch);
            reserveUpwardDispatch = itemView.findViewById(R.id.table_reserve_upward_dispatch);
            reserveDownwardDispatch = itemView.findViewById(R.id.table_reserve_downward_dispatch);
            emergencyPower = itemView.findViewById(R.id.table_emergency_power);
            priceHigh = itemView.findViewById(R.id.table_price_high);
            priceLow = itemView.findViewById(R.id.table_price_low);
        }

        public void setRecord(UnprocessedRecord record) {
            time.setText(record.getFormattedTime());
            upwardDispatch.setText(String.valueOf(record.getUpwardDispatch()));
            downwardDispatch.setText(String.valueOf(record.getDownwardDispatch()));
            downwardDispatch.setText(String.valueOf(record.getDownwardDispatch()));
            reserveUpwardDispatch.setText(String.valueOf(record.getReserveUpwardDispatch()));
            reserveDownwardDispatch.setText(String.valueOf(record.getReserveDownwardDispatch()));
            emergencyPower.setText(record.isEmergencyPower() ? "1" : "0");
            priceHigh.setText(record.getMaxPrice() == null ? "" : String.valueOf(record.getMaxPrice()));
            priceLow.setText(record.getMinPrice() == null ? "" : String.valueOf(record.getMinPrice()));
        }

    }
}