package com.think.runex.java.Pages.ReviewEvent;

import com.think.runex.java.Models.EventIdAndPartnerObject;

import java.util.List;

public interface OnConfirmEventsListener {
    public void onConfirmEvents(List<EventIdAndPartnerObject> selectedEvents);
}
