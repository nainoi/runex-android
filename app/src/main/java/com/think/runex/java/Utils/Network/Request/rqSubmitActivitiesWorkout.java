package com.think.runex.java.Utils.Network.Request;

import com.think.runex.java.Models.EventIdAndPartnerObject;
import com.think.runex.java.Models.WorkoutInfo;

import java.util.List;

public class rqSubmitActivitiesWorkout {

    private List<EventIdAndPartnerObject> event_activity;
    private WorkoutInfo workout_info;

    public List<EventIdAndPartnerObject> getEvent_activity() {
        return event_activity;
    }

    public void setEvent_activity(List<EventIdAndPartnerObject> event_activity) {
        this.event_activity = event_activity;
    }

    public WorkoutInfo getWorkout_info() {
        return workout_info;
    }

    public void setWorkout_info(WorkoutInfo workout_info) {
        this.workout_info = workout_info;
    }

}
