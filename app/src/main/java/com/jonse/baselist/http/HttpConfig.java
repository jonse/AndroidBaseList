package com.jonse.baselist.http;

public final class HttpConfig {

    public static String appendParam(String url, String key, String value) {
        if (url.contains("?")) {
            return url + "&" + key + "=" + value;
        }
        return url + "?" + key + "=" + "value";
    }

    public static String appendParam(String url, String key, int value) {
        if (url.contains("?")) {
            return url + "&" + key + "=" + value;
        }
        return url + "?" + key + "=" + "value";
    }
}
