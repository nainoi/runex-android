package com.think.runex.java.Pages.Record;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Pages.ReviewEvent.EventCheckerAdapter;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.ui.components.listdecoration.ListItemDecoration;

public class ActivityRecordPage extends xActivity {
    /**
     * Main variables
     */
    private final String ct = "ActivityRecordPage->";

    // explicit variables
    private RecordAdapter adapter;

    // views
    private RecyclerView recordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_activity_record);

        ActivityUtils.newInstance(this).fullScreen();

        // view matching
        viewMatching();
        setupRecyclerView();

        //Get activity record list from [getExtras]
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            adapter.submitList(bundle.getParcelableArrayList("recodeList"));
        }

    }

    // view matching
    private void viewMatching() {
        recordList = findViewById(R.id.record_list);
    }

    private void setupRecyclerView() {
        adapter = new RecordAdapter();
        recordList.setLayoutManager(new LinearLayoutManager(this));
        recordList.setAdapter(adapter);
    }


}
