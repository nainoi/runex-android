package com.think.runex.java.Utils.Network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NetworkMultipartProps {

    private final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");
    private MultipartBody.Builder builder;

    public List<String[][]> headers = new ArrayList<>();
    public String url;

    public NetworkMultipartProps() {
        builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
    }

    public NetworkMultipartProps addImageFile(String key, String imagePath) {
        builder.addFormDataPart(key, getFileName(imagePath), RequestBody.create(MEDIA_TYPE_IMAGE, new File(imagePath)));
        return this;
    }

    public NetworkMultipartProps addFormDataPart(String key, String value) {
        builder.addFormDataPart(key, value);
        return this;
    }

    public NetworkMultipartProps addHeader(String name, String value) {
        String[][] s = {{name, value}};
        headers.add(s);
        return this;
    }

    public NetworkMultipartProps setUrl(String url) {
        this.url = url;
        return this;
    }


    public RequestBody getRequestBody() {
        if (builder != null) {
            return builder.build();
        } else {
            return null;
        }
    }

    private String getFileName(String filePath) {
        int cut = filePath.lastIndexOf('/');
        if (cut != -1) {
            return filePath.substring(cut + 1);
        } else {
            return ("file" + filePath.split(".")[1]);
        }
    }


}
