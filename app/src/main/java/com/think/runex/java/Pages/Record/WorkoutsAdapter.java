package com.think.runex.java.Pages.Record;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.WorkoutInfo;
import com.think.runex.java.Pages.OnItemClickListener;
import com.think.runex.java.Utils.L;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkoutsAdapter extends RecyclerView.Adapter<RecordViewHolder> {
    /**
     * Main variables
     */
    private final String ct = "RecordAdapter->";
    private List<WorkoutInfo> list = new ArrayList<>();
    private boolean canDelete = false;

    private OnItemClickListener onItemClickListener;
    private OnDeleteRecordListener onDeleteRecord;

    public WorkoutsAdapter(OnItemClickListener onItemClickListener,
                           boolean canDelete,
                           OnDeleteRecordListener onDeleteRecord) {
        super();

        this.onItemClickListener = onItemClickListener;
        this.canDelete = canDelete;
        this.onDeleteRecord = onDeleteRecord;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(list.get(position), onItemClickListener, false, onDeleteRecord);
    }

    /**
     * Feature methods
     */
    private void sortByDate() {
        // prepare usage variables
        final String mtn = ct + "sortByDate() ";

        for (int a = 0; a < list.size(); a++) {
            // prepare usage variables
            final WorkoutInfo item = list.get(a);

            try {

                for (int b = a; b < list.size(); b++) {

                    if (Globals.SDF_TOKEN.parse(list.get(a).getWorkout_date())
                            .before(Globals.SDF_TOKEN.parse(list.get(b).getWorkout_date())

                            )) {

                        // swapping
                        WorkoutInfo obj = list.get(a);
                        list.set(a, list.get(b));
                        list.set(b, obj);

                    }
                }

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());

            }
        }
    }

    private void toCustomizeDate(int addHour) {
        // prepare usage variables
        final String mtn = ct + "toCustomizeDate() ";
        final Calendar calendar = Calendar.getInstance();

        for (int a = 0; a < list.size(); a++) {
            // prepare usage variables
            final WorkoutInfo data = list.get(a);

            try {
                final Date d = Globals.SDF_TOKEN.parse(data.getWorkout_date());

                // update props
                calendar.setTimeInMillis(d.getTime());
                calendar.add(Calendar.HOUR, addHour);

                // custom display date-time
                String displayDatetime = Globals.SDF_DISPLAY_FULL_DATE_TIME.format(calendar.getTime());

                // update props
                //data.setCustom_display_date( displayDatetime );

            } catch (Exception e) {
                L.e(mtn + "Err: " + e.getMessage());
            }
        }
    }

    public List<WorkoutInfo> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public WorkoutInfo getItem(int position) {
        return list.get(position);
    }

    public void submitList(List<WorkoutInfo> list) {
        this.submitList(list, 0);
    }

    public void submitList(List<WorkoutInfo> list, int addHour) {
        this.list = list;
        toCustomizeDate(addHour);
        sortByDate();
        notifyDataSetChanged();

    }

    public interface OnDeleteRecordListener {
        void onDeleteRecord(int position);
    }
}
