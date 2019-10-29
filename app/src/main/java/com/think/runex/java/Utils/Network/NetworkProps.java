package com.think.runex.java.Utils.Network;

import java.util.ArrayList;
import java.util.List;

public class NetworkProps {
    public String url;
    public List<String[][]> headers = new ArrayList<>();
    public Object jsonAsObject;

    public NetworkProps addHeader(String name, String value){
        String[][] s = { {name, value} };
        headers.add( s );
        return this;
    }
    public NetworkProps setUrl(String url) {
        this.url = url;
        return this;
    }

    public NetworkProps setJsonAsObject(Object jsonAsObject) {
        this.jsonAsObject = jsonAsObject;
        return this;
    }
}
