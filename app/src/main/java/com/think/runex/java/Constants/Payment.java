package com.think.runex.java.Constants;

import com.think.runex.R;

public enum Payment {
    PAYMENT_SUCCESS("PAYMENT_SUCCESS", "5EB503", "สำเร็จ"),
    PAYMENT_WAITING("PAYMENT_WAITING", "494E52", "รอชำระเงิน"),
    PAYMENT_WAITING_APPROVE("PAYMENT_WAITING_APPROVE", "F7B500", "รอการตรวจสอบ"),
    NONE("NONE", "F7B500", "รอการตรวจสอบ"),
    ;

    public final String COLOR_HEX;
    public final String DESC;
    public final String KEY;
    private Payment(String key, String colorHex, String desc){
        this.COLOR_HEX = colorHex;
        this.DESC = desc;
        this.KEY = key;
    }

    public static Payment match( String key ){
        // prepare usage variables
        Payment[] p = values();

        for( int a = 0; a < p.length; a++){
            if( p[a].KEY.equalsIgnoreCase( key ) ){
                return p[a];

            }

        }

        return NONE;
    }
}