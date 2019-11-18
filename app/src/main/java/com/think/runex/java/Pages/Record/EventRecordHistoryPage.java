package com.think.runex.java.Pages.Record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.ActivityInfoBean;
import com.think.runex.java.Utils.ActivityUtils;

import java.util.List;

public class EventRecordHistoryPage extends xFragment {
    /**
     * Main variables
     */
    private final String ct = "EventRecordHistoryPage->";

    // instance variables
    private List<ActivityInfoBean> records;

    // explicit variables
    private RecordAdapter adapter;

    // views
    private RecyclerView recordList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        View v = inflater.inflate(R.layout.page_event_record_history, container, false);

        // need fullscreen
        ActivityUtils.newInstance(activity).fullScreen();

        // view matching
        viewMatching(v);
        setupRecyclerView();

        if( records != null ){
            adapter.submitList( records );

        }

        return v;
    }

    /** Setter */
    public EventRecordHistoryPage setRecordList(List<ActivityInfoBean> recordList){
        this.records = recordList;
        return this;
    }

    // view matching
    private void viewMatching(View v) {
        recordList = v.findViewById(R.id.record_list);
    }

    private void setupRecyclerView() {
        adapter = new RecordAdapter(false,null);
        recordList.setLayoutManager(new LinearLayoutManager(activity));
        recordList.setAdapter(adapter);
    }


}
