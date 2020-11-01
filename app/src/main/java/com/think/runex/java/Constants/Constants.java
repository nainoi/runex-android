package com.think.runex.java.Constants;

import java.util.Date;

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
