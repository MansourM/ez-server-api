package ir.masterz.mansour.ez.serverapi.ion;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import ir.masterz.mansour.ez.serverapi.BaseApi;

public class ServerApi extends BaseApi {
    public ServerApi(Context context) {
        super(context);
    }

    @Override
    public void connect() {
        if (Requests.size() < 1) {
            log("Error, not no request remaining, but in connect!!?");
            return;
        }

        String token = Requests.get(0).getToken();

        log("in Connect");
        log("url= " + Requests.get(0).getRequestUrl());
        log("Token= " + token);
        log("Request Json= " + Requests.get(0).getRequestJason());

        Ion.with(getAppContext())
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
