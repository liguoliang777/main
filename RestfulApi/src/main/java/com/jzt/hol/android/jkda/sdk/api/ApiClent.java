package com.jzt.hol.android.jkda.sdk.api;

/**
 * Created by tangkun on 16/9/5.
 */
public class ApiClent implements IApiEndPoint {

    private String hostname;

    public ApiClent(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String getApiOauthUrlEndpoint() {
        return null;
    }

    @Override
    public String getApiEndpoint() {
        if (!hostname.startsWith("http://")) {
            hostname = "http://" + hostname;
        }
        if (!hostname.endsWith("/")) {
            hostname += "/";
        }
        return hostname;
    }
}
