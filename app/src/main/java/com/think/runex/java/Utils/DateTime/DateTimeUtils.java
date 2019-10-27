package com.think.runex.java.Utils.DateTime;

import com.think.runex.java.App.Configs;
import com.think.runex.java.Utils.L;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtils {
    /** Main variables */
    private final String ct = "DateTimeUtils->";
    private static DateTimeUtils ins;

    private DateTimeUtils(){}
    public static DateTimeUtils instance(){
        return ( ins == null ) ? ins = new DateTimeUtils() : ins;
    }


    public DisplayDateTimeObject stringToDate(String strDate ){
        // prepare usage variables
        final String mtn = ct +"stringToDate() ";

        try{
            // prepare usage variables
            SimpleDateFormat df = new SimpleDateFormat(Configs.SERVER_DATE_TIME_FORMAT);

            // parse
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis( df.parse( strDate ).getTime() );

            int date = c.get(Calendar.DATE);
            int month = c.get(Calendar.MONTH);
            String shortMonth = Months.TH.get( month );
            int year = c.get(Calendar.YEAR) + 543;

            DisplayDateTimeObject display = new DisplayDateTimeObject();
            display.Day = date;
            display.MonthPosition = month;
            display.shortMonth = shortMonth;
            display.year = year;

            return display;

        }catch ( Exception e ){
            L.e(mtn +"Err: "+ e);

        }

        return null;
    }

}
