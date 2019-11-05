package com.think.runex.java.Utils.Network;

import android.app.Activity;

import com.google.gson.Gson;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.think.runex.java.Constants.APIs.DOMAIN;

public class NetworkUtils {

    // instance variables
    private static final String ct = "NetworkUtils->";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Activity activity;

    // explicit variable
    private final int HTTP_TEMPORARY_DERIRECT = 307;

    private NetworkUtils(Activity activity) {
        this.activity = activity;

    }

    public static NetworkUtils newInstance(Activity activity) {
        return new NetworkUtils(activity);
    }

    /**
     * Feature methods
     */
    private void addHeaders(NetworkProps props, Request.Builder builder) {

        for (int a = 0; a < props.headers.size(); a++) {
            // prepare usage variables
            final String[][] header = props.headers.get(a);

            // add header
            builder.addHeader(header[0][0], header[0][1]);
        }
    }

    private void onSuccess(int statusCode, String json, onNetworkCallback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // prepare usage variables
                xResponse rsp = new xResponse();

                // update props
                rsp.setResponseCode(statusCode);
                rsp.setJsonString(json);

                // trigger
                callback.onSuccess(rsp);

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
                rsp.setJsonString(Globals.GSON.toJson(e));

                // trigger
                callback.onFailure(rsp);

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

                    // add headers
                    addHeaders(props, builder);

                    // prepare usage variables
                    Request request = builder.build();
                    Response response = getHttpClient().newCall(request).execute();
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

    public void postFormData(NetworkProps props, onNetworkCallback callback) {
        // prepare usage variables
        final String mtn = ct + "postFormData() ";
        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                try {
                    // prepare usage variables
                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM);

                    // add parts
                    for (int a = 0; a < props.multiParts.size(); a++) {
                        // prepare usage variables
                        final Object[][] part = props.multiParts.get(a);

                        L.i(mtn + "Part name: " + part[0][0] + " val: " + part[0][1]);

//                        // conditions
                        if (part[0][2] != null)
                            multipartBuilder.addFormDataPart(part[0][0] + "", part[0][1] + "", (RequestBody) part[0][2]);
                        else {
                            multipartBuilder.addFormDataPart(part[0][0] + "", part[0][1] + "");

                        }
                    }

                    RequestBody body = multipartBuilder.build();
                    Request.Builder builder = new Request.Builder()
                            .url(props.url)
                            .post(body);

                    // add headers
                    addHeaders(props, builder);

                    try (Response response = getHttpClient().newCall(builder.build()).execute()) {
                        onSuccess(response.code(), response.body().string(), callback);

                    } catch (Exception e) {
                        L.e(mtn + "Err: " + e.getMessage());
                        onFailed(HttpURLConnection.HTTP_BAD_REQUEST, e, callback);

                    }

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
                    onFailed(HttpURLConnection.HTTP_BAD_REQUEST, e, callback);

                }
            }
        };

        thread(runner);
    }

    public void postJSON(NetworkProps props, onNetworkCallback callback) {
        // prepare usage variables
        final String mtn = ct + "postJSON() ";
        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                // prepare usage variables
                RequestBody body = RequestBody.create(new Gson().toJson(props.jsonAsObject), JSON);
                Request.Builder builder = new Request.Builder()
                        .url(props.url)
                        .post(body);

                // add headers
                addHeaders(props, builder);

                // build request
                Request request = builder.build();

                try (Response response = getHttpClient().newCall(request).execute()) {

                    // response as temporary redirect
                    if (response.code() == HTTP_TEMPORARY_DERIRECT) {
                        // prepare usage variables
                        String redirectUrl = response.header("Location");

                        // conditions
                        if (redirectUrl != null && !redirectUrl.isEmpty()) {
                            // update props
                            props.url = DOMAIN.VAL + redirectUrl;

                            // fire
                            postJSON(props, callback);

                        }
                    } else onSuccess(response.code(), response.body().string(), callback);

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
                    onFailed(HttpURLConnection.HTTP_BAD_REQUEST, e, callback);
                }

            }
        };

        thread(runner);


    }

    private OkHttpClient getHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }


}
