package ir.masterz.mansour.ez.serverapi.callback.basic;

import com.google.gson.JsonObject;


/**
 * Created by Sora on 11/8/2016.
 */

public interface ErrorCallback {

    void onErrorMessage(String message, JsonObject data);

}
