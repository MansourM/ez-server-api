package ir.masterz.mansour.ez.serverapi.fan;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ir.masterz.mansour.ez.serverapi.BaseApi;
import ir.masterz.mansour.ez.serverapi.Request;
import ir.masterz.mansour.fan.core.common.ANRequest;
import ir.masterz.mansour.fan.core.common.RequestBuilder;
import ir.masterz.mansour.fan.core.error.ANError;
import ir.masterz.mansour.fan.core.interfaces.StringRequestListener;
import okhttp3.OkHttpClient;
import okio.Buffer;

public class ServerApi extends BaseApi {
    public ServerApi(Context context) {
        super(context);
    }

    @Override
    public void connect(final Request request) {

        switch (request.getMethod()) {
            case Request.Method.GET:
                setBuilder(new ANRequest.GetRequestBuilder(request.getUrl()), request);
                break;
            case Request.Method.POST:
                setBuilder(new ANRequest.PostRequestBuilder<>(request.getUrl()), request);
                break;
            case Request.Method.DELETE:
                setBuilder(new ANRequest.DeleteRequestBuilder(request.getUrl()), request);
                break;
            case Request.Method.PUT:
                setBuilder(new ANRequest.PutRequestBuilder(request.getUrl()), request);
                break;
            case Request.Method.PATCH:
                setBuilder(new ANRequest.PatchRequestBuilder(request.getUrl()), request);
                break;
        }
    }

    private void setCommonInfo(RequestBuilder rb, final Request request) {
        if (request.getRequestTimeout() != 10) {
            log("setting timeout:" + request.getRequestTimeout());
            rb.setOkHttpClient(new OkHttpClient.Builder()
                    .connectTimeout(request.getRequestTimeout(), TimeUnit.SECONDS)
                    .build());
        }

        //default config headers
        if (!getConfig().getHeaders().isEmpty()) {
            log("client headers: " + getConfig().getHeaders().toString());
            rb.addHeaders(getConfig().getHeaders());
        }

        //request headers
        if (!request.getHeaders().isEmpty()) {
            log("request headers: " + request.getHeaders().toString());
            rb.addHeaders(request.getHeaders());
        }

        if (request.getTag() != null)
            rb.setTag(request.getTag());
    }

    private void setBuilder(ANRequest.GetRequestBuilder requestBuilder, final Request request) {
        setCommonInfo(requestBuilder, request);

        if (request.getRequestJson() != null)
            loge("GET request can't have a body (well ik technically it can :), try POST or sending via query params");

        log("connecting: " + request.getUrl());
        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.PostRequestBuilder requestBuilder, final Request request) {
        setCommonInfo(requestBuilder, request);
        if (request.getRequestJson() != null) {
            log("Post Request json: " + request.getRequestJson().toString());
            requestBuilder.setContentType("application/json");
            requestBuilder.addStringBody(request.getRequestJson().toString());
        }

        log("connecting: " + request.getUrl());
        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.DeleteRequestBuilder requestBuilder, final Request request) {
        setCommonInfo(requestBuilder, request);
        if (request.getRequestJson() != null) {
            log("Delete Request json: " + request.getRequestJson().toString());
            requestBuilder.setContentType("application/json");
            requestBuilder.addStringBody(request.getRequestJson().toString());
        }

        log("connecting: " + request.getUrl());
        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.PutRequestBuilder requestBuilder, final Request request) {
        setCommonInfo(requestBuilder, request);
        if (request.getRequestJson() != null) {
            log("Put Request json: " + request.getRequestJson().toString());
            requestBuilder.setContentType("application/json");
            requestBuilder.addStringBody(request.getRequestJson().toString());
        }

        log("connecting: " + request.getUrl());
        requestBuilder.build().getAsString(getStringRequestListener(request));
    }

    private void setBuilder(ANRequest.PatchRequestBuilder requestBuilder, final Request request) {
        setCommonInfo(requestBuilder, request);
        if (request.getRequestJson() != null) {
            log("Patch Request json: " + request.getRequestJson().toString());
            requestBuilder.setContentType("application/json");
            requestBuilder.addStringBody(request.getRequestJson().toString());
        }

        log("connecting: " + request.getUrl());
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
                    log("onResponse->catch!: " + e.getMessage());
                    e.printStackTrace();
                    ServerApi.super.onResponseParseError();
                }
            }

            @Override
            public void onError(ANError anError) {
                log("onError");
                try {
                    log("error body:");
                    log(anError.getErrorBody());
                    JsonObject result = JsonParser.parseString(anError.getErrorBody()).getAsJsonObject();
                    request.setResponseJson(result);
                    ServerApi.super.onValidResponse();
                } catch (Exception e) {
                    log("onError->catch! can't parse response (not json or invalid schema): " + e.getMessage());
                    e.printStackTrace();
                    ServerApi.super.onResponseParseError();
                }
            }
        };
    }
}
