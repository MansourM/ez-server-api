package ir.masterz.mansour.ez.serverapi.test.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import ir.masterz.mansour.ez.serverapi.ListenerServerApi;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "activity_first";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onCreate");
        init();
    }

    private void init() {
        G.API.request("http://dotat.ir/v1/app/init")
                .setCustomCallback(new ListenerServerApi() {
                    @Override
                    public void onSuccess(String message, JsonObject data) {


                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject data) {

                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onResponse() {
                        Log.d(TAG, "onResponse");
                    }
                });
    }
}