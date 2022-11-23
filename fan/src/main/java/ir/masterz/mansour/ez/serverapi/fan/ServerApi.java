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

        ANRequest.PostRequestBuilder postRequestBuilder = new ANRequest.PostRequestBuilder(request.getRequestUrl());

        if (request.getRequestTimeout() != 10)
            postRequestBuilder.setOkHttpClient(new OkHttpClient.Builder()
                    .connectTimeout(request.getRequestTimeout(), TimeUnit.SECONDS)
                    .build());

        if (request.getTag() != null)
            postRequestBuilder.setTag(request.getTag());

        if (request.getToken() != null)
            postRequestBuilder.addHeaders("token", request.getToken());

        if (request.getRequestJson() != null)
            postRequestBuilder.addStringBody(request.getRequestJson().toString());

        log("in Connect");
        log("url= " + request.getRequestUrl());
        log("Token= " + request.getToken());
        log("Request Json= " + request.getRequestJson());

        postRequestBuilder.build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        log("Json connect Completed! : Response");
                        log(response);
                        JsonObject result = JsonParser.parseString(response).getAsJsonObject();

                        request.setResponseJson(result);
                        log("response Json= " + request.getResponseJson());

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
}
