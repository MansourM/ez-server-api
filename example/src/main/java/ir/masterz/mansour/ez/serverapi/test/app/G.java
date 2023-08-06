package ir.masterz.mansour.ez.serverapi.test.app;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;

import ir.masterz.mansour.ez.serverapi.BaseApi;
import ir.masterz.mansour.ez.serverapi.fan.ServerApi;

//TODO: Server api add setPermenantToken

public class G extends Application {

    public static Context APP_CONTEXT;
    public static ServerApi API;


    @Override
    public void onCreate() {
        super.onCreate();

        APP_CONTEXT = getApplicationContext();

        API = new ServerApi(APP_CONTEXT);
        API.getConfig().setLogging(true);
        API.getConfig().setLoggingTag("Mansour_API");
        API.setErrorMessageHandler(new MyApiCallbackDefaultHandler());

    }

    public static void message(String msg) {
        Toast.makeText(APP_CONTEXT, msg, Toast.LENGTH_SHORT).show();
    }

    private static class MyApiCallbackDefaultHandler extends BaseApi.CallbackDefaultHandler {

        @Override
        public void handleErrorMessage(String message, JsonObject data) {
            G.message("Default Error Message Handler, msg: " + message);
        }

        @Override
        public void handleFailure() {
            G.message("Connection Error!");
        }
    }


}
