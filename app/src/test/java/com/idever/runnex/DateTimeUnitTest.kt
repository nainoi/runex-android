package com.idever.runnex

import com.jozzee.android.core.convertor.toDate
import com.think.runex.common.toTimeStamp
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import org.junit.Test

class DateTimeUnitTest {

    @Test
    fun server_date_time_to_timestamp() {
        val serverTime = "2019-09-19T10:29:57Z"
        val timeStamp: Long = serverTime.toTimeStamp(SERVER_DATE_TIME_FORMAT)
        println("timeStamp: $timeStamp")
    }
}