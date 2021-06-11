package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

import ir.masterz.mansour.ez.serverapi.callback.SuccessCallback;

/**
 * Created by Sora on 4/22/2017.
 */

public class Request {

    private String Method;
    private String RequestUrl;
    private String Token;
    private JsonObject RequestJason;
    private JsonObject ResponseJason;
    private SuccessCallback CustomCallback;
    private boolean success;

    public Request() {
        Token = "pub";
        ResponseJason = new JsonObject();
        RequestJason = new JsonObject();
        success = false;
    }

    public Request(String url) {
        Token = "pub";
        ResponseJason = new JsonObject();
        RequestJason = new JsonObject();
        success = false;
        setRequestUrl(url);
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public boolean wasSuccessful() {
        return success;
    }

    public void success() {
        success = true;
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

    public JsonObject getRequestJason() {
        return RequestJason;
    }

    public void setRequestJason(JsonObject requestJason) {
        RequestJason = requestJason;
    }

    public JsonObject getResponseJason() {
        return ResponseJason;
    }

    public void setResponseJason(JsonObject responseJason) {
        ResponseJason = responseJason;
    }

    public SuccessCallback getCustomCallback() {
        return CustomCallback;
    }

    public void setCustomCallback(SuccessCallback customCallback) {
        CustomCallback = customCallback;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Request))
            return false;

        Request that = (Request) other;

        return this.getRequestUrl().equals(that.getRequestUrl())
                && this.getRequestJason().equals(that.getRequestJason())
                && this.getMethod().equals(that.getMethod())
                && this.getToken().equals(that.getToken());
    }

}
