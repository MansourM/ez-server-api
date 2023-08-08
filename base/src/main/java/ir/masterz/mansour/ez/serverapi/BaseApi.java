package ir.masterz.mansour.ez.serverapi;

import static ir.masterz.mansour.ez.serverapi.Config.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.masterz.mansour.ez.serverapi.callback.CallBackRequestBuilt;
import ir.masterz.mansour.ez.serverapi.callback.basic.ErrorCallback;
import ir.masterz.mansour.ez.serverapi.callback.basic.FailureCallback;
import ir.masterz.mansour.ez.serverapi.callback.basic.ResponseCallback;
import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;

public abstract class BaseApi {

    private final Context app_context;

    private final Config Config;
    private CallbackDefaultHandler CallbackDefaultHandler;

    public final ArrayList<Request> Requests;

    protected BaseApi(Context context) {
        app_context = context;
        Config = new Config();
        Requests = new ArrayList<>();
    }

    public abstract static class CallbackDefaultHandler {
        public abstract void handleErrorMessage(String message, JsonObject data);

        public abstract void handleFailure();
    }

    public void setErrorMessageHandler(CallbackDefaultHandler callbackDefaultHandler) {
        CallbackDefaultHandler = callbackDefaultHandler;
    }

    protected Context getAppContext() {
        return app_context;
    }

    public Config getConfig() {
        return Config;
    }

    public void log(String msg) {
        if (Config.loggingEnabled())
            Log.d(Config.getLoggingTag(), msg);
    }

    public void loge(String msg) {
        if (Config.loggingEnabled())
            Log.e(Config.getLoggingTag(), msg);
    }

    public RequestBuilder request(String url) {
        log("Building Request!");
        return new RequestBuilder(url, new CallBackRequestBuilt() {
            @Override
            public void requestBuilt(Request request) {
                if (request.getMethod() == Request.Method.GET && request.getRequestJson() != null)
                    Log.w(TAG, "GET requests can't have a string body, use query params instead!, url: " + request.getUrl());
                addRequestToQue(request);
            }
        });
    }

    public RequestBuilder request(UrlHelper urlHelper) {
        log("Building Request!");
        return new RequestBuilder(urlHelper, new CallBackRequestBuilt() {
            @Override
            public void requestBuilt(Request request) {
                addRequestToQue(request);
            }
        });
    }

    private boolean isDuplicate(Request request) {
        for (Request req : Requests)
            if (req.equals(request)) {
                log("Duplicate request removed, url: " + req.getUrl());
                return true;
            }
        return false;
    }

    private void addRequestToQue(Request request) {
        if (isDuplicate(request) && !Config.allowDuplicateRequests()) {
            SuccessCallback callback = request.getCustomCallback();
            if (callback instanceof ResponseCallback)
                ((ResponseCallback) getCurrentRequest().getCustomCallback()).onResponse();
            return;
        }

        Requests.add(request);
        if (Requests.size() > 1)
            log("Api is busy, adding request to que, url: " + request.getUrl());
        else
            connect(getCurrentRequest());
    }

    protected void processNextRequest() {
        Requests.remove(0);
        if (Requests.size() > 0)
            connect(getCurrentRequest());
        else
            log("Request que done!");
    }

    protected abstract void connect(final Request request);


    private Request getCurrentRequest() {
        return Requests.get(0);
    }

    //success or error message
    protected void onValidResponse() {
        SuccessCallback callback = getCurrentRequest().getCustomCallback();
        String status = getCurrentRequest().getResponseJson().get("status").getAsString();
        String message = getCurrentRequest().getResponseJson().get("message").getAsString();

        if (callback instanceof ResponseCallback)
            ((ResponseCallback) getCurrentRequest().getCustomCallback()).onResponse();

        if (status.equals("success"))
            onSuccess(callback,
                    message,
                    getCurrentRequest().getResponseJson()
            );
        else
            onErrorMessage(
                    callback,
                    message,
                    getCurrentRequest().getResponseJson()
            );

        getCurrentRequest().success();
        processNextRequest();
    }

    protected void onResponseParseError() {
        loge("Unable parse the response string!");

        SuccessCallback callback = getCurrentRequest().getCustomCallback();

        if (callback instanceof ResponseCallback)
            ((ResponseCallback) getCurrentRequest().getCustomCallback()).onResponse();

        if (callback instanceof FailureCallback)
            ((FailureCallback) getCurrentRequest().getCustomCallback()).onFailure();
        else if (CallbackDefaultHandler != null)
            CallbackDefaultHandler.handleFailure();

        processNextRequest();
    }

    protected void onErrorMessage(SuccessCallback callback, String message, JsonObject data) {
        if (callback instanceof ErrorCallback)
            ((ErrorCallback) getCurrentRequest().getCustomCallback()).onErrorMessage(message, data);
        else if (CallbackDefaultHandler != null)
            CallbackDefaultHandler.handleErrorMessage(message, data);
    }


    protected void onSuccess(SuccessCallback callback, String message, JsonObject data) {
        callback.onSuccess(message, data);
    }


    //TODO: Using it with your own JAVA Object - JSON Parser
    //TODO: Image Upload
}
