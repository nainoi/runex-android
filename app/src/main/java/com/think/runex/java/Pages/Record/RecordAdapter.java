package com.think.runex.java.Pages.Record;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.ActivityInfoBean;
import com.think.runex.java.Models.RegEventsObject;
import com.think.runex.java.Pages.onItemClick;
import com.think.runex.java.Utils.L;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {
    /** Main variables */
    private final String ct = "RecordAdapter->";
    private List<ActivityInfoBean> list;
    private onItemClick onItemClickListener;
    private boolean editor;

    public RecordAdapter(boolean editor, onItemClick onItemClickListener) {
        super();

        this.onItemClickListener = onItemClickListener;
        this.editor = editor;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(list.get(position), editor, onItemClickListener);
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

                    if (Globals.SDF_TOKEN.parse(list.get(a).getActivity_date())
                            .before(Globals.SDF_TOKEN.parse(list.get( b ).getActivity_date())

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

    private void toCustomizeDate(int addHour){
        // prepare usage variables
        final String mtn = ct +"toCustomizeDate() ";
        final Calendar calendar = Calendar.getInstance();

        for(int a = 0; a < list.size(); a++){
            // prepare usage variables
            final ActivityInfoBean data = list.get( a );

            try {
                final Date d = Globals.SDF_TOKEN.parse(data.getActivity_date());

                // update props
                calendar.setTimeInMillis(d.getTime());
                calendar.add(Calendar.HOUR, addHour);

                // custom display date-time
                String displayDatetime = Globals.SDF_DISPLAY_FULL_DATE_TIME.format(calendar.getTime());

                // update props
                data.setCustom_display_date( displayDatetime );

            } catch ( Exception e ){
                L.e(mtn +"Err: "+ e.getMessage());
            }
        }
    }
    public List<ActivityInfoBean> getList(){
        return list;
    }
    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public ActivityInfoBean getItem( int position ){
        return list.get( position );
    }

    public void submitList(List<ActivityInfoBean> list) {
        this.submitList(list, 0);
    }
    public void submitList(List<ActivityInfoBean> list, int addHour) {
        this.list = list;
        toCustomizeDate(addHour);
        sortByDate();
        notifyDataSetChanged();
    }
}
