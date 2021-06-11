package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;


/**
 * Created by Sora on 11/8/2016.
 */

public interface ListenerServerApi {

    void onSuccess(String message, JsonObject data);

    void onErrorMessage(String message, JsonObject data);

    void onFailure();

    void onResponse();
}
