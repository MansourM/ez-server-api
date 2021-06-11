package ir.masterz.mansour.ez.serverapi.fan;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

        AndroidNetworking.post(Requests.get(0).getRequestUrl())
                .addHeaders("token", token)
                .setTag(getConfig().getLoggingTag())
                .addStringBody(Requests.get(0).getRequestJason().toString())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        log("Json connect Completed! : Response");
                        log(response);
                        JsonObject result = JsonParser.parseString(response).getAsJsonObject();

                        Requests.get(0).setResponseJason(result);
                        log("response Json= " + Requests.get(0).getResponseJason());

                        ServerApi.super.onResponse();
                    }

                    @Override
                    public void onError(ANError anError) {
                        log("Json connect Completed! : Error");

                        try {
                            log(anError.getMessage());
                            log(anError.getResponse().toString());
                        } catch (Exception e) {
                            log(e.getMessage());
                        }

                        retry();
                    }
                });
    }

    //TODO: Using it with your own JAVA Object - JSON Parser
    //TODO: Image Upload
    //TODO: add automatic Error handling / logging for lower callback levels
}
