package ir.masterz.mansour.ez.serverapi.fan;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import ir.masterz.mansour.ez.serverapi.BaseApi;
import ir.masterz.mansour.ez.serverapi.Request;
import ir.masterz.mansour.fan.core.common.ANRequest;
import ir.masterz.mansour.fan.core.common.RequestBuilder;
import ir.masterz.mansour.fan.core.error.ANError;
import ir.masterz.mansour.fan.core.interfaces.StringRequestListener;
import okhttp3.OkHttpClient;

public class ServerApi extends BaseApi {
    public ServerApi(Context context) {
        super(context);
    }

    @Override
    public void connect(final Request request) {

        switch (request.getMethod()) {
            case Request.Method.GET:
                setBuilder(new ANRequest.GetRequestBuilder(request.geUrl()),request);
                break;
            case Request.Method.POST:
                setBuilder(new ANRequest.PostRequestBuilder<>(request.geUrl()),request);
                break;
            case Request.Method.DELETE:
                setBuilder(new ANRequest.DeleteRequestBuilder(request.geUrl()),request);
                break;
            case Request.Method.PUT:
                setBuilder(new ANRequest.PutRequestBuilder(request.geUrl()),request);
                break;
            case Request.Method.PATCH:
                setBuilder(new ANRequest.PatchRequestBuilder(request.geUrl()),request);
                break;
        }
    }

    private void setCommonInfo(RequestBuilder rb, final Request request) {
        if (request.getRequestTimeout() != 10)
            rb.setOkHttpClient(new OkHttpClient.Builder()
                    .connectTimeout(request.getRequestTimeout(), TimeUnit.SECONDS)
                    .build());

        if (request.getTag() != null)
            rb.setTag(request.getTag());

        if (request.getToken() != null)
            rb.addHeaders("token", request.getToken());

        log("connecting: " + request.geUrl());
        log("Token: " + request.getToken());
        log("Request json: " + request.getRequestJson());
    }

    private void setBuilder(ANRequest.GetRequestBuilder requestBuilder,final Request request) {
        setCommonInfo(requestBuilder,request);

        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.PostRequestBuilder requestBuilder,final Request request) {
        setCommonInfo(requestBuilder,request);
        if (request.getRequestJson() != null)
            requestBuilder.addStringBody(request.getRequestJson().toString());

        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.DeleteRequestBuilder requestBuilder,final Request request) {
        setCommonInfo(requestBuilder,request);
        if (request.getRequestJson() != null)
            requestBuilder.addStringBody(request.getRequestJson().toString());

        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.PutRequestBuilder requestBuilder,final Request request) {
        setCommonInfo(requestBuilder,request);
        if (request.getRequestJson() != null)
            requestBuilder.addStringBody(request.getRequestJson().toString());

        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.PatchRequestBuilder requestBuilder,final Request request) {
        setCommonInfo(requestBuilder,request);
        if (request.getRequestJson() != null)
            requestBuilder.addStringBody(request.getRequestJson().toString());

        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private StringRequestListener getStringRequestListener(final Request request) {
        return new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                log("onResponse");
                log(response);
                try {
                    JsonObject result = JsonParser.parseString(response).getAsJsonObject();
                    request.setResponseJson(result);
                    ServerApi.super.onValidResponse();
                } catch (Exception e) {
                    log("onResponse: catch!: " + e.getMessage());
                    e.printStackTrace();
                    ServerApi.super.onResponseParseError();
                }
            }

            @Override
            public void onError(ANError anError) {
                log("onError");
                try {
                    log(anError.getErrorBody());
                    JsonObject result = JsonParser.parseString(anError.getErrorBody()).getAsJsonObject();
                    request.setResponseJson(result);
                    ServerApi.super.onValidResponse();
                } catch (Exception e) {
                    log("onError: catch!: " + e.getMessage());
                    e.printStackTrace();
                    ServerApi.super.onResponseParseError();
                }
            }
        };
    }
}
