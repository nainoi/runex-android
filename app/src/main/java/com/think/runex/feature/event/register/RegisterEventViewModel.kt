package com.think.runex.feature.event.register

import androidx.lifecycle.MutableLiveData
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.Shirt
import com.think.runex.feature.event.data.Ticket
import com.think.runex.feature.event.detail.EventDetailsViewModel

class RegisterEventViewModel(repo: EventRepository) : EventDetailsViewModel(repo) {

    val changeChildScreen: MutableLiveData<String> by lazy { MutableLiveData() }

    var ticket: Ticket? = null
        private set

    var shirt: Shirt? = null
        private set


    fun onGetEventDetailCompleted() {
        changeChildScreen.postValue(ChooseTicketFragment::class.java.simpleName)
    }

    fun onSelectTicket(ticket: Ticket) {
        this.ticket = ticket
        changeChildScreen.postValue(FillOutUserInfoFragment::class.java.simpleName)
    }

    fun onSelectShirt(shirt: Shirt) {
        this.shirt = shirt
    }
}