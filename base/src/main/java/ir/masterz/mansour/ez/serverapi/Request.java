package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

import java.util.Objects;

import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;

/**
 * Created by Sora on 4/22/2017.
 */

public class Request {

    private String RequestUrl;
    private String Tag;
    private String Token;
    private JsonObject RequestJson;
    private JsonObject ResponseJson;
    private SuccessCallback CustomCallback;
    private boolean Success;
    private int RequestTimeout;

    public Request() {
        setDefaults();
    }

    public Request(String url) {
        setDefaults();
        setRequestUrl(url);
    }


    private void setDefaults() {
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

    public String getRequestUrl() {
        return RequestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        RequestUrl = requestUrl;
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
        return this.hashCode() == ((Request) other).hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(RequestUrl, Tag, Token, RequestJson);
    }
}
