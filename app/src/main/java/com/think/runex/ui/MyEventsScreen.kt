package com.think.runex.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.think.runex.R
import com.think.runex.mockup.LastedEvents
import kotlinx.android.synthetic.main.screen_my_events.*

class MyEventsScreen : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_my_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events_list.layoutManager = LinearLayoutManager(context)
        //events_list.adapter = EventsAdapter(mockUpEventList())
    }

    private fun mockUpEventList(): List<LastedEvents> {
        return listOf<LastedEvents>(
                LastedEvents(imageUrl = "https://www.hongkongfp.com/wp-content/uploads/2015/06/32aed4c7ab1842068b10872cf80c52f2.jpg",
                        title = "ASCOTT VIRTUAL RUN 2019",
                        description = "Rush hour, London. a packed commuter train is tron apart in collision. Picking trough the carnage",
                        time = "12:44:22",
                        price = 0f),
                LastedEvents(imageUrl = "https://storage.googleapis.com/s.race.thai.run/files/86251ee5-27df-401e-9d52-9c8fde09abc8.jpeg",
                        title = "KU RUN วิ่งลั่นทุ่ง",
                        description = "Put forward (someone or something) with approval as being suitable for a particular purpose of role.",
                        time = "12:44:22",
                        price = 2.99f),
                LastedEvents(imageUrl = "https://www.runningconnect.com/uploads/images/event/THDR2016/logo.png",
                        title = "THAI Health - We Shall Run",
                        description = "Put forward (someone or something) with approval as being suitable for a particular purpose of role.",
                        time = "12:44:22",
                        price = 0f),
                LastedEvents(imageUrl = "https://talk.mthai.com/storage/uploads/2017/09/21/9255af75c7cfe41d9a99088da000a045.jpeg",
                        title = "Bangkok Women's Run-2019",
                        description = "Put forward (someone or something) with approval as being suitable for a particular purpose of role.",
                        time = "12:44:22",
                        price = 2.99f)
        )
    }
}
