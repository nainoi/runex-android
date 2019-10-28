package com.think.runex.java.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.think.runex.java.Activities.LoginActivity;
import com.think.runex.java.Constants.Globals;

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
    private final String APP_ENTITY = "appEntity";

    // singleton
    private App( Context context) {
        this.context = context;
        init();

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

    public AppEntity getAppEntity(){
        final String json = spf.getString(APP_ENTITY, null);
        if( json != null ){
            return Globals.GSON.fromJson( json, AppEntity.class );

        }

        return new AppEntity();
    }
    public void save(AppEntity appEntity){
        editor.putString( APP_ENTITY, Globals.GSON.toJson( appEntity ));
        editor.commit();

    }


    public void clear(){
        editor.clear().commit();
    }
    public void serveLoginPage(Fragment fragment, int requestCode){
        Intent i = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivityForResult(i, requestCode);
    }

    public void serveLoginPage(Activity activity, int requestCode){
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(i, requestCode);
    }

}