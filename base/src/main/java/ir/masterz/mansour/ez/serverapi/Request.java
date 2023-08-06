package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

import java.util.Objects;

import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;

/**
 * Created by Sora on 4/22/2017.
 */

public class Request {

    private String RequestUrl;

    private int MethodId;
    private String Tag;
    private String Token;
    private JsonObject RequestJson;
    private JsonObject ResponseJson;
    private SuccessCallback CustomCallback;
    private boolean Success;
    private int RequestTimeout;

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

    public String geUrl() {
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

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
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


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Request))
            return false;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(RequestUrl, Tag, Token, RequestJson);
    }
}
