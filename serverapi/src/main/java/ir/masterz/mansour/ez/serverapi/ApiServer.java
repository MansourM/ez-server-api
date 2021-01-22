package ir.masterz.mansour.ez.serverapi;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ApiServer {

    private static String TAG = "api_server";
    private static boolean LOGGING_ENABLED = false;

    private static final int RETRY_COUNT = 3; //2+1 = 3 in total

    private ArrayList<Request> Requests;
    private int retriesLeft;
    private Context app_context;


    public ApiServer(Context context) {
        app_context = context;
        retriesLeft = RETRY_COUNT - 1;
        Requests = new ArrayList<>();
    }

    public static void setLogging(boolean isEnabled) {
        LOGGING_ENABLED = isEnabled;
    }

    public static void setLoggingTag(String tag) {
        TAG = tag;
    }

    private static void log(String msg) {
        if (LOGGING_ENABLED)
            Log.d(TAG, msg);
    }


    private void retry() {
        retriesLeft--;
        if (retriesLeft > 0) {
            log("Retrying, Retries Left :" + retriesLeft);
            connect();
        } else {
            retriesLeft = RETRY_COUNT;
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

    public RequestBuilder addRequestToQue(String url) {
        return new RequestBuilder(url, this);
    }

    public void checkDuplicates(Request request) {
        for (Request req : Requests)
            if (req.equals(request)) {
                log("duplicate request removed = " + req.getRequestUrl());
                return;
            }

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
