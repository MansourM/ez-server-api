package ir.masterz.mansour.ez.serverapi.test.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.Locale;

import ir.masterz.mansour.ez.serverapi.JsonBuilder;
import ir.masterz.mansour.ez.serverapi.Request;
import ir.masterz.mansour.ez.serverapi.UrlHelper;
import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;
import ir.masterz.mansour.ez.serverapi.callback.composit.SeApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.composit.SerApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.composit.SerfApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.composit.SrApiCallback;
import ir.masterz.mansour.fan.core.AndroidNetworking;
import ir.masterz.mansour.fan.core.common.Priority;
import ir.masterz.mansour.fan.core.error.ANError;
import ir.masterz.mansour.fan.core.interfaces.DownloadListener;
import ir.masterz.mansour.fan.core.interfaces.DownloadProgressListener;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "activity_first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/

        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onCreate");

        findViewById(R.id.btn_malfored).setOnClickListener(v -> malformedResponse());
        findViewById(R.id.btn_test_1).setOnClickListener(v -> test1());
        findViewById(R.id.btn_test_2).setOnClickListener(v -> test2());
        findViewById(R.id.btn_test_3).setOnClickListener(v -> test3());
        findViewById(R.id.btn_test_4).setOnClickListener(v -> test4());
        findViewById(R.id.btn_test_5).setOnClickListener(v -> test5());
        //errorMessageWithData();
    }

    private void malformedResponse() {
        G.API.request("http://10.0.2.2:8081/test/malfored")
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject response) {

                    }
                });
    }

    private void test1() {
        Log.d(TAG, "test1");
        G.API.request("http://10.0.2.2:8081/test/test1")
                .setRequestJason(new JsonBuilder("test", "test1").build())
                .setCustomCallback(new SrApiCallback() {
                    @Override
                    public void onResponse() {
                        Log.d(TAG, "response");
                    }

                    @Override
                    public void onSuccess(String message, JsonObject response) {
                        Log.d(TAG, "success");
                    }
                });
    }

    private void test2() {
        String localFileName = "test.txt";
        Log.d(TAG, "local fileName=" + localFileName);
        AndroidNetworking.download("http://10.0.2.2:8081/export/totals", this.getFilesDir().getPath())
                .useServerFileName(localFileName)
                .setTag("downloadTest")
                .addHeaders("token", "y7yv72454qm5a946745egf7c201jcvhy")
                .addQueryParameter("start_date", "2022-11-29")
                .addQueryParameter("end_date", "2022-11-29")
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {

                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete(String fileName) {
                        Log.d(TAG, "server fileName=" + fileName);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError");
                        Log.d(TAG, anError.getMessage());
                        Log.d(TAG, anError.getErrorDetail());

                    }
                });
    }

    private void successMessage() {
        G.API.request(URL.SUCCESS_MESSAGE)
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject response) {


                    }
                });
    }

    private void successMessageWithData() {
        G.API.request(URL.SUCCESS_MESSAGE_WITH_DATA)
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject response) {


                    }
                });
    }

    private void errorMessage() {

        G.API.request(URL.ERROR_MESSAGE)
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject response) {

                    }
                });
    }

    private void errorMessageWithData() {

        G.API.request(URL.ERROR_MESSAGE_WITH_DATA)
                .setCustomCallback(new SeApiCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject response) {

                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject response) {
                        G.message("Error handled from activity, msg: " + message);
                    }
                });
    }

    private void exmaple1() {
        G.API.request(URL.ERROR_MESSAGE)
                .setToken("f47xgpoxozd8r9j4ueiog6behp2kziab90uebm45")
                .setRequestJason(new JsonBuilder().add("bracket_id", 1).build())
                .setCustomCallback(new SeApiCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject response) {

                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject response) {

                    }

                });
    }

    private void test3() {
        Log.d(TAG, "test3");
        G.API.request("http://192.168.1.22:8084/api/v1/app/ping")
                .setMethod(Request.Method.POST)
                .setRequestJason(new JsonBuilder("key1", "value1").add("key2", "value2").build())
                .setRequestTimeout(10) //seconds
                .setToken("public") //adds "token" to header
                .setCustomCallback(new SerfApiCallback() {

                    @Override
                    public void onSuccess(String message, JsonObject response) {
                        //response code is //200-300
                    }

                    @Override
                    public void onFailure() {
                        //no proper response
                        //e.g. no internet or parse error
                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject response) {
                        //response code is //400-500
                    }

                    @Override
                    public void onResponse() {
                        //request in finished
                        //used for thins like loading(false)
                    }
                });


    }

    private void test4() {
        Log.d(TAG, "test4");
        G.API.request("http://192.168.1.22:8084/api/v1/app/ping")
                .setMethod(Request.Method.GET)
                .setRequestJason(new JsonBuilder("key1", "value1").add("key2", "value2").build())
                .setRequestTimeout(10) //seconds
                .setHeaderAcceptJson() // adds "Accept" -> "application/json header
                .setToken("public") //adds "token" to header
                .addHeader("custom", "header")
                .setCustomCallback(new SerfApiCallback() {

                    @Override
                    public void onSuccess(String message, JsonObject response) {
                        //response code is //200-299
                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject response) {
                        //response code is //400-499
                    }

                    @Override
                    public void onResponse() {
                        //request in finished
                        //used for thins like loading(false)
                    }

                    @Override
                    public void onFailure() {
                        // response code is 500+
                        //no proper response
                        //e.g. no internet or parse error
                    }
                });
    }

    private void test5() {
        Log.d(TAG, "test5");
        G.API.request(URL.App.sendCode())
                .setRequestJason(new JsonBuilder("mobile", "09112223344").add("test", "t").build())
                .setCustomCallback(new SrApiCallback() {
                    @Override
                    public void onResponse() {

                    }

                    @Override
                    public void onSuccess(String message, JsonObject data) {
                    }
                });
    }
}