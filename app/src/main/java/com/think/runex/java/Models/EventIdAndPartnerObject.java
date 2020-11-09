package com.think.runex.java.Models;

import com.think.runex.feature.event.model.Partner;

public class EventIdAndPartnerObject {
    private String event_id;
    private Partner partner;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
