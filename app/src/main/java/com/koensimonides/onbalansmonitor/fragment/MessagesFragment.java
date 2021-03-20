package com.koensimonides.onbalansmonitor.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.data.types.OperationalMessage;
import com.koensimonides.onbalansmonitor.view.OperationalMessagesAdapter;

public class MessagesFragment extends ActivityFragment {

    private RecyclerView recyclerView;

    public MessagesFragment() {
        super(R.string.messages_fragment_title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = requireView().findViewById(R.id.messages_message_picker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        onUpdate();
    }

    @Override
    public void onUpdate() {
        OperationalMessage[] messages = dataManager.getOperationalMessages().asArray();
        recyclerView.setAdapter(new OperationalMessagesAdapter(this, messages));
    }

    public void openMessage(@NonNull OperationalMessage message) {
        startActivity(new Intent("android.intent.action.VIEW",
                Uri.parse(message.getLink())));
    }
}
