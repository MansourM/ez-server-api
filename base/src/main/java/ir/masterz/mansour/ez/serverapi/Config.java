package ir.masterz.mansour.ez.serverapi;

import java.util.HashMap;

public class Config {
    public static String TAG = "ez_server_api";
    private boolean LoggingEnabled = false;
    private boolean AllowDuplicateRequests = false;

    private HashMap<String, String> Headers;

    public Config() {
        Headers = new HashMap<>();
    }

    public Config setLogging(boolean isEnabled) {
        LoggingEnabled = isEnabled;
        return this;
    }

    public boolean loggingEnabled() {
        return LoggingEnabled;
    }

    public Config setLoggingTag(String tag) {
        TAG = tag;
        return this;
    }

    public String getLoggingTag() {
        return TAG;
    }

    public boolean allowDuplicateRequests() {
        return AllowDuplicateRequests;
    }

    public Config setAllowDuplicateRequests(boolean allow) {
        AllowDuplicateRequests = allow;
        return this;
    }

    //Adds Header: ["Accept" => "application/json"]
    public Config setHeaderAcceptJson() {
        Headers.put("Accept", "application/json");
        return this;
    }

    public Config setHeaders(HashMap<String, String> headers) {
        Headers = headers;
        return this;
    }

    public HashMap<String, String> getHeaders() {
        return Headers;
    }

    public Config addHeader(String key, String value) {
        Headers.put(key, value);
        return this;
    }

    public void removeHeader(String key) {
        Headers.remove(key);
    }

    public Config addBearerToken(String token) {
        Headers.put("Authorization", "Bearer " + token);
        return this;
    }

    public Config removeBearerToken() {
        Headers.remove("Authorization");
        return this;
    }

    public String getHeader(String key) {
        return Headers.get(key);
    }

    public void clearHeaders() {
        Headers.clear();
    }

}
