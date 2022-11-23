package ir.masterz.mansour.ez.serverapi;

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

    public void loge(String msg) {
        if (Config.loggingEnabled())
            Log.e(Config.getLoggingTag(), msg);
    }

    protected void retry() {
        retriesLeft--;
        if (retriesLeft > 0) {
            log("Retrying, Retries Left :" + retriesLeft);
            connect(Requests.get(0));
            return;
        }

        //Handling onFailure and onResponse before finishing current "failed" request
        if (Requests.get(0).getCustomCallback() instanceof ResponseCallback)
            ((ResponseCallback) Requests.get(0).getCustomCallback()).onResponse();

        if (Requests.get(0).getCustomCallback() instanceof FailureCallback)
            ((FailureCallback) Requests.get(0).getCustomCallback()).onFailure();
        else if (CallbackDefaultHandler != null)
            CallbackDefaultHandler.handleFailure();

        //reset retries go next
        retriesLeft = Config.getRetryCount();
        processNextRequest();
    }

    protected int getStatus() {
        return Requests.get(0).getResponseJson().get("status").getAsInt();
    }


    protected String getMessage() {
        return Requests.get(0).getResponseJson().get("message").getAsString();
    }

    protected JsonObject getData() {
        try {
            return Requests.get(0).getResponseJson().get("data").getAsJsonObject();
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
        if (isDuplicate(request) && !Config.allowDuplicateRequests())
            return;

        Requests.add(request);
        if (Requests.size() > 1)
            log("api is busy, adding request to que, req url = " + request.getRequestUrl());
        else
            connect();
    }

    protected void processNextRequest() {
        Requests.remove(0);
        if (Requests.size() > 0)
            connect();
        else
            log("request que done!");
    }

    @Deprecated
    protected void connect() {
        connect(Requests.get(0));
    }

    protected abstract void connect(final Request request);

    //success or error message
    protected void onValidResponse() {
        SuccessCallback callback = Requests.get(0).getCustomCallback();

        log("valid r:" + callback.getClass().getSimpleName() + ":" + ResponseCallback.class.getSimpleName());

        if (callback instanceof ResponseCallback)
            ((ResponseCallback) Requests.get(0).getCustomCallback()).onResponse();

        if (getStatus() != 0)
            onSuccess(callback, getMessage(), getData());
        else
            onErrorMessage(callback, getMessage(), getData());

        Requests.get(0).success();
        processNextRequest();
    }

    protected void onResponseParseError() {
        loge("unable parse the response string!");

        SuccessCallback callback = Requests.get(0).getCustomCallback();

        if (callback instanceof ResponseCallback)
            ((ResponseCallback) Requests.get(0).getCustomCallback()).onResponse();

        if (callback instanceof FailureCallback)
            ((FailureCallback) Requests.get(0).getCustomCallback()).onFailure();

        processNextRequest();
    }

    protected void onErrorMessage(SuccessCallback callback, String message, JsonObject data) {
        if (callback instanceof ErrorCallback)
            ((ErrorCallback) Requests.get(0).getCustomCallback()).onErrorMessage(message, data);
        else if (CallbackDefaultHandler != null)
            CallbackDefaultHandler.handleErrorMessage(message, data);
    }

    //TODO: hmmmmm :D
    protected void onSuccess(SuccessCallback callback, String message, JsonObject data) {
        callback.onSuccess(message, data);
    }


    //TODO: Using it with your own JAVA Object - JSON Parser
    //TODO: Image Upload
    //TODO: think about decentralized API requests
}
