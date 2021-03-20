package com.koensimonides.onbalansmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.data.types.UnprocessedRecord;
import com.koensimonides.onbalansmonitor.view.TableAdapter;

import java.util.List;

public class TableFragment extends ActivityFragment {

    private RecyclerView recyclerView;

    public TableFragment() {
        super(R.string.table_fragment_title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = requireView().findViewById(R.id.table_row_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        onUpdate();
    }

    @Override
    public void onUpdate() {
        final List<UnprocessedRecord> values = dataManager.getLastLoad();
        UnprocessedRecord[] array = new UnprocessedRecord[0];
        recyclerView.setAdapter(new TableAdapter(values == null ? array : values.toArray(array)));
    }
}
