package com.think.runex.java.Constants;

import com.think.runex.feature.event.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum Constants {
    TAG("KT_PJ");

    public String VAL;

    Constants(String description){
       VAL = description;


    }

    public static class Fragment {
        public static String TAG(){
            return Fragment.class.getSimpleName() +""+ new Date().getTime();
        }
    }
}
