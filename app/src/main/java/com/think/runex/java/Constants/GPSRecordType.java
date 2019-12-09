package com.think.runex.java.Constants;

public enum GPSRecordType {
    START("start", 0, "@sep"),
    END("end", 1, ""),
    RECORDING( "recording", 2, "@sep"),
    ;

    public final int ID;
    public final String DESC;
    public final String SYMBOL;
    private GPSRecordType(String DESC, int ID, String symb){
        this.ID = ID;
        this.DESC = DESC;
        this.SYMBOL = symb;
    }
}
