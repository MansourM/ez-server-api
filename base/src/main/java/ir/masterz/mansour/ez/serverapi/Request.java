package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Objects;

import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;

/**
 * Created by Sora on 4/22/2017.
 */

public class Request {

    private String RequestUrl;

    private int MethodId;
    private String Tag;
    private JsonObject RequestJson;
    private JsonObject ResponseJson;
    private SuccessCallback CustomCallback;
    private boolean Success;
    private int RequestTimeout;
    private HashMap<String, String> Headers;

    public static class Method {
        public static final int GET = 0;
        public static final int POST = 1;
        public static final int DELETE = 2;
        public static final int PUT = 3;
        public static final int PATCH = 4;
    }

    public Request() {
        setDefaults();
    }

    public Request(String url) {
        setDefaults();
        setUrl(url);
    }


    private void setDefaults() {
        MethodId = Method.POST;
        RequestTimeout = 10;
        ResponseJson = new JsonObject();
        Success = false;
        Headers = new HashMap<>();
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public boolean wasSuccessful() {
        return Success;
    }

    public void success() {
        Success = true;
    }

    public String getUrl() {
        return RequestUrl;
    }

    public void setUrl(String requestUrl) {
        RequestUrl = requestUrl;
    }

    public int getMethod() {
        return MethodId;
    }

    public void setMethod(int methodId) {
        MethodId = methodId;
    }

    public JsonObject getRequestJson() {
        return RequestJson;
    }

    public void setRequestJson(JsonObject requestJson) {
        RequestJson = requestJson;
    }

    public JsonObject getResponseJson() {
        return ResponseJson;
    }

    public void setResponseJson(JsonObject responseJson) {
        ResponseJson = responseJson;
    }

    public SuccessCallback getCustomCallback() {
        return CustomCallback;
    }

    public void setCustomCallback(SuccessCallback customCallback) {
        CustomCallback = customCallback;
    }

    public int getRequestTimeout() {
        return RequestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        RequestTimeout = requestTimeout;
    }

    public HashMap<String, String> getHeaders() {
        return Headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        Headers = headers;
    }

    public void addHeader(String key, String value) {
        Headers.put(key, value);
    }

    public String getHeader(String key) {
        return Headers.get(key);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Request))
            return false;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(RequestUrl, Tag, RequestJson);
    }
}
