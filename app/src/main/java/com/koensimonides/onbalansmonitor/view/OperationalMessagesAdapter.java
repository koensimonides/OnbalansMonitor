package com.koensimonides.onbalansmonitor.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.fragment.MessagesFragment;
import com.koensimonides.onbalansmonitor.data.types.OperationalMessage;

public class OperationalMessagesAdapter extends RecyclerView.Adapter<OperationalMessagesAdapter.ViewHolder> {

    private final MessagesFragment fragment;

    private final OperationalMessage[] messages;

    public OperationalMessagesAdapter(MessagesFragment fragment, OperationalMessage[] messages) {
        this.fragment = fragment;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        holder.setMessage(messages[index]);

        holder.layout.setOnClickListener(view -> fragment.openMessage(holder.getMessage()));
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView date;

        private final LinearLayout layout;

        private OperationalMessage message;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.messages_list_item_title);
            date = itemView.findViewById(R.id.messages_list_item_date);

            layout = itemView.findViewById(R.id.messages_list_item_layout);
        }

        public void setMessage(OperationalMessage message) {
            this.message = message;

            title.setText(message.getTitle());
            date.setText(message.getPublicationDate());
        }

        public OperationalMessage getMessage() {
            return message;
        }

    }
}