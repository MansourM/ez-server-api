package ir.masterz.mansour.ez.serverapi;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public abstract class BaseApi {

    private final Context app_context;

    public final ArrayList<Request> Requests;

    private final Config Config;
    private int retriesLeft;

    public BaseApi(Context context) {
        app_context = context;
        Config = new Config();
        retriesLeft = Config.getRetryCount();
        Requests = new ArrayList<>();
    }

    public Context getAppContext() {
        return app_context;
    }

    public Config getConfig() {
        return Config;
    }

    public void log(String msg) {
        if (Config.loggingEnabled())
            Log.d(Config.getLoggingTag(), msg);
    }


    public void retry() {
        retriesLeft--;
        if (retriesLeft > 0) {
            log("Retrying, Retries Left :" + retriesLeft);
            connect();
        } else {
            retriesLeft = Config.getRetryCount();
            Requests.get(0).getCustomCallback().onResponse();
            Requests.get(0).getCustomCallback().onFailure();
            requestCompleted();
        }
    }

    public int getStatus() {
        return Requests.get(0).getResponseJason().get("status").getAsInt();
    }


    public String getMessage() {
        return Requests.get(0).getResponseJason().get("message").getAsString();
    }

    public JsonObject getData() {
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

    public void requestCompleted() {
        Requests.remove(0);
        if (Requests.size() > 0)
            connect();
        else
            log("request que done!");
    }

    public abstract void connect();
}
