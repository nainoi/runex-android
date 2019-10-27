package com.think.runex.java.Pages.RegisteredEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.think.runex.R;
import com.think.runex.java.Customize.xFragment;
import com.think.runex.java.Models.MultiObject;
import com.think.runex.java.ViewHolders.VHEvent;

import java.util.ArrayList;
import java.util.List;

public class RegisteredEventsPage extends xFragment {
    /** Main variables */
    private final String ct = "RegisteredEventsPage->";

    // instance variables
    private RecyclerView.Adapter<VHEvent> eventAdapter;
    private List<MultiObject> events = new ArrayList<MultiObject>(){{
        add( new MultiObject());
        add( new MultiObject());
        add( new MultiObject());
        add( new MultiObject());
        add( new MultiObject());
    }};

    // views
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_registered_events, container, false);

        // init
        eventAdapter = new EventAdapter( events );

        // matching view
        recyclerView = v.findViewById(R.id.recycler_view);
        refreshLayout = v.findViewById(R.id.refresh_layout);

        // recycler view props
        recyclerViewProps();


        return v;
    }

    /** Recycler view props */
    private void recyclerViewProps(){
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ));
        recyclerView.setAdapter( eventAdapter );
    }
}
