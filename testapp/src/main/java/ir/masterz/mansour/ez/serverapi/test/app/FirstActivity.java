package ir.masterz.mansour.ez.serverapi.test.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import ir.masterz.mansour.ez.serverapi.JsonBuilder;
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
        //init();
        getMatches();
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

    private void getMatches() {

        G.API.request("http://dotat.ir/v1/tournament/getMatches")
                .setToken("f47xgpoxozd8r9j4ueiog6behp2kziab90uebm45")
                .setRequestJason(new JsonBuilder().add("bracket_id", 1).build())
                .setCustomCallback(new ListenerServerApi() {
                    @Override
                    public void onSuccess(String s, JsonObject jsonObject) {
                    }

                    @Override
                    public void onErrorMessage(String s, JsonObject jsonObject) {

                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onResponse() {

                    }
                });
    }
}