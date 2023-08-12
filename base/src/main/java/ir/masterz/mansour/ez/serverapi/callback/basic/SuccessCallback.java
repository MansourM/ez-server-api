package ir.masterz.mansour.ez.serverapi.callback.basic;

import com.google.gson.JsonObject;


/**
 * Created by Sora on 11/8/2016.
 */

public interface SuccessCallback {

    void onSuccess(String message, JsonObject response);

}
