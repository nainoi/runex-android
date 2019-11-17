package com.think.runex.java.Pages.Record;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.ActivityInfoBean;
import com.think.runex.java.Models.RegEventsObject;
import com.think.runex.java.Utils.L;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {
    /** Main variables */
    private final String ct = "RecordAdapter->";
    private List<ActivityInfoBean> list;

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    /** Feature methods */
    private void sortByDate(){
        // prepare usage variables
        final String mtn = ct +"sortByDate() ";

        for( int a = 0; a < list.size(); a++){
            // prepare usage variables
            final ActivityInfoBean item = list.get( a );

            try {

                for (int b = a; b < list.size(); b++) {

                    if (Globals.SDF_ONLY_DATE.parse(list.get(a).getActivity_date())
                            .before(Globals.SDF_ONLY_DATE.parse(list.get( b ).getActivity_date())

                    )) {

                        // swapping
                        ActivityInfoBean obj = list.get( a );
                        list.set(a, list.get( b ));
                        list.set(b, obj);

                    }
                }

            } catch ( Exception e ){
                L.e(mtn +"Err: "+ e.getMessage());

            }
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public void submitList(List<ActivityInfoBean> list) {
        this.list = list;
        sortByDate();
        notifyDataSetChanged();
    }
}
