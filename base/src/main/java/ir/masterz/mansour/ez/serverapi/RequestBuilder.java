package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ir.masterz.mansour.ez.serverapi.callback.CallBackRequestBuilt;
import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;

/**
 * Created by Sora on 4/22/2017.
 */

public class RequestBuilder {

    private final Request request;
    private CallBackRequestBuilt Callback;

    public RequestBuilder(String url) {
        request = new Request(url);
    }

    public RequestBuilder(String url, CallBackRequestBuilt callback) {
        Callback = callback;
        request = new Request(url);
    }

    public RequestBuilder(UrlHelper urlHelper, CallBackRequestBuilt callback) {
        Callback = callback;
        request = new Request(urlHelper.url);
        request.setMethod(urlHelper.method);
    }

    public RequestBuilder setMethod(int methodId) {
        request.setMethod(methodId);
        return this;
    }

    public RequestBuilder setTag(String tag) {
        request.setTag(tag);
        return this;
    }

    public RequestBuilder setToken(String token) {
        request.setToken(token);
        return this;
    }

    public RequestBuilder setRequestJason(JsonObject requestJason) {
        request.setRequestJson(requestJason);
        return this;
    }

    public RequestBuilder setRequestTimeout(int seconds) {
        request.setRequestTimeout(seconds);
        return this;
    }

    public RequestBuilder addHeader(String key, String value) {
        request.addHeader(key, value);
        return this;
    }

    public RequestBuilder setHeaderAcceptJson() {
        request.addHeader("Accept", "application/json");
        return this;
    }

    public RequestBuilder addHeaders(HashMap<String, String> headers) {
        request.setHeaders(headers);
        return this;
    }

    public void setCustomCallback(SuccessCallback customCallback) {
        request.setCustomCallback(customCallback);
        Callback.requestBuilt(request);
    }

}
