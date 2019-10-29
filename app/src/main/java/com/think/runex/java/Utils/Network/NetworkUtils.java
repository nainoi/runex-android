package com.think.runex.java.Utils.Network;

import android.app.Activity;

import com.google.gson.Gson;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;

import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {

    // instance variables
    private static final String ct = "NetworkUtils->";
    private static final MediaType JSON = MediaType.get("application/json");
    private static OkHttpClient client = new OkHttpClient();
    private Activity activity;

    private NetworkUtils(Activity activity) {
        this.activity = activity;

    }

    public static NetworkUtils newInstance(Activity activity) {
        return new NetworkUtils(activity);
    }

    /**
     * Feature methods
     */
    private void onSuccess(int statusCode, String json, onNetworkCallback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // prepare usage variables
                xResponse rsp = new xResponse();

                // update props
                rsp.setResponseCode( statusCode);
                rsp.setJsonString( json );

                // trigger
                callback.onSuccess( rsp );

            }
        });
    }

    private void onFailed(int statusCode, Exception e, onNetworkCallback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // prepare usage variables
                xResponse rsp = new xResponse();

                // update props
                rsp.setResponseCode(statusCode);
                rsp.setJsonString(Globals.GSON.toJson( e ));

                // trigger
                callback.onFailure( rsp );

            }
        });
    }

    private void thread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public void get(NetworkProps props, onNetworkCallback callback) {
        // prepare usage variables
        final String mtn = ct + "get() ";
        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                try {
                    // prepare usage variables
                    Request.Builder builder = new Request.Builder()
                            .url(props.url);

                    for (int a = 0; a < props.headers.size(); a++) {
                        // prepare usage variables
                        final String[][] header = props.headers.get(a);

                        // add header
                        builder.addHeader(header[0][0], header[0][1]);
                    }

                    // prepare usage variables
                    Request request = builder.build();
                    Response response = client.newCall(request).execute();
                    final String strResult = response.body().string();

                    try {
                        onSuccess(response.code(), strResult, callback);

                    } catch (Exception e) {
                        L.e(mtn + "Err: " + e);
                        onFailed(HttpURLConnection.HTTP_BAD_REQUEST, e, callback);

                    }


                } catch (Exception e) {
                    L.e(mtn + "Err: " + e);
                    onFailed(HttpURLConnection.HTTP_BAD_REQUEST, e, callback);

                }


            }
        };

        // on thread
        thread(runner);
    }

    public void postJSON(NetworkProps props, onNetworkCallback callback) {
        // prepare usage variables
        final String mtn = ct + "postJSON() ";
        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(new Gson().toJson(props.jsonAsObject), JSON);
                Request.Builder builder = new Request.Builder()
                        .url(props.url)
                        .post(body);

                // add headers
                for (int a = 0; a < props.headers.size(); a++) {
                    // prepare usage variables
                    final String[][] header = props.headers.get(a);

                    // add header
                    builder.addHeader(header[0][0], header[0][1]);
                }

                // build request
                Request request = builder.build();

                try (Response response = client.newCall(request).execute()) {
                    onSuccess(response.code(), response.body().string(), callback);

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
                    onFailed(HttpURLConnection.HTTP_BAD_REQUEST, e, callback);
                }

            }
        };

        thread(runner);


    }
}
