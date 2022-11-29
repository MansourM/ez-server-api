package ir.masterz.mansour.ez.serverapi.test.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.masterz.mansour.ez.serverapi.JsonBuilder;
import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;
import ir.masterz.mansour.ez.serverapi.callback.composit.SeApiCallback;
import ir.masterz.mansour.ez.serverapi.callback.composit.SrApiCallback;
import ir.masterz.mansour.fan.core.AndroidNetworking;
import ir.masterz.mansour.fan.core.common.Priority;
import ir.masterz.mansour.fan.core.error.ANError;
import ir.masterz.mansour.fan.core.interfaces.DownloadListener;
import ir.masterz.mansour.fan.core.interfaces.DownloadProgressListener;
import okhttp3.Response;

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

        findViewById(R.id.btn_malfored).setOnClickListener(v -> malformedResponse());
        findViewById(R.id.btn_test_1).setOnClickListener(v -> test1());
        findViewById(R.id.btn_test_2).setOnClickListener(v -> test2());
        //errorMessageWithData();
    }

    private void malformedResponse() {
        G.API.request("http://10.0.2.2:8081/test/malfored")
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject data) {

                    }
                });
    }

    private void test1() {
        Log.d(TAG, "test1");
        G.API.request("http://10.0.2.2:8081/test/test1")
                .setCustomCallback(new SrApiCallback() {
                    @Override
                    public void onResponse() {
                        Log.d(TAG, "response");
                    }

                    @Override
                    public void onSuccess(String message, JsonObject data) {
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
                    public void onSuccess(String message, JsonObject data) {


                    }
                });
    }

    private void successMessageWithData() {
        G.API.request(URL.SUCCESS_MESSAGE_WITH_DATA)
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject data) {


                    }
                });
    }

    private void errorMessage() {

        G.API.request(URL.ERROR_MESSAGE)
                .setCustomCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject data) {

                    }
                });
    }

    private void errorMessageWithData() {

        G.API.request(URL.ERROR_MESSAGE_WITH_DATA)
                .setCustomCallback(new SeApiCallback() {
                    @Override
                    public void onSuccess(String message, JsonObject data) {

                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject data) {
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
                    public void onSuccess(String message, JsonObject data) {

                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject data) {

                    }

                });
    }
}