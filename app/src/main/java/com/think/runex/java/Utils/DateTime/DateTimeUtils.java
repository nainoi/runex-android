package com.think.runex.java.Utils.DateTime;

import com.think.runex.java.App.Configs;
import com.think.runex.java.Utils.L;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtils {
    /**
     * Main variables
     */
    private static final String ct = "DateTimeUtils->";

    public static String toTimeFormat(long millisec) {
        // prepare usage variables
        final int allSec = (int) (millisec / 1000);
        final int sec = allSec % 60;
        final int min = (((allSec) / 60) % 60);
        final int hour = (allSec / 3600);

        return ((hour <= 0)
                ? "" : ((hour < 10 ? "0" + hour : hour + "")) + ":") +

                (min < 10 ? "0" + min : min + "") + ":" +
                (sec < 10 ? "0" + sec : sec);
    }

    public static DisplayDateTimeObject stringToDate(String strDate) {
        // prepare usage variables
        final String mtn = ct + "stringToDate() ";

        try {
            // prepare usage variables
            SimpleDateFormat df = new SimpleDateFormat(Configs.SERVER_DATE_TIME_FORMAT);

            // parse
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(df.parse(strDate).getTime());

            int date = c.get(Calendar.DATE);
            int month = c.get(Calendar.MONTH);
            String shortMonth = Months.TH.get(month);
            int year = c.get(Calendar.YEAR) + 543;

            DisplayDateTimeObject display = new DisplayDateTimeObject();
            display.Day = date;
            display.MonthPosition = month;
            display.shortMonth = shortMonth;
            display.year = year;
            display.timestamp = c.getTimeInMillis() / 1000;

            return display;

        } catch (Exception e) {
            L.e(mtn + "Err: " + e);

        }

        return null;
    }

}
