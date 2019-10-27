package com.think.runex.java.Utils.DateTime;

import com.think.runex.java.Utils.L;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateTimeUtils {
    /** Main variables */
    private final String ct = "DateTimeUtils->";
    private static DateTimeUtils ins;

    private List<String> MONTHS = new ArrayList<String>(){{

    }};
    private DateTimeUtils(){}
    public static DateTimeUtils instance(){
        return ( ins == null ) ? ins = new DateTimeUtils() : ins;
    }


    public Date stringToDate(String strDate ){
        // prepare usage variables
        final String mtn = ct +"stringToDate() ";

        try{

        }catch ( Exception e ){
            L.e(mtn +"Err: "+ e);

        }

        return date;
    }

}
