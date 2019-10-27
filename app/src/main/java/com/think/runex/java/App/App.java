package com.think.runex.java.App;

import android.content.Context;
import android.content.SharedPreferences;

public class App {
    /**
     * Main variables
     */
    private static App ins = null;
    private static SharedPreferences spf;
    private static SharedPreferences.Editor editor;
    private Context context;


    // explicit variables
    private final String KEY_DB = "db-app-runex";

    // singleton
    private App( Context context) {
        this.context = context;

    }

    public static App instance( Context context) {
        return (ins == null)
                ? ins = new App( context ) : ins;
    }

    /** Feature methods */
    private void init(){
        spf = context.getSharedPreferences(KEY_DB, Context.MODE_PRIVATE);
        editor = spf.edit();

    }



}
