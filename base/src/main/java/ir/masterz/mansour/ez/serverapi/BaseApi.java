package ir.masterz.mansour.ez.serverapi;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import ir.masterz.mansour.ez.serverapi.callback.ApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.CallBackRequestBuilt;
import ir.masterz.mansour.ez.serverapi.callback.FullApiCallback;

public abstract class BaseApi {

    private final Context app_context;

    public final ArrayList<Request> Requests;

    private final Config Config;
    private int retriesLeft;

    protected BaseApi(Context context) {
        app_context = context;
        Config = new Config();
        retriesLeft = Config.getRetryCount();
        Requests = new ArrayList<>();
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
            }
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
        if (Requests.get(0).getCustomCallback() instanceof FullApiCallback)
            ((FullApiCallback) Requests.get(0).getCustomCallback()).onResponse();

        if (getStatus() != 0)
            onSuccess();
        else
            onErrorMessage();

        Requests.get(0).success();
        requestCompleted();
    }

    protected void onErrorMessage() {
        if (Requests.get(0).getCustomCallback() instanceof ApiCallback) {
            ((ApiCallback) Requests.get(0).getCustomCallback()).onErrorMessage(getMessage(), getData());
        }
    }

    protected void onSuccess() {
        Requests.get(0).getCustomCallback().onSuccess(getMessage(), getData());
    }
}
