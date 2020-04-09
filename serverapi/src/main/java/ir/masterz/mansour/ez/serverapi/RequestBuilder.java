package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

/**
 * Created by Sora on 4/22/2017.
 */

public class RequestBuilder {

    private Request request;
    private ApiServer sapi;

    public RequestBuilder(String url) {
        request = new Request(url);
    }

    public RequestBuilder(String url, ApiServer api) {
        sapi = api;
        request = new Request(url);
    }

    public RequestBuilder setMethod(String method) {
        request.setMethod(method);
        return this;
    }


    public RequestBuilder setToken(String token) {
        request.setToken(token);
        return this;
    }

    public RequestBuilder setRequestJason(JsonObject requestJason) {
        request.setRequestJason(requestJason);
        return this;
    }


    public void setCustomCallback(ListenerServerApi customCallback) {
        request.setCustomCallback(customCallback);
        sapi.checkDuplicates(request);
    }

}
