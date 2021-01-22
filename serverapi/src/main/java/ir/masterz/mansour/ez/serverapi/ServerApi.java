package ir.masterz.mansour.ez.serverapi;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ServerApi {


    private final Context app_context;

    private final ArrayList<Request> Requests;

    private final Config Config;
    private int retriesLeft;

    public ServerApi(Context context) {
        app_context = context;
        Config = new Config();
        retriesLeft = Config.getRetryCount();
        Requests = new ArrayList<>();
    }

    public Config getConfig() {
        return Config;
    }

    private void log(String msg) {
        if (Config.loggingEnabled())
            Log.d(Config.getLoggingTag(), msg);
    }


    private void retry() {
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

    private int getStatus() {
        return Requests.get(0).getResponseJason().get("status").getAsInt();
    }


    private String getMessage() {
        return Requests.get(0).getResponseJason().get("message").getAsString();
    }

    private JsonObject getData() {
        try {
            return Requests.get(0).getResponseJason().get("data").getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public RequestBuilder request(String url) {
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
        if (!Config.allowDuplicateRequests() || isDuplicate(request))
            return;

        Requests.add(request);
        if (Requests.size() > 1)
            log("api is busy, adding request to que, req url = " + request.getRequestUrl());
        else
            connect();
    }

    private void requestCompleted() {
        Requests.remove(0);
        if (Requests.size() > 0)
            connect();
        else
            log("request que done!");
    }

    private void connect() {

        if (Requests.size() < 1) {
            log("Error, not no request remaining, but in connect!!?");
            return;
        }

        String token = Requests.get(0).getToken();

        log("in Connect");
        log("url= " + Requests.get(0).getRequestUrl());
        log("Token= " + token);
        log("Request Json= " + Requests.get(0).getRequestJason());

        Ion.with(app_context)
                .load(Requests.get(0).getRequestUrl())
                .setHeader("token", token)
                .setJsonObjectBody(Requests.get(0).getRequestJason())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        log("Json connect Completed!");
                        if (e != null) {
                            log("" + e.getMessage());
                            log("fail res: " + result);
                            e.printStackTrace();
                            retry();
                        } else {
                            Requests.get(0).setResponseJason(result);
                            log("response Json= " + Requests.get(0).getResponseJason());
                            Requests.get(0).getCustomCallback().onResponse();
                            if (getStatus() != 0)
                                Requests.get(0).getCustomCallback().onSuccess(getMessage(), getData());
                            else {
                                Requests.get(0).getCustomCallback().onErrorMessage(getMessage(), getData());
                            }
                            Requests.get(0).success();
                            requestCompleted();
                        }
                    }
                });

    }
}
