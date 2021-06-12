package ir.masterz.mansour.ez.serverapi;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.masterz.mansour.ez.serverapi.callback.ApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.CallBackRequestBuilt;
import ir.masterz.mansour.ez.serverapi.callback.FullApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.SuccessCallback;

public abstract class BaseApi {

    private final Context app_context;
    private int retriesLeft;

    private final Config Config;
    private CallbackDefaultHandler CallbackDefaultHandler;

    public final ArrayList<Request> Requests;

    protected BaseApi(Context context) {
        app_context = context;
        Config = new Config();
        retriesLeft = Config.getRetryCount();
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


    protected void retry() {
        retriesLeft--;
        if (retriesLeft > 0) {
            log("Retrying, Retries Left :" + retriesLeft);
            connect();
        } else {
            retriesLeft = Config.getRetryCount();
            if (Requests.get(0).getCustomCallback() instanceof FullApiCallback) {
                ((FullApiCallback) Requests.get(0).getCustomCallback()).onResponse();
                ((FullApiCallback) Requests.get(0).getCustomCallback()).onFailure();
            } else if (CallbackDefaultHandler != null)
                CallbackDefaultHandler.handleFailure();
            else
                Log.w(Config.getLoggingTag(), "using a ApiCallback/SuccessCallback without Setting up a CallbackDefaultHandler!");

            requestCompleted();
        }
    }

    protected int getStatus() {
        return Requests.get(0).getResponseJason().get("status").getAsInt();
    }


    protected String getMessage() {
        return Requests.get(0).getResponseJason().get("message").getAsString();
    }

    protected JsonObject getData() {
        try {
            return Requests.get(0).getResponseJason().get("data").getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public RequestBuilder request(String url) {
        log("Building Request!");
        return new RequestBuilder(url, new CallBackRequestBuilt() {
            @Override
            public void requestBuilt(Request request) {
                addRequestToQue(request);
            }
        });
    }

    private boolean isDuplicate(Request request) {
        for (Request req : Requests)
            if (req.equals(request)) {
                log("duplicate request removed = " + req.getRequestUrl());
                return true;
            }
        return false;
    }

    private void addRequestToQue(Request request) {
        if (!Config.allowDuplicateRequests() && isDuplicate(request))
            return;

        Requests.add(request);
        if (Requests.size() > 1)
            log("api is busy, adding request to que, req url = " + request.getRequestUrl());
        else
            connect();
    }

    protected void requestCompleted() {
        Requests.remove(0);
        if (Requests.size() > 0)
            connect();
        else
            log("request que done!");
    }

    protected abstract void connect();

    protected void onResponse() {
        SuccessCallback callback = Requests.get(0).getCustomCallback();
        warnHandler(callback);

        if (callback instanceof FullApiCallback)
            ((FullApiCallback) Requests.get(0).getCustomCallback()).onResponse();

        if (getStatus() != 0)
            onSuccess(callback, getMessage(), getData());
        else
            onErrorMessage(callback, getMessage(), getData());

        Requests.get(0).success();
        requestCompleted();
    }

    protected void onErrorMessage(SuccessCallback callback, String message, JsonObject data) {
        if (callback instanceof ApiCallback)
            ((ApiCallback) Requests.get(0).getCustomCallback()).onErrorMessage(message, data);
        else if (CallbackDefaultHandler != null)
            CallbackDefaultHandler.handleErrorMessage(message, data);
    }

    protected void onSuccess(SuccessCallback callback, String message, JsonObject data) {
        callback.onSuccess(message, data);
    }

    private void warnHandler(SuccessCallback callback) {

        if (CallbackDefaultHandler == null && !(callback instanceof ApiCallback))
            Log.w(Config.getLoggingTag(), "using a SuccessCallback without Setting up a CallbackDefaultHandler!");


    }

    //TODO: Using it with your own JAVA Object - JSON Parser
    //TODO: Image Upload
    //TODO: add automatic Error/Failure handling (Done) maybe redo it with lambda later?
    //TODO: make token header and json body optional
    //TODO: think about decentralized API requests
}
