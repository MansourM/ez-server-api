package ir.masterz.mansour.ez.serverapi.test.app;

import ir.masterz.mansour.ez.serverapi.Request;
import ir.masterz.mansour.ez.serverapi.UrlHelper;

public class URL {

    public static final String BASE = "http://dotat.ir/v1";

    public static final String SUCCESS_MESSAGE = BASE + "/test/successMessage";
    public static final String SUCCESS_MESSAGE_WITH_DATA = BASE + "/test/successMessageWithData";

    public static final String ERROR_MESSAGE = BASE + "/test/errorMessage";
    public static final String ERROR_MESSAGE_WITH_DATA = BASE + "/test/errorMessageWithData";

    public static class App {
        public static String baseUrl() {
            return BASE + "/app";
        }

        public static UrlHelper ping() {
            return UrlHelper.get(baseUrl() + "/ping");
        }

        public static UrlHelper sendCode() {
            return UrlHelper.post("http://192.168.1.11:8084/api/v1/app/sendCode");
        }
    }

}
