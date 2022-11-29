package ir.masterz.mansour.ez.serverapi.fan;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import ir.masterz.mansour.ez.serverapi.BaseApi;
import ir.masterz.mansour.ez.serverapi.Request;
import ir.masterz.mansour.fan.core.common.ANRequest;
import ir.masterz.mansour.fan.core.error.ANError;
import ir.masterz.mansour.fan.core.interfaces.StringRequestListener;
import okhttp3.OkHttpClient;

public class ServerApi extends BaseApi {
    public ServerApi(Context context) {
        super(context);
    }

    @Override
    public void connect(final Request request) {
        log("connecting: "+ request.getRequestUrl());
        log("Token= " + request.getToken());
        log("Request Json= " + request.getRequestJson());

        ANRequest.PostRequestBuilder postRequestBuilder = new ANRequest.PostRequestBuilder(request.getRequestUrl());

        if (request.getRequestTimeout() != 10) {
            postRequestBuilder.setOkHttpClient(new OkHttpClient.Builder()
                    .connectTimeout(request.getRequestTimeout(), TimeUnit.SECONDS)
                    .build());
        }

        if (request.getTag() != null) {
            postRequestBuilder.setTag(request.getTag());
        }

        if (request.getToken() != null) {
            postRequestBuilder.addHeaders("token", request.getToken());
        }

        if (request.getRequestJson() != null) {
            postRequestBuilder.addStringBody(request.getRequestJson().toString());
        }



        postRequestBuilder.build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        log("onResponse");
                        log(response);
                        try {
                            JsonObject result = JsonParser.parseString(response).getAsJsonObject();
                            request.setResponseJson(result);
                            ServerApi.super.onValidResponse();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ServerApi.super.onResponseParseError();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        log("onError");

                        try {
                            log("ANError: " + anError.getMessage());
                        } catch (Exception e) {
                            log("ANError: catch!: " + e.getMessage());
                            e.printStackTrace();
                        }
                        retry();
                    }
                });
    }
}
